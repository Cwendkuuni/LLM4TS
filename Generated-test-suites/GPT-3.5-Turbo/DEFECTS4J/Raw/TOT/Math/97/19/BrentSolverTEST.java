import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = -2.0;
    double x1 = 1.0;
    double f1 = 2.0;

    BrentSolver solver = new BrentSolver();

    try {
      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      Assert.assertTrue("Root should be found in the given interval", result);
      double expectedRoot = 0.0;
      double actualRoot = solver.getRoot();
      Assert.assertEquals("Root value is incorrect", expectedRoot, actualRoot, 0.000001);
    } catch (FunctionException e) {
      Assert.fail("Exception should not be thrown");
    }
  }

  @Test
  public void testFindRoot_NoRootInInterval() {
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = 100;
    double x0 = -2.0;
    double f0 = -2.0;
    double x1 = -1.0;
    double f1 = -1.0;

    BrentSolver solver = new BrentSolver();

    try {
      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      Assert.assertFalse("No root should be found in the given interval", result);
    } catch (FunctionException e) {
      Assert.fail("Exception should not be thrown");
    }
  }

  @Test
  public void testFindRoot_ExceptionThrown() {
    ComputableFunction function = new TestFunctionWithException();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = -2.0;
    double x1 = 1.0;
    double f1 = 2.0;

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      Assert.fail("FunctionException should be thrown");
    } catch (FunctionException e) {
      // Exception expected
    }
  }

  private class TestFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) throws FunctionException {
      return x * x - 1.0;
    }

  }

  private class TestFunctionWithException implements ComputableFunction {

    @Override
    public double valueAt(double x) throws FunctionException {
      throw new FunctionException("Function exception");
    }

  }

  private class TestConvergenceChecker implements ConvergenceChecker {

    @Override
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 0.000001) {
        return ConvergenceChecker.LOW;
      } else if (Math.abs(fHigh) < 0.000001) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.DIVERGED;
      }
    }

  }

}