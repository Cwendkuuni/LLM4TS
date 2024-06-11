import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

  @Test
  public void testGetPivotColumn() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // typical use case
    tableau.setEntry(0, 0, -5);
    tableau.setEntry(0, 1, -10);
    tableau.setEntry(0, 2, -15);
    Integer expectedPivotColumn = 2;
    assertEquals(expectedPivotColumn, solver.getPivotColumn(tableau));
    
    // edge case: only one negative coefficient
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 10);
    tableau.setEntry(0, 2, -15);
    expectedPivotColumn = 2;
    assertEquals(expectedPivotColumn, solver.getPivotColumn(tableau));
    
    // edge case: all coefficients are positive
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 10);
    tableau.setEntry(0, 2, 15);
    expectedPivotColumn = null;
    assertEquals(expectedPivotColumn, solver.getPivotColumn(tableau));
  }
  
  @Test
  public void testGetPivotRow() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // typical use case
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, 3);
    tableau.setEntry(1, 1, 5);
    tableau.setEntry(1, 2, 10);
    tableau.setEntry(2, 0, 2);
    tableau.setEntry(2, 1, 8);
    tableau.setEntry(2, 2, 15);
    Integer expectedPivotRow = 1;
    assertEquals(expectedPivotRow, solver.getPivotRow(2, tableau));
    
    // edge case: no positive entry in the column
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, 3);
    tableau.setEntry(1, 1, 5);
    tableau.setEntry(1, 2, 10);
    tableau.setEntry(2, 0, -2);
    tableau.setEntry(2, 1, -8);
    tableau.setEntry(2, 2, -15);
    expectedPivotRow = null;
    assertEquals(expectedPivotRow, solver.getPivotRow(2, tableau));
    
    // edge case: all ratios are negative
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, -3);
    tableau.setEntry(1, 1, -5);
    tableau.setEntry(1, 2, -10);
    tableau.setEntry(2, 0, -2);
    tableau.setEntry(2, 1, -8);
    tableau.setEntry(2, 2, -15);
    expectedPivotRow = null;
    assertEquals(expectedPivotRow, solver.getPivotRow(2, tableau));
  }
  
  @Test
  public void testIsPhase1Solved() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // typical use case: phase 1 not solved
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    Boolean expectedPhase1Solved = false;
    assertEquals(expectedPhase1Solved, solver.isPhase1Solved(tableau));
    
    // typical use case: phase 1 solved
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, -3);
    tableau.setEntry(1, 1, -5);
    tableau.setEntry(1, 2, -10);
    tableau.setNumArtificialVariables(0);
    expectedPhase1Solved = true;
    assertEquals(expectedPhase1Solved, solver.isPhase1Solved(tableau));
    
    // edge case: tableau has no artificial variables
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, -3);
    tableau.setEntry(1, 1, -5);
    tableau.setEntry(1, 2, -10);
    tableau.setNumArtificialVariables(1);
    expectedPhase1Solved = false;
    assertEquals(expectedPhase1Solved, solver.isPhase1Solved(tableau));
  }
  
  @Test
  public void testIsOptimal() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // typical use case: not optimal
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    Boolean expectedOptimal = false;
    assertEquals(expectedOptimal, solver.isOptimal(tableau));
    
    // typical use case: optimal
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, -3);
    tableau.setEntry(1, 1, -5);
    tableau.setEntry(1, 2, -10);
    tableau.setNumArtificialVariables(0);
    expectedOptimal = true;
    assertEquals(expectedOptimal, solver.isOptimal(tableau));
    
    // edge case: tableau has artificial variables
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, -3);
    tableau.setEntry(1, 1, -5);
    tableau.setEntry(1, 2, -10);
    tableau.setNumArtificialVariables(1);
    expectedOptimal = false;
    assertEquals(expectedOptimal, solver.isOptimal(tableau));
  }
  
  @Test(expected = NoFeasibleSolutionException.class)
  public void testSolvePhase1_noFeasibleSolutionException() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // typical use case
    tableau.setEntry(0, 0, 5);
    tableau.setEntry(0, 1, 7);
    tableau.setEntry(0, 2, 2);
    tableau.setEntry(1, 0, -3);
    tableau.setEntry(1, 1, -5);
    tableau.setEntry(1, 2, -10);
    solver.solvePhase1(tableau);
  }
  
  @Test
  public void testDoOptimize() {
    SimplexSolver solver = new SimplexSolver();
    
    // typical use case: optimal solution found
    double[] f = {5, 7, 2};
    LinearConstraint[] constraints = {
      new LinearConstraint(new double[] {-3, -5, -10}, Relationship.GEQ, 0)
    };
    GoalType goalType = GoalType.MINIMIZE;
    boolean restrictToNonNegative = false;
    double epsilon = 1.0e-6;
    RealPointValuePair expectedSolution = new RealPointValuePair(new double[] {0, 0, 0}, 0);
    RealPointValuePair actualSolution = solver.doOptimize(f, constraints, goalType, restrictToNonNegative, epsilon);
    assertEquals(expectedSolution, actualSolution);
    
    // edge case: no feasible solution
    f = new double[] {5, 7, 2};
    constraints = new LinearConstraint[0];
    goalType = GoalType.MINIMIZE;
    restrictToNonNegative = false;
    epsilon = 1.0e-6;
    expectedSolution = null;
    actualSolution = solver.doOptimize(f, constraints, goalType, restrictToNonNegative, epsilon);
    assertEquals(expectedSolution, actualSolution);
  }
}