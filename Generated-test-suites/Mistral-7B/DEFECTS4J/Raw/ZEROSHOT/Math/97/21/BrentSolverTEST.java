import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.test.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private TestFunctions functions;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        if (Math.abs(fx1) < Math.abs(fx2)) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx2) < Double.MIN_VALUE) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.UNDEFINED;
        }
      }
    };
    functions = new TestFunctions();
  }

  @Test
  public void testFindRoot_simple() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = functions.f1;
    int maxIter = 10;

    Assert.assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    double root = solver.getRoot();
    Assert.assertEquals(1.449695, root, 0.0001);
  }

  @Test
  public void testFindRoot_complex() throws FunctionException {
    double x0 = -10.0;
    double x1 = 10.0;
    ComputableFunction function = functions.f4;
    int maxIter = 100;

    Assert.assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    double root = solver.getRoot();
    Assert.assertEquals(0.0, root, 0.0001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_failure() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    ComputableFunction function = functions.f5;
    int maxIter = 10;

    Assert.assertFalse(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

}