import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private FunctionFactory functionFactory;
  private ConvergenceChecker convergenceChecker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    functionFactory = new FunctionFactory();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        if ((Math.abs(fx1) < 0.0001) && (Math.abs(fx2) < 0.0001)) {
          return ConvergenceChecker.BOTH;
        } else if ((Math.abs(fx1) < 0.0001)) {
          return ConvergenceChecker.LOW;
        } else if ((Math.abs(fx2) < 0.0001)) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
  }

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = functionFactory.newFunction("f", x -> Math.pow(x, 2) - 4.0);
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    solver.findRoot(function, convergenceChecker, 100, x0, Math.pow(x0, 2), x1, Math.pow(x1, 2));

    Assert.assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithNonConvergentFunction() throws FunctionException {
    ComputableFunction function = functionFactory.newFunction("g", x -> 1.0 / Math.sqrt(Math.abs(x - 1.0)));
    double x0 = 0.0;
    double x1 = 4.0;

    solver.findRoot(function, convergenceChecker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test
  public void testFindRootWithInvalidInterval() throws FunctionException {
    ComputableFunction function = functionFactory.newFunction("h", x -> Math.pow(x, 2) - 4.0);
    double x0 = 1.0;
    double x1 = 0.0;

    try {
      solver.findRoot(function, convergenceChecker, 100, x0, Math.pow(x0, 2), x1, Math.pow(x1, 2));
      Assert.fail();
    } catch (FunctionException e) {
      Assert.assertEquals("Interval should not be valid", e.getMessage());
    }
  }

  @Test
  public void testFindRootWithMaxIterationsReached() throws FunctionException {
    ComputableFunction function = functionFactory.newFunction("i", x -> Math.sin(Math.PI / x));
    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, convergenceChecker, 0, x0, Math.sin(Math.PI / x0), x1, Math.sin(Math.PI / x1));

    Assert.assertFalse(solver.getRoot() != Double.NaN);
  }

}