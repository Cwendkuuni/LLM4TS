import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleCase() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return x1 < x2 ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = x0 * x0 - 2.0;
    double x1 = 2.0;
    double f1 = x1 * x1 - 2.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Function error");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = 0.0;
    double x1 = 2.0;
    double f1 = 0.0;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

  @Test
  public void testFindRoot_convergence() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) / x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return Math.abs(fx1) < 0.001 ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.sin(x0) / x0;
    double x1 = 2.0;
    double f1 = Math.sin(x1) / x1;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
  }

  @Test
  public void testFindRoot_maxIter() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3.0) - 2.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 3.0) - 2.0;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 3.0) - 2.0;

    Assert.assertFalse(solver.findRoot(function, checker, 0, x0, f0, x1, f1));
  }

  @Test
  public void testFindRoot_interval_noRoot() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2.0) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double f0 = Math.pow(x0, 2.0) - 4.0;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 2.0) - 4.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2.0;
      }
    }, new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return x1 < x2 ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    }, 100, 1.0, 0.0, 2.0, 0.0);

    Assert.assertEquals(-1.0, solver.getRoot(), 0.0);
  }

}