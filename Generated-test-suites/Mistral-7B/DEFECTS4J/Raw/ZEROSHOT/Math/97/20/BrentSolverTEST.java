import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

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
        return ConvergenceChecker.LOW;
      }
    };
    function = new RationalFunction("x^3 - 2*x^2 + x - 1");
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", -0.9288733538952386, root, 0.00001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 0.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testGetRoot() {
    double root;
    try {
      root = solver.findRoot(function, checker, 100, 1.0, function.valueAt(1.0), 2.0, function.valueAt(2.0));
    } catch (FunctionException e) {
      root = Double.NaN;
    }
    assertEquals("Root not initialized", root, -0.9288733538952386);
  }

}