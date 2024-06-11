import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.INDEFINITE;
      }
    };
    function = FunctionFactory.createSinFunction();
  }

  /**
   * Test finding root of a simple function.
   * @throws FunctionException if error occurs during root finding
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    double x0 = 0.5;
    double x1 = 1.5;
    double root = Math.PI / 4;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), 1e-5);
  }

  /**
   * Test finding root of function with multiple roots.
   * @throws FunctionException if error occurs during root finding
   */
  @Test
  public void testFindRootFunctionWithMultipleRoots() throws FunctionException {
    double x0 = 0;
    double x1 = 2;
    double root1 = 1.0;
    double root2 = -1.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // root1 is found with higher accuracy
    assertEquals(root1, solver.getRoot(), 1e-6);
    solver.reset();

    // reset solver and find root2
    solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));

    assertEquals(root2, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding root of function with negative root.
   * @throws FunctionException if error occurs during root finding
   */
  @Test
  public void testFindRootFunctionWithNegativeRoot() throws FunctionException {
    double x0 = 0;
    double x1 = 1;
    double root = -Math.sqrt(2);

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding root of function with non-converging interval.
   * @throws FunctionException if error occurs during root finding
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNonConvergingInterval() throws FunctionException {
    double x0 = 0;
    double x1 = 1;
    double root = Math.sqrt(2);

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (x > 0.5) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.INDEFINITE;
      }
    };

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    fail("Expected FunctionException when interval is not converging.");
  }
}