import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionMath;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return x > x2 ? ConvergenceChecker.LOW : (fx < fx2 ? ConvergenceChecker.HIGH : ConvergenceChecker.NOT_CONVERGED);
      }
    };
    function = new FunctionMath() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
      }
    };
  }

  /**
   * Test the solver with a function for which we know a root.
   */
  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 0.6;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test the solver with a function for which we know no root in the given interval.
   */
  @Test
  public void testNoRoot() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double functionValue = function.valueAt(0.5);

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, functionValue);
    assertFalse(result);
  }

  /**
   * Test the solver with a function for which the root is at the boundary of the interval.
   */
  @Test
  public void testRootAtBoundary() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test the solver with a function for which the root is at an endpoint of the interval.
   */
  @Test
  public void testRootAtEndpoint() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 1.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }
}