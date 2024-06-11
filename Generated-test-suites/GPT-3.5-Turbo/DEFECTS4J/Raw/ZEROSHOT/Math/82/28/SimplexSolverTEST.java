import org.junit.Test;
import org.junit.Assert;

public class SimplexSolverTest {

  @Test
  public void testGetPivotColumn() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
    Integer result = solver.getPivotColumn(tableau);
    Assert.assertEquals(expectedResult, result);
  }

  @Test
  public void testGetPivotRow() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
    Integer result = solver.getPivotRow(col, tableau);
    Assert.assertEquals(expectedResult, result);
  }

  @Test
  public void testDoIteration() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
    solver.doIteration(tableau);
    Assert.assertEquals(expectedResult, tableau);
  }

  @Test
  public void testIsPhase1Solved() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
    boolean result = solver.isPhase1Solved(tableau);
    Assert.assertEquals(expectedResult, result);
  }

  @Test
  public void testIsOptimal() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
    boolean result = solver.isOptimal(tableau);
    Assert.assertEquals(expectedResult, result);
  }

  @Test
  public void testSolvePhase1() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
    solver.solvePhase1(tableau);
    Assert.assertEquals(expectedResult, tableau);
  }

  @Test
  public void testDoOptimize() {
    SimplexSolver solver = new SimplexSolver();
    RealPointValuePair result = solver.doOptimize();
    Assert.assertEquals(expectedResult, result);
  }
  
}