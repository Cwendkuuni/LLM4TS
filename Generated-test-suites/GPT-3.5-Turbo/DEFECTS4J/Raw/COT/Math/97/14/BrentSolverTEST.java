import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Initialize test data
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    int maxIter = 100;

    // Create ComputableFunction implementation for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1.0; // Function: f(x) = x^2 - 1
      }
    };

    // Create ConvergenceChecker implementation for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW; // f(xLow) is close to zero
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH; // f(xHigh) is close to zero
        } else {
          return ConvergenceChecker.NONE; // Not converged
        }
      }
    };

    try {
      boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

      assertTrue(foundRoot);
      assertEquals(1.0, solver.getRoot(), 1e-6); // The root should be close to 1
    } catch (FunctionException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testFindRootException() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Initialize test data
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    int maxIter = 100;

    // Create ComputableFunction implementation for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        if (x == 0.0) {
          throw new FunctionException("Cannot evaluate function at x = 0");
        }
        return x * x - 1.0; // Function: f(x) = x^2 - 1
      }
    };

    // Create ConvergenceChecker implementation for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW; // f(xLow) is close to zero
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH; // f(xHigh) is close to zero
        } else {
          return ConvergenceChecker.NONE; // Not converged
        }
      }
    };

    try {
      boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail("Exception should be thrown"); // This line should not be reached
    } catch (FunctionException e) {
      // Exception should be thrown
      assertEquals("Cannot evaluate function at x = 0", e.getMessage());
    }
  }
}