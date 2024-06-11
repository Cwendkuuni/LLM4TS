import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() throws FunctionException {
    ComputableFunction function = new SimpleFunction();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue("Find root failed", result);
    Assert.assertNotNull("Root is not set", solver.getRoot());
    Assert.assertEquals("Root value is incorrect", root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_FunctionWithZeroDerivative() throws FunctionException {
    ComputableFunction function = new ZeroDerivativeFunction();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue("Find root failed", result);
    Assert.assertNotNull("Root is not set", solver.getRoot());
    Assert.assertEquals("Root value is incorrect", root, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionThrowsException() throws FunctionException {
    ComputableFunction function = new ThrowingFunction();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.fail("Expected FunctionException to be thrown");
    } catch (FunctionException ex) {
      // Expected
    }

  }

  @Test
  public void testFindRoot_FunctionWithManyRoots() throws FunctionException {
    ComputableFunction function = new ManyRootsFunction();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.2; // There are two roots, but only one is tested here

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue("Find root failed", result);
    Assert.assertNotNull("Root is not set", solver.getRoot());
    Assert.assertEquals("Root value is incorrect", root, solver.getRoot(), epsilon);
  }

}

class SimpleFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return x * x - 2.0;
  }
}

class ZeroDerivativeFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.pow(x - 1.5, 2);
  }
}

class ThrowingFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    if (x < -1.0) {
      throw new FunctionException("Function is undefined for this x value");
    }
    return Math.sqrt(Math.abs(x));
  }
}

class ManyRootsFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    double result = Math.pow(Math.sin(x), 2) - Math.pow(Math.cos(x), 2);
    return result;
  }
}