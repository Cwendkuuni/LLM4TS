import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int convergence(double x1, double f1, double x2, double f2) {
        if (Math.abs(x1 - x2) < 1e-6 && Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.BOTH;
        }
        if (Math.abs(x1 - x2) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
      }
    };
  }

  @Test
  public void testRoot() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertTrue("Root should be found", !Double.isNaN(root));
    assertEquals("Root should be approximately -0.5", -0.5, root, 1e-6);
  }

  @Test
  public void testNoRoot() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    assertFalse("No root should be found", solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

}