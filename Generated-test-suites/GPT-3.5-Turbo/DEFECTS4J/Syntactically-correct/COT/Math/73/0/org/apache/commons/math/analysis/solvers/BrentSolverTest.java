import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        if (Math.abs(y1) < 1e-7) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(y2) < 1e-7) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 1000;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, maxIter, x0, y0, x1, y1);
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-7);
  }

  // Additional test cases for edge cases and exceptions

  @Test
  public void testFindRoot_withNegativeValues() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        if (Math.abs(y1) < 1e-7) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(y2) < 1e-7) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 1000;
    double x0 = -2.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, maxIter, x0, y0, x1, y1);
    assertTrue(result);
    assertEquals(-2.0, solver.getRoot(), 1e-7);
  }

  @Test
  public void testFindRoot_withLargeInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        if (Math.abs(y1) < 1e-7) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(y2) < 1e-7) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 1000;
    double x0 = -1000.0;
    double f0 = function.valueAt(x0);
    double x1 = 1000.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, maxIter, x0, y0, x1, y1);
    assertTrue(result);
    assertEquals(-2.0, solver.getRoot(), 1e-7);
  }

}