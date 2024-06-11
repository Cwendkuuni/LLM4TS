import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver brentSolver;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
  }

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        return LOW;
      }
    };

    int maxIter = 100;
    double x0 = -2;
    double f0 = -4;
    double x1 = 2;
    double f1 = 0;

    boolean result = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(2.0, brentSolver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRoot_withNoRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        return LOW;
      }
    };

    int maxIter = 100;
    double x0 = -1;
    double f0 = Math.sin(x0);
    double x1 = 1;
    double f1 = Math.sin(x1);

    boolean result = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

  @Test
  public void testFindRoot_withMultipleRoots() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        if (x < -2) {
          return (x + 1) * (x + 2) * (x + 3);
        } else if (x > 1) {
          return (x - 1) * (x - 2) * (x - 3);
        } else {
          return x * x;
        }
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        return LOW;
      }
    };

    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean result = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertTrue(brentSolver.getRoot() < -2 || brentSolver.getRoot() > 1);
  }

}