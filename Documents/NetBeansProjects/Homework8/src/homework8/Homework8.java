//import java.util.Arrays;
package homework8;
import java.util.ArrayList;
import java.util.List;

public class Homework8 {
  // Constants
  private static final Integer ARRAY_SIZE = 15; // 15 postitions
  private static final Integer OVER = 0;    
  private static final Integer TO = 1;
  private static final Integer START_PEG = 0;
  private static final char PRESENT_CHAR = 'x'; // peg is occupied 
  private static final char MISSING_CHAR = '.'; // peg is empty

  //     0
  //    1 2
  //   3 4 5
  //  6 7 8 9
  // A B C D E
  //
  // array of available moves
  private static final Integer[][][] MOVES = {
    { {1, 3}, {2, 5} }, // 0
    { {3, 6}, {4, 8} }, // 1 
    { {4, 7}, {5, 9} }, // 2
    { {1, 0}, {6, 10}, {7, 12}, {4, 5} }, // 3
    { {7, 11}, {8, 13} }, // 4
    { {2, 0}, {4, 3}, {8, 12}, {9, 14} }, // 5
    { {3, 1}, {7, 8} }, // 6
    { {4, 2}, {8, 9} }, // 7
    { {4, 1}, {7, 6} }, // 8
    { {8, 7}, {5, 2} }, // 9
    { {11, 12}, {6, 3} }, // 10
    { {7, 4}, {12, 13} }, // 11
    { {7, 3}, {8, 5},  {11, 10}, {13, 14} }, // 12
    { {8, 4}, {12, 11} }, // 13
    { {9, 5}, {13, 12} }  // 14
  };

  private boolean[] board  = new boolean[ARRAY_SIZE];

  // The current running solution 
  private List<String> solution = new ArrayList<String>();

  private boolean solved = false;

  // Default constructor
  public Homework8() {
    reset();
  }

  // Return the character this peg should be in string
  private char getDrawChar(Integer peg) {
    return (occupied(peg)) ? PRESENT_CHAR : MISSING_CHAR;
  }

  // Draw the board to a string
  private String drawBoard() {
    String boardString;

    char[] toDraw = new char[ARRAY_SIZE];

    for (int i = 0; i < ARRAY_SIZE; i++) {
      toDraw[i] = getDrawChar(i);
    }
    
    boardString  = "     " + toDraw[0] + "\n";
    boardString += "    "  + toDraw[1]  + " " + toDraw[2] + "\n";
    boardString += "   "   + toDraw[3]  + " " + toDraw[4]  + " " + toDraw[5] + "\n";
    boardString += "  "    + toDraw[6]  + " " + toDraw[7]  + " " + toDraw[8]  + " "
                           + toDraw[9]  + "\n";
    boardString += " "     + toDraw[10] + " " + toDraw[11] + " " + toDraw[12] + " "
                           + toDraw[13] + " " + toDraw[14] + "\n";

    return boardString;
  }

  private boolean isInvalidPegIndex(Integer peg) {
    return (peg < 0 || peg >= ARRAY_SIZE);
  }

  private boolean occupied(Integer peg) {
    if (isInvalidPegIndex(peg)) {
      return false;
    }

    return board[peg];
  }

  private boolean empty(Integer peg) {
    return !occupied(peg);
  }

  private Integer currentNumberOfPegs() {
    Integer numPegs = 0;

    for (boolean present : board) {
      if (present) {
        numPegs++;
      }
    }
    return numPegs;
  }

  private void updatePeg(Integer peg, boolean set) {
    if (isInvalidPegIndex(peg)) {
      return;
    }
    
    board[peg] = set;
  }

  private void removePeg(Integer peg) {
    updatePeg(peg, false);
  }

  private void addPeg(Integer peg) {
    updatePeg(peg, true);
  }

  private void reset() {
    solution.clear();

    for (int i = 0; i < ARRAY_SIZE; i++) {
      addPeg(i);
    }
  }

  // Initialize board with starting peg
  private void initializeBoard(Integer peg) {
    reset();
    solved = false;
    removePeg(peg);

    solution.add(drawBoard());
  }

  private void initializeBoard() {
    initializeBoard(START_PEG);
  }

  // turns solution into a string
  private String intoString() {
    StringBuilder sb = new StringBuilder();
    
    for (String step : solution) {
      sb.append(step + "\n");
    }

    return sb.toString();
  }
  
  public String play(Integer peg) {
    if (isInvalidPegIndex(peg)) {
      System.out.println("Invalid peg");
      peg = START_PEG;
    }
    
    System.out.println("     " + peg + "    ");
    System.out.println("============");
    
    initializeBoard(peg);
    solve();

    return intoString();
  }
  
  private boolean isSolved() {
    return (currentNumberOfPegs() == 1);
  }

  private boolean invalidMove(Integer from, Integer over, Integer to) {
    return (empty(from) || empty(over) || occupied(to));
  }

  private boolean move(Integer from, Integer over, Integer to) {
    if (invalidMove(from, over, to)) {
      return false;
    }

    // Remove peg that is jumping and the one jumping over
    removePeg(from);
    removePeg(over);
    
    addPeg(to);
    return true;
  }

  // Solves the puzzle
  private void solve() {
    if (isSolved()) {
      solved = true;
      return;
    }

    
    for (int peg = 0; peg < ARRAY_SIZE; peg++)
    {
      if (empty(peg)) 
      {
        continue;
      }

      for (Integer[] move : MOVES[peg]) 
      {
        int from = peg;        // starting postition
        int over = move[OVER];  // over
        int to   = move[TO];   // to

        // save board
        boolean[] savedBoard = board.clone();

        // if no move is made then continue
        if (move(from, over, to) == false) {
          continue;
        }

        // add current board to the solution
        String currentBoard = drawBoard();
        solution.add(currentBoard);

        //solve the puzzle
        solve();
        if (isSolved()) 
        {
          solved = true;
          return;
        }

        // if couldnt solve go back to the saved board
        board = savedBoard;
        // and remove the string some the solution
        solution.remove(solution.size() - 1);
      }
    }
  }

  public static void main(String[] args) {
    //create new instance
     Homework8 boardGame = new Homework8();
    
    // Print first five solutions 
    for (int peg = 0; peg < 5; peg++) 
    {
      String solution = boardGame.play(peg);
      System.out.println(solution);
        //print(solution);
    }
  }
}