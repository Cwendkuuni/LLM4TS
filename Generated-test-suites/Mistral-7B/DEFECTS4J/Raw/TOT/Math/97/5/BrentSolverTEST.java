import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionsTest;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test finding a root with a simple function.
   */
  @Test
  public void testFindRootSimpleFunction() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new FunctionsTest.SimpleFunction();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test finding a root with a function with multiple roots.
   */
  @Test
  public void testFindRootFunctionMultipleRoots() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new FunctionsTest.QuadraticFunction(1.0, -2.0, 1.0);

    double x0 = -2.0;
    double x1 = 2.0;
    double root1 = -1.0;
    double root2 = 1.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root1, solver.getRoot(), epsilon);

    solver.reset();
    Assert.assertTrue(solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0)));
    Assert.assertEquals(root2, solver.getRoot(), epsilon);
  }

  /**
   * Test finding a root with a function with no root in the interval.
   */
  @Test
  public void testFindRootFunctionNoRoot() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new FunctionsTest.FunctionWithNoRoot();

    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  /**
   * Test finding a root with a function with a singular point in the interval.
   */
  @Test
  public void testFindRootFunctionSingularPoint() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new FunctionsTest.FunctionWithSingularPoint();

    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  /**
   * Test finding a root with a function with a large interval.
   */
  @Test
  public void testFindRootFunctionLargeInterval() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new FunctionsTest.LargeIntervalFunction();

    double x0 = -1000.0;
    double x1 = 1000.0;
    double root = 0.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

}