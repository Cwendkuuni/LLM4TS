import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BrentSolver}
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NO;
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
  }

  /**
   * Test finding root of a simple function
   * @throws FunctionException when the solver fails
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(Math.sqrt(2.0), root, 0.001);
  }

  /**
   * Test finding root of a function with roots at end points of interval
   * @throws FunctionException when the solver fails
   */
  @Test
  public void testFindRootRootAtEndPoints() throws FunctionException {
    double x0 = 1.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(1.0, root, 0.001);
  }

  /**
   * Test finding root of a function with roots between end points of interval
   * @throws FunctionException when the solver fails
   */
  @Test
  public void testFindRootRootBetweenEndPoints() throws FunctionException {
    double x0 = 0.5;
    double x1 = 1.5;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(1.0, root, 0.001);
  }

  /**
   * Test finding root of a function with a convergence failure
   */
  @Test
  public void testFindRootConvergenceFailure() {
    double x0 = 0.0;
    double x1 = 1.0;
    // Function with roots at 0 and 1, but solver will not converge since roots are too close
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(Math.E, (1.0 / x)) - Math.pow(Math.E, (1.0 / (x + 0.1)));
      }
    };
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  /**
   * Test finding root of a function with a failure due to maximal number of iterations
   */
  @Test
  public void testFindRootMaxIterations() {
    double x0 = 0.0;
    double x1 = 1.0;
    // Function with roots at 0 and 1, but solver will exceed the maximal number of iterations
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        if (x == 0) {
          return 0.0;
        }
        return Math.pow(Math.E, (1.0 / x)) - Math.pow(Math.E, (1.0 / (x + 0.1)));
      }
    };
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }
}