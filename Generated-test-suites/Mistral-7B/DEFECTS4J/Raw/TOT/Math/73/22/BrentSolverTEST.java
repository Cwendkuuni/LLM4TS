import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * JUnit test for BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionFactory functionFactory;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    functionFactory = new FunctionFactory();
  }

  @Test
  public void testFindRootSimple() throws Exception {
    ComputableFunction function = functionFactory.function(x -> x * x - 2);
    double x0 = 1.0;
    double x1 = 2.0;
    double root = -1.0;
    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 1e-5);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootAtRoot() throws Exception {
    ComputableFunction function = functionFactory.function(Math::sin);
    double x0 = 1.0;
    double x1 = 1.0;
    double root = 0.0;
    boolean result = solver.findRoot(function, checker, 10, x0, Math.sin(x0), x1, Math.sin(x1));
    assertEquals(root, solver.getRoot(), 1e-5);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootOutsideInterval() throws Exception {
    ComputableFunction function = functionFactory.function(x -> Math.pow(x, 2) - 4);
    double x0 = -1.0;
    double x1 = 1.0;
    double root = 2.0;
    assertFalse(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(Double.NaN, solver.getRoot());
  }

  @Test
  public void testFindRootFunctionException() throws Exception {
    ComputableFunction function = (x) -> { throw new FunctionException("Test exception"); };
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0));
  }
}