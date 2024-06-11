import org.junit.*;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case 1: Basic functionality
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double tol = 1e-6;
        if (Math.abs(fLow) < tol) {
          return LOW;
        } else if (Math.abs(fHigh) < tol) {
          return HIGH;
        } else {
          return NOT_CONVERGED;
        }
      }
    };
    int maxIter = 100;
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 1e-6);

    // Test case 2: Edge case (lower bound of interval is root)
    x0 = -1.0;
    f0 = 0.0;
    x1 = 1.0;
    f1 = 1.0;
    result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 1e-6);

    // Test case 3: Edge case (upper bound of interval is root)
    x0 = -1.0;
    f0 = -1.0;
    x1 = 1.0;
    f1 = 0.0;
    result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 1e-6);

    // Test case 4: Exception handling (throw FunctionException in function)
    x0 = 0.0;
    f0 = -1.0;
    x1 = 1.0;
    f1 = 1.0;
    ComputableFunction functionWithException = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function evaluation error");
      }
    };
    try {
      solver.findRoot(functionWithException, checker, maxIter, x0, f0, x1, f1);
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      assertEquals("Function evaluation error", e.getMessage());
    }

    // Test case 5: Edge case (tolerance is very small)
    x0 = 0.0;
    f0 = -1e-10;
    x1 = 1e-10;
    f1 = 1e-10;
    result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }

}