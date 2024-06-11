import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private RationalFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 1e-10) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fLow) < 1e-10) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = new RationalFunction(new double[]{-1, 4, 0, 3});
  }

  /**
   * Test finding a root with a simple function.
   */
  @Test
  public void testSimple() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  /**
   * Test finding a root with a function with multiple roots.
   */
  @Test
  public void testMultipleRoots() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(-1.0, solver.getRoot(), 1e-10);
    assertEquals(1.0, solver.getRoot(), 1e-10);
  }

  /**
   * Test finding a root with a function with no root in the interval.
   */
  @Test
  public void testNoRoot() throws FunctionException {
    double x0 = 0.0;
    double x1 = 4.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(Double.isFinite(solver.getRoot()));
  }

  /**
   * Test finding a root with a function with a singularity in the interval.
   */
  @Test
  public void testSingularity() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    double x0 = 1.0;
    double x1 = 2.0;
    assertThrows(FunctionException.class, () -> {
      solver.findRoot(new RationalFunction(new double[]{-1, 0, 0, 0}), checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    });
  }
}