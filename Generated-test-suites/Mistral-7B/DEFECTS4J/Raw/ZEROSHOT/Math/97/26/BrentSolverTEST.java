import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double tolerance = 1e-5;
        return Math.abs(xHigh - xLow) < tolerance || Math.abs(fHigh) < tolerance ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
  }

  @Test
  public void testFindRootSimple() throws FunctionException {
    ComputableFunction function = new RationalFunction("x^3 - 2");
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;
    assertEquals(-1.0, root, 1e-9);
  }

  @Test
  public void testFindRootComplex() throws FunctionException {
    ComputableFunction function = new RationalFunction("x^5 - 3 * x^3 + 2");
    double x0 = 1.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;
    assertEquals(1.282949949296055, root, 1e-5);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    ComputableFunction function = new RationalFunction("x^2 - 4 / x");
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}