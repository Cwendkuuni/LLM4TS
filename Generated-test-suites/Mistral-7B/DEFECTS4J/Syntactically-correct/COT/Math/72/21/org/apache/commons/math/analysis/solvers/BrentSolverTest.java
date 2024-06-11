import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test with a simple function: x^2 - 1
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.UNDEFINED;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue(result);
    Assert.assertEquals(0.0, solver.getRoot(), 0.001);

    // Test with a function with roots at -1 and 1
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 2;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if ((Math.abs(fx) < 0.01) && (Math.abs(x - 1.0) < 0.01) || (Math.abs(fx) < 0.01) && (Math.abs(x + 1.0) < 0.01)) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.UNDEFINED;
      }
    };

    x0 = -2.0;
    x1 = 2.0;

    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue(result);
    Assert.assertEquals(1.0, solver.getRoot(), 0.01);

    // Test with a function that does not have a root in the interval
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.UNDEFINED;
      }
    };

    x0 = 0.0;
    x1 = 2.0;

    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }
  }

}