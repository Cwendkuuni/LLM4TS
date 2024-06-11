import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    boolean result = brentSolver.findRoot(function, convergenceChecker, 100, -2.0, -1.0, 2.0, 1.0);
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, brentSolver.getRoot());

    double expectedRoot = -1.2;
    Assert.assertEquals(expectedRoot, brentSolver.getRoot(), 0.001);

    result = brentSolver.findRoot(function, convergenceChecker, 100, 2.0, 3.0, 4.0, 5.0);
    Assert.assertFalse(result);

  }

  @Test
  public void testFindRootFunctionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0) {
          throw new FunctionException("Function is not defined for negative x");
        }
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    try {
      brentSolver.findRoot(function, convergenceChecker, 100, -1.0, 0.0, 1.0, 1.0);
      Assert.fail("FunctionException should be thrown");
    } catch (FunctionException e) {
      // expected
    }

  }

}