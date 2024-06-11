import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private RationalFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(xLow - xHigh) < 0.0001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.INDEFINITE;
      }
    };
    function = new RationalFunction(1.0, 2.0, 1.0, 3.0);
  }

  @Test
  public void testFindRoot_basic() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = () -> { throw new FunctionException("Test exception"); };
    solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);
  }

  @Test
  public void testFindRoot_convergenceChecker_high() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.HIGH;
      }
    };

    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(Double.NaN, solver.getRoot(), 0.001);

  }

  @Test
  public void testFindRoot_convergenceChecker_low() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };

    root = solver.getRoot(); // reset the root to NaN
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_maxIter() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(Double.NaN, solver.getRoot(), 0.001);

    solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(Double.NaN, solver.getRoot(), 0.001);

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

}