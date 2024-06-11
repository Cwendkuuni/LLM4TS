import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new SimpleFunction(); // Create a valid function to test with
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        // A simple checker that considers the function converged when the difference between x1 and x2 is less than a given tolerance
        return Math.abs(x1 - x2) < 0.001 ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0); // Arbitrary initial values and interval
    double root = solver.getRoot();

    Assert.assertNotNull("Root should not be null", root);
    Assert.assertTrue("Root should be within the interval", root >= 1.0 && root <= 3.0);
  }

  @Test
  public void testFindRootWithFunctionException() throws FunctionException {
    ComputableFunction function = new ThrowsFunction(); // Create a function that throws an exception
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0);
      Assert.fail("A FunctionException should have been thrown");
    } catch (FunctionException e) {
      // Expected
    }
  }

  @Test
  public void testFindRootWithMaxIter() throws FunctionException {
    ComputableFunction function = new SimpleFunction(); // Create a valid function to test with
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return Math.abs(x1 - x2) < 0.001 ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 0, 1.0, 2.0, 3.0, 4.0); // Max iterations should be greater than zero
      Assert.fail("A FunctionException should have been thrown");
    } catch (FunctionException e) {
      // Expected
    }
  }

  @Test
  public void testFindRootWithInvalidInterval() throws FunctionException {
    ComputableFunction function = new SimpleFunction(); // Create a valid function to test with
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return Math.abs(x1 - x2) < 0.001 ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, Double.NEGATIVE_INFINITY, 2.0, Double.POSITIVE_INFINITY, 3.0); // Interval should not be invalid
      Assert.fail("A FunctionException should have been thrown");
    } catch (FunctionException e) {
      // Expected
    }
  }

}

class SimpleFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.pow(x, 2) - 4.0;
  }
}

class ThrowsFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    if (x > 0) {
      throw new FunctionException("FunctionException message");
    } else {
      return Math.pow(x, 2) - 4.0;
    }
  }
}