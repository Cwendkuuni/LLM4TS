import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker convergenceChecker;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
  }

  @Test
  public void testFindRootSimple() {
    ComputableFunction function = FunctionUtils.newComputableFunction("x^3 - 2", x -> Math.pow(x, 3) - 2);
    double x0 = 1.0;
    double x1 = 2.0;

    assertTrue(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(1.3398856894052734, brentSolver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootMultipleRoots() {
    ComputableFunction function = FunctionUtils.newComputableFunction("(x - 1)^2 * (x - 3)", x -> Math.pow(x - 1, 2) * Math.pow(x - 3, 2));
    double x0 = 1.5;
    double x1 = 2.5;

    assertTrue(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(1.960220763247112, brentSolver.getRoot(), 0.0001);

    brentSolver.reset();

    assertTrue(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(2.9997836842094366, brentSolver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootNoRoots() {
    ComputableFunction function = FunctionUtils.newComputableFunction("x^2 + 1", x -> Math.pow(x, 2) + 1);
    double x0 = 0.5;
    double x1 = 1.5;

    assertFalse(brentSolver.findRoot(function, convergenceChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertNotNull(brentSolver.getRoot());
  }

}