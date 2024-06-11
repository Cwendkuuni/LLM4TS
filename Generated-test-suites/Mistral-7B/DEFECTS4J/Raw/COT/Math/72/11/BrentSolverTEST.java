import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleFunction() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertFalse("Root not found", Double.isNaN(solver.getRoot()));
    Assert.assertEquals("Root not equal to theoretical value", -1.4142135625, solver.getRoot(), 1e-5);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x > 0) {
          throw new FunctionException("Function not defined for positive x.");
        }
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = -1.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_convergence() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        double threshold = 1e-8;
        return Math.abs(fx) < threshold ? ConvergenceChecker.LOW : (Math.abs(x - xPrevious) < threshold ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE);
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = FunctionConstants.PI / 2;
    double x1 = 3.0 * FunctionConstants.PI / 2;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertEquals("Root not equal to theoretical value", Math.PI, solver.getRoot(), 1e-5);
  }

}