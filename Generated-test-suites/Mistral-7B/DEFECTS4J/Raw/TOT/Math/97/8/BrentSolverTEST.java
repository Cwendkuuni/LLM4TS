import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NEITHER;
      }
    };
  }

  /**
   * Test finding root of a function in a given interval using BrentSolver.
   */
  @Test
  public void testFindRoot() throws FunctionException {
    DummyFunction function = new DummyFunction(3.0);
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  /**
   * Test error scenario when function value is not defined in interval bounds.
   */
  @Test(expected = FunctionException.class)
  public void testFunctionException() throws FunctionException {
    DummyFunction function = new DummyFunction(3.0, -1.0, 1.0); // function undefined at x = 0
    double x0 = 0.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, function.valueAt(x1));
  }

  /**
   * Test edge case when interval bounds are almost equal.
   */
  @Test
  public void testEdgeCase() throws FunctionException {
    DummyFunction function = new DummyFunction(Math.sin);
    double x0 = 0.0;
    double x1 = 0.001;
    double expectedRoot = 0.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

}