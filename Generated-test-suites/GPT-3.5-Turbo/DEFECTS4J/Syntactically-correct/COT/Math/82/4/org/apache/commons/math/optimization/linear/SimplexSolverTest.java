import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

  @Test
  public void testGetPivotColumn() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    // Test case 1
    tableau.setEntry(0, 0, -1.0);
    tableau.setEntry(0, 1, -2.0);
    Integer pivotColumn = solver.getPivotColumn(tableau);
    assertEquals(Integer.valueOf(0), pivotColumn);

    // Test case 2
    tableau.setEntry(0, 0, -2.0);
    tableau.setEntry(0, 1, -1.0);
    pivotColumn = solver.getPivotColumn(tableau);
    assertEquals(Integer.valueOf(1), pivotColumn);

    // Test case 3
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    pivotColumn = solver.getPivotColumn(tableau);
    assertNull(pivotColumn);
  }

  @Test
  public void testGetPivotRow() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    // Test case 1
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(1, 0, 2.0);
    tableau.setEntry(2, 0, 3.0);
    Integer pivotRow = solver.getPivotRow(0, tableau);
    assertEquals(Integer.valueOf(1), pivotRow);

    // Test case 2
    tableau.setEntry(0, 0, 2.0);
    tableau.setEntry(1, 0, 1.0);
    tableau.setEntry(2, 0, 3.0);
    pivotRow = solver.getPivotRow(0, tableau);
    assertEquals(Integer.valueOf(1), pivotRow);

    // Test case 3
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(1, 0, 3.0);
    tableau.setEntry(2, 0, 2.0);
    pivotRow = solver.getPivotRow(0, tableau);
    assertEquals(Integer.valueOf(0), pivotRow);
  }

  @Test
  public void testDoIteration() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    // Test case 1
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(1, 0, 3.0);
    tableau.setEntry(1, 1, 4.0);
    solver.doIteration(tableau);
    assertEquals(2.0, tableau.getEntry(0, 1), 0.0001);
    assertEquals(0.0, tableau.getEntry(1, 1), 0.0001);

    // Test case 2
    tableau.setEntry(0, 0, 2.0);
    tableau.setEntry(0, 1, 1.0);
    tableau.setEntry(1, 0, 4.0);
    tableau.setEntry(1, 1, 3.0);
    solver.doIteration(tableau);
    assertEquals(1.0, tableau.getEntry(0, 0), 0.0001);
    assertEquals(0.0, tableau.getEntry(1, 0), 0.0001);

    // Test case 3
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 3.0);
    tableau.setEntry(1, 0, 2.0);
    tableau.setEntry(1, 1, 4.0);
    solver.doIteration(tableau);
    assertEquals(2.0, tableau.getEntry(0, 1), 0.0001);
    assertEquals(0.0, tableau.getEntry(1, 1), 0.0001);
  }

  @Test
  public void testIsPhase1Solved() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    // Test case 1
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    boolean isSolved = solver.isPhase1Solved(tableau);
    assertFalse(isSolved);

    // Test case 2
    tableau.setEntry(0, 0, 2.0);
    tableau.setEntry(0, 1, 1.0);
    isSolved = solver.isPhase1Solved(tableau);
    assertFalse(isSolved);

    // Test case 3
    tableau.setEntry(0, 0, -1.0);
    tableau.setEntry(0, 1, -2.0);
    isSolved = solver.isPhase1Solved(tableau);
    assertTrue(isSolved);
  }

  @Test
  public void testIsOptimal() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    // Test case 1
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    boolean isOptimal = solver.isOptimal(tableau);
    assertFalse(isOptimal);

    // Test case 2
    tableau.setEntry(0, 0, 2.0);
    tableau.setEntry(0, 1, 1.0);
    isOptimal = solver.isOptimal(tableau);
    assertFalse(isOptimal);

    // Test case 3
    tableau.setEntry(0, 0, 0.0);
    tableau.setEntry(0, 1, 0.0);
    isOptimal = solver.isOptimal(tableau);
    assertTrue(isOptimal);
  }

  @Test
  public void testSolvePhase1() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    // Test case 1
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    try {
      solver.solvePhase1(tableau);
      fail("NoFeasibleSolutionException not thrown.");
    } catch (NoFeasibleSolutionException e) {
      // Test passed
    } catch (OptimizationException e) {
      fail("Unexpected exception.");
    }

    // Test case 2
    tableau.setEntry(0, 0, -1.0);
    tableau.setEntry(0, 1, -2.0);
    try {
      solver.solvePhase1(tableau);
      fail("NoFeasibleSolutionException not thrown.");
    } catch (NoFeasibleSolutionException e) {
      // Test passed
    } catch (OptimizationException e) {
      fail("Unexpected exception.");
    }

    // Test case 3
    tableau.setEntry(0, 0, 0.0);
    tableau.setEntry(0, 1, 0.0);
    try {
      solver.solvePhase1(tableau);
      // Test passed
    } catch (OptimizationException e) {
      fail("Unexpected exception.");
    }
  }

  @Test
  public void testDoOptimize() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    // Test case 1
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    RealPointValuePair solution = solver.doOptimize();
    assertNotNull(solution);

    // Test case 2
    tableau.setEntry(0, 0, 0.0);
    tableau.setEntry(0, 1, 0.0);
    solution = solver.doOptimize();
    assertNotNull(solution);

    // Test case 3
    tableau.setEntry(0, 0, -1.0);
    tableau.setEntry(0, 1, -2.0);
    try {
      solution = solver.doOptimize();
      assertNull(solution);
    } catch (OptimizationException e) {
      fail("Unexpected exception.");
    }
  }
}