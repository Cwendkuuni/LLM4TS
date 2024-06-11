import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws FunctionException if an error occurs while computing the root
   */
  @Test
  public void testFindRoot() throws FunctionException {
    // Test case 1: function with one root
    ComputableFunction function = FunctionFactory.createFunction(Math::sin);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, Math.sin(x0), x1, Math.sin(x1)));
    Assert.assertEquals(root, solver.getRoot(), 1e-6);

    // Test case 2: function with multiple roots
    function = FunctionFactory.createFunction(x -> x * x * x - 2);

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.HIGH;
      }
    };

    solver = new BrentSolver();
    x0 = 1.0;
    x1 = -1.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(-1.0, solver.getRoot(), 1e-6);

    // Test case 3: function with no root
    function = FunctionFactory.createFunction(x -> Math.exp(x) - 2);

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };

    solver = new BrentSolver();
    x0 = -5.0;
    x1 = 5.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, Math.exp(x0) - 2, x1, Math.exp(x1) - 2));
  }

  /**
   * Test method for {@link BrentSolver#getRoot()}.
   */
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.createFunction(x -> x * x - 2);
    double root = 1.0;

    try {
      solver.findRoot(function, new ConvergenceChecker() {
        @Override
        public int converged(double x, double fx, double xLow, double fxLow) {
          return ConvergenceChecker.LOW;
        }
      }, 100, -1.0, Double.NEGATIVE_INFINITY, 1.0, Double.POSITIVE_INFINITY);
    } catch (FunctionException e) {
      // Ignore
    }

    Assert.assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with an invalid convergence checker.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootWithInvalidChecker() throws FunctionException {
    ComputableFunction function = FunctionFactory.createFunction(x -> x * x - 2);
    BrentSolver solver = new BrentSolver();

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        throw new RuntimeException("Invalid checker.");
      }
    };

    solver.findRoot(function, checker, 100, -1.0, Double.NEGATIVE_INFINITY, 1.0, Double.POSITIVE_INFINITY);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with an invalid function.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootWithInvalidFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };

    ComputableFunction function = null;
    solver.findRoot(function, checker, 100, -1.0, Double.NEGATIVE_INFINITY, 1.0, Double.POSITIVE_INFINITY);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with an invalid interval.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootWithInvalidInterval() throws FunctionException {
    ComputableFunction function = FunctionFactory.createFunction(x -> x * x - 2);
    BrentSolver solver = new BrentSolver();

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };

    solver.findRoot(function, checker, 100, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that does not converge.
   */
  @Test
  public void testFindRootWithFunctionThatDoesNotConverge() throws FunctionException {
    ComputableFunction function = FunctionFactory.createFunction(x -> Math.sin(x) / x);
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };

    try {
      solver.findRoot(function, checker, 100, -10.0, Math.sin(Math.PI), 0.0, Math.sin(0));
    } catch (FunctionException e) {
      // Expect this exception
    }
  }

}