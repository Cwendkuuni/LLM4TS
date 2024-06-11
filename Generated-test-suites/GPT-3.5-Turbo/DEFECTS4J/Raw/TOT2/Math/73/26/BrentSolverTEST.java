import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    double result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // function x^2 - 4
      }
    }, new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(f0) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    }, 100, 0.0, -4.0, 2.0, 0.0);
    assertEquals("Root should be 2.0", 2.0, result, 1e-6);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals("Default root should be NaN", Double.NaN, solver.getRoot(), 0.0);
  }

  @Test
  public void testInvalidFunctionException() {
    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(null, null, 100, 0.0, -4.0, 2.0, 0.0);
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      // Expected
    }
  }

  @Test
  public void testMaxIterationsExceeded() {
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x + 5; // function x^2 + 5
      }
    }, new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    }, 5, 0.0, 5.0, 2.0, 9.0);
    assertFalse("Max iterations exceeded, result should be false", result);
  }

  // Add more test cases for other methods if needed

}