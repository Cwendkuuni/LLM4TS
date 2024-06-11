import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) + Math.sin(x) - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return Math.abs(fx) < 0.001 ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    Assert.assertNotNull(root);
    Assert.assertTrue(Math.abs(Math.sin(root) + Math.pow(root, 3) - 2) < 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFunctionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Error message");
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
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentException() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(null, null, 100, 1.0, 1.0, 2.0, 2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionFunction() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = null;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    solver.findRoot(function, checker, 100, 1.0, 1.0, 2.0, 2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionChecker() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };
    ConvergenceChecker checker = null;
    solver.findRoot(function, checker, 100, 1.0, 1.0, 2.0, 2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionInterval() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    solver.findRoot(function, checker, 100, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
  }

}