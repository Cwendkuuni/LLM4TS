import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

  @Test
  public void testGetPivotColumn() {
    double[][] matrix = {
      {1.0, 2.0, -1.0, 0.0},
      {5.0, -1.0, 3.0, -5.0},
      {2.0, 1.0, -1.0, -1.0}
    };
    SimplexTableau tableau = new SimplexTableau(matrix);
    SimplexSolver solver = new SimplexSolver();
    Integer pivotColumn = solver.getPivotColumn(tableau);
    assertEquals(2, pivotColumn.intValue());
  }

  @Test
  public void testGetPivotRow() {
    double[][] matrix = {
      {1.0, 2.0, -1.0, 0.0},
      {5.0, -1.0, 3.0, -5.0},
      {2.0, 1.0, -1.0, -1.0}
    };
    SimplexTableau tableau = new SimplexTableau(matrix);
    SimplexSolver solver = new SimplexSolver();
    Integer pivotColumn = solver.getPivotColumn(tableau);
    Integer pivotRow = solver.getPivotRow(pivotColumn.intValue(), tableau);
    assertEquals(0, pivotRow.intValue());
  }

  @Test
  public void testDoIteration() {
    double[][] matrix = {
      {1.0, 2.0, -1.0, 0.0},
      {5.0, -1.0, 3.0, -5.0},
      {2.0, 1.0, -1.0, -1.0}
    };
    SimplexTableau tableau = new SimplexTableau(matrix);
    SimplexSolver solver = new SimplexSolver();
    solver.doIteration(tableau);
    double[][] expectedMatrix = {
      {1.0, 0.0, 0.5, 1.0},
      {0.0, 0.0, -4.0, -10.0},
      {0.0, 1.0, -2.0, -1.0}
    };
    assertArrayEquals(expectedMatrix, tableau.getMatrix());
  }

  @Test
  public void testIsPhase1Solved() {
    double[][] matrix = {
      {1.0, 2.0, -1.0, 0.0},
      {5.0, -1.0, 3.0, -5.0},
      {2.0, 1.0, -1.0, -1.0}
    };
    SimplexTableau tableau = new SimplexTableau(matrix);
    SimplexSolver solver = new SimplexSolver();
    boolean isPhase1Solved = solver.isPhase1Solved(tableau);
    assertFalse(isPhase1Solved);
  }

  @Test
  public void testIsOptimal() {
    double[][] matrix = {
      {1.0, 0.0, 0.5, 1.0},
      {0.0, 0.0, -4.0, -10.0},
      {0.0, 1.0, -2.0, -1.0}
    };
    SimplexTableau tableau = new SimplexTableau(matrix);
    SimplexSolver solver = new SimplexSolver();
    boolean isOptimal = solver.isOptimal(tableau);
    assertFalse(isOptimal);
  }

  @Test
  public void testSolvePhase1() {
    double[][] matrix = {
      {1.0, 2.0, -1.0, 0.0},
      {5.0, -1.0, 3.0, -5.0},
      {2.0, 1.0, -1.0, -1.0}
    };
    SimplexTableau tableau = new SimplexTableau(matrix);
    SimplexSolver solver = new SimplexSolver();
    try {
      solver.solvePhase1(tableau);
    } catch (OptimizationException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDoOptimize() {
    double[][] matrix = {
      {1.0, 2.0, -1.0, 0.0},
      {5.0, -1.0, 3.0, -5.0},
      {2.0, 1.0, -1.0, -1.0}
    };
    SimplexTableau tableau = new SimplexTableau(matrix);
    SimplexSolver solver = new SimplexSolver();
    try {
      RealPointValuePair result = solver.doOptimize();
      double expectedValue = -1.0;
      double[] expectedPoint = {3.0, 1.0, 0.0};
      assertEquals(expectedValue, result.getValue(), 0.0);
      assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
    } catch (OptimizationException e) {
      fail(e.getMessage());
    }
  }
}