import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();

    // Create a mock ComputableFunction for testing
    ComputableFunction mockFunction = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x;
      }
    };

    // Create a mock ConvergenceChecker for testing
    ConvergenceChecker mockChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(f2) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    // Test with different inputs
    boolean result = solver.findRoot(mockFunction, mockChecker, 100, 0, 0, 10, 10);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.001);

    result = solver.findRoot(mockFunction, mockChecker, 100, -10, -10, 0, 0);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.001);

    result = solver.findRoot(mockFunction, mockChecker, 100, -10, -10, 10, 10);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.001);

    result = solver.findRoot(mockFunction, mockChecker, 100, -10, -10, 0, -5);
    assertTrue(result);
    assertEquals(-5.0, solver.getRoot(), 0.001);

    result = solver.findRoot(mockFunction, mockChecker, 100, -10, -5, 0, -10);
    assertTrue(result);
    assertEquals(-10.0, solver.getRoot(), 0.001);

    result = solver.findRoot(mockFunction, mockChecker, 100, -10, -5, 10, 5);
    assertTrue(result);
    assertEquals(5.0, solver.getRoot(), 0.001);

    result = solver.findRoot(mockFunction, mockChecker, 100, -10, -5, 0, 0);
    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 0.001);
  }

}