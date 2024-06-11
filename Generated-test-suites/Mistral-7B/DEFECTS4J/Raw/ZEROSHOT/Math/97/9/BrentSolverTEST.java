import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionMock;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionMock function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(xLow - xHigh) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    function = new FunctionMock();
  }

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    function.setFunction((x) -> x * x - 4.0);
    double x0 = 2.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, x0 * x0 - 4.0, x1, x1 * x1 - 4.0));
    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionThrowingException() throws FunctionException {
    function.setThrowingException(true);

    try {
      solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRootWithFunctionNotConverging() throws FunctionException {
    function.setFunction((x) -> Math.sin(x));

    assertTrue(solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0));
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testFindRootWithFunctionConvergingToHigh() throws FunctionException {
    function.setFunction((x) -> x * x - 4.0);
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 3.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, x0 * x0 - 4.0, x1, x1 * x1 - 4.0));
    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootWithFunctionConvergingToLow() throws FunctionException {
    function.setFunction((x) -> x * x - 4.0);
    double x0 = 3.0;
    double x1 = 2.0;
    double expectedRoot = 2.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, x0 * x0 - 4.0, x1, x1 * x1 - 4.0));
    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }
}