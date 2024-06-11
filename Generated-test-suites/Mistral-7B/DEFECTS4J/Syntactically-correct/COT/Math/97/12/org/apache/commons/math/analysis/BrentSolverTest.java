import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleCase() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        return Math.pow(x, 2) - 4.0;
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
    double f0 = Math.pow(1.0, 2) - 4.0;
    double x1 = 2.0;
    double f1 = Math.pow(2.0, 2) - 4.0;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(Math.PI, Math.acos(solver.getRoot()));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        throw new FunctionException("Test exception");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_illegalArgumentException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        return x * Math.sin(1.0 / x);
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
    double f0 = Double.NaN;
    double x1 = 1.0 / Math.PI;
    double f1 = Math.sin(1.0 / x1);

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

  @Test
  public void testFindRoot_convergence() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        return Math.pow(x, 3) - 2.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return Math.abs(fx) < 1e-8 ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double f0 = Math.pow(1.0, 3) - 2.0;
    double x1 = 2.0;
    double f1 = Math.pow(2.0, 3) - 2.0;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
  }

  @Test
  public void testFindRoot_maxIter() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        return Math.sin(x);
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
    double f0 = Math.sin(x0);
    double x1 = Math.PI / 2.0;
    double f1 = Math.sin(x1);

    try {
      solver.findRoot(function, checker, 0, x0, f0, x1, f1);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFindRoot_failToFindRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        return Math.sin(x) / (1.0 - Math.pow(x, 2));
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
    double f0 = Math.sin(x0) / (1.0 - Math.pow(x0, 2));
    double x1 = 1.0;
    double f1 = Math.sin(x1) / (1.0 - Math.pow(x1, 2));

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
  }

}