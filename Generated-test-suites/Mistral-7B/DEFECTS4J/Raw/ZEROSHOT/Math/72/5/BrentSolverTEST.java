import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtil;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        double error = Math.abs(x2 - x1);
        if (error < 1e-9) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f1) < 1e-9) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    function = FunctionUtil.powXM1;
  }

  @Test
  public void testBrentSolverSimple() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertNotNaN(solver.getRoot());
    double expectedRoot = 1.5;
    Assert.assertEquals(expectedRoot, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testBrentSolverException() throws Exception {
    double x0 = 1.0;
    double x1 = 0.0;
    int maxIter = 10;

    solver.findRoot(FunctionUtil.fXMX, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}