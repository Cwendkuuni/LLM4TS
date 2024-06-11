import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    ComputableFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : -1.0;

    Assert.assertTrue(root > 1.5 && root < 2.5);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithDivisionFunctionThrowsFunctionException() throws FunctionException {
    ComputableFunction function = new DivideFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 1.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRootWithFunctionWithDifferentRoots() throws FunctionException {
    ComputableFunction function = new DifferentRootsFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 2.0;
    double root1 = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : -1.0;
    Assert.assertEquals(-1.0, root1, 0.001);

    solver.setRoot(root1);
    double root2 = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0)) ? solver.getRoot() : -1.0;
    Assert.assertEquals(1.0, root2, 0.001);
  }

  @Test
  public void testFindRootWithFunctionWithSameRoots() throws FunctionException {
    ComputableFunction function = new SameRootsFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : -1.0;
    Assert.assertEquals(1.0, root, 0.001);
  }

  @Test
  public void testFindRootWithFunctionWithZeroDerivative() throws FunctionException {
    ComputableFunction function = new ZeroDerivativeFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : -1.0;
    Assert.assertEquals(1.0, root, 0.001);
  }

  @Test
  public void testFindRootWithFunctionWithDifferentSigns() throws FunctionException {
    ComputableFunction function = new DifferentSignsFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 2.0;
    double root1 = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : -1.0;
    Assert.assertEquals(-1.0, root1, 0.001);

    solver.setRoot(root1);
    double root2 = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0)) ? solver.getRoot() : -1.0;
    Assert.assertEquals(1.0, root2, 0.001);
  }

  private static class SimpleFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
      return Math.pow(x, 2) - 2;
    }
  }

  private static class DivideFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
      throw new ArithmeticException("Cannot divide by zero");
    }
  }

  private static class DifferentRootsFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
      return Math.pow(x, 2) - 3;
    }
  }

  private static class SameRootsFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
      return Math.pow(x, 2) - 1;
    }
  }

  private static class ZeroDerivativeFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
      return Math.pow(x, 3) - x;
    }
  }

  private static class DifferentSignsFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
      return Math.pow(x, 2) > 0 ? 1 : -1;
    }
  }

}