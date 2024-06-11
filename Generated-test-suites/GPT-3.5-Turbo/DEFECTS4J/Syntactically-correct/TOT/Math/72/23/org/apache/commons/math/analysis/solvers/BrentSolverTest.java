import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setup() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    checker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return (Math.abs(f1) < 1e-6) ? HIGH : NOT_CONVERGED;
      }
    };
  }

  @Test
  public void testFindRoot_typicalCase() {
    boolean result = solver.findRoot(function, checker, 100, -2.0, -8.0, 2.0, 0.0);
    Assert.assertTrue(result);
    Assert.assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_edgeCase() {
    boolean result = solver.findRoot(function, checker, 100, -1.0, -5.0, 1.0, -3.0);
    Assert.assertTrue(result);
    Assert.assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_errorScenario() {
    boolean result = false;
    try {
      result = solver.findRoot(function, checker, 10, -2.0, 0.0, 2.0, 0.0);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception");
    }
    Assert.assertFalse(result);
    Assert.assertTrue(Double.isNaN(solver.getRoot()));
  }
}