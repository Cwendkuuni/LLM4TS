import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BrentSolver}.
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
        if (Math.abs(xHigh - xLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(xLow - xHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NEITHER;
      }
    };
    function = new RationalFunction("1 + x^2", "1");
  }

  @Test
  public void testFindRootSimpleCase() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(expectedRoot, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = () -> {
      throw new FunctionException("FunctionException message");
    };

    assertThrows(FunctionException.class, () -> {
      solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
    });
  }

  @Test
  public void testFindRootEdgeCases() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    double expectedRoot = 0.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(expectedRoot, solver.getRoot(), 1e-6);

    x0 = Double.NEGATIVE_INFINITY;
    x1 = Double.POSITIVE_INFINITY;

    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);

    assertEquals(expectedRoot, solver.getRoot(), 1e-6);
  }
}