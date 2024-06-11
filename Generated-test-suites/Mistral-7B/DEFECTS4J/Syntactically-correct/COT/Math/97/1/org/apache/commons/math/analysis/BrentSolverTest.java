import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = x0 * x0 - 4.0;
    double x1 = 2.0;
    double fx1 = x1 * x1 - 4.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
    Assert.assertNotNaN(solver.getRoot());
    Assert.assertEquals(-2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_FunctionWithMultipleRoots() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.5;
    double fx0 = Math.pow(x0, 3) - 2 * Math.pow(x0, 2) + x0;
    double x1 = 1.5;
    double fx1 = Math.pow(x1, 3) - 2 * Math.pow(x1, 2) + x1;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
    Assert.assertNotNaN(solver.getRoot());
    Assert.assertEquals(-0.5, solver.getRoot(), 1e-6);

    solver.reset(); // Reset the root to NaN

    x0 = 2.5;
    fx0 = Math.pow(x0, 3) - 2 * Math.pow(x0, 2) + x0;
    x1 = 3.5;
    fx1 = Math.pow(x1, 3) - 2 * Math.pow(x1, 2) + x1;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
    Assert.assertNotNaN(solver.getRoot());
    Assert.assertEquals(1.0, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithNoRoots() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double fx0 = Math.pow(x0, 2) + 1.0;
    double x1 = 1.0;
    double fx1 = Math.pow(x1, 2) + 1.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
  }

  @Test
  public void testFindRoot_FunctionWithNegativeValues() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double fx0 = Math.pow(x0, 2) + 1.0;
    double x1 = 0.0;
    double fx1 = Math.pow(x1, 2) + 1.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
  }

  @Test
  public void testFindRoot_FunctionWithDegenerateInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double fx0 = Math.pow(x0, 2) - 1.0;
    double x1 = 0.0;
    double fx1 = Math.pow(x1, 2) - 1.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
  }

  @Test
  public void testFindRoot_FunctionWithLargeInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(x) - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -10.0;
    double fx0 = Math.exp(x0) - 1.0;
    double x1 = 10.0;
    double fx1 = Math.exp(x1) - 1.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
    Assert.assertNotNaN(solver.getRoot());
    // Approximate value, the exact root is e - 1
    Assert.assertEquals(Math.E - 1.0, solver.getRoot(), 1e-5);
  }

  @Test
  public void testFindRoot_FunctionWithVerySmallInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double fx0 = Math.pow(x0, 2) - 1.0;
    double x1 = 1e-10;
    double fx1 = Math.pow(x1, 2) - 1.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
  }

}