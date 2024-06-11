import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
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
      public int converged(double x, double fx, double xm, double fxm) {
        if (Math.abs(fx) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fxm) < Double.MIN_VALUE) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
  }

  @Test
  public void testFindRootSimple() throws FunctionException {
    ComputableFunction function = FunctionUtils.sinFunction();
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.PI / 2.0;

    boolean result = solver.findRoot(function, checker, 100, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1));

    assertEquals(root, solver.getRoot(), 0.001);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFail() throws FunctionException {
    ComputableFunction function = FunctionUtils.functionXSquared();
    double x0 = 0.0;
    double x1 = 1.0;

    solver.findRoot(function, checker, 100, x0, FunctionUtils.square(x0), x1, FunctionUtils.square(x1));
  }
}