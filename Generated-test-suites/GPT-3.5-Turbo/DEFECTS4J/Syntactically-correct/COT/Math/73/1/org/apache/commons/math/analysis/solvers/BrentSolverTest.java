import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Test case 1
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    boolean result = solver.findRoot(function, null, 100, 1, -3, 4, 12);
    Assert.assertTrue(result);
    Assert.assertEquals(2.0, solver.getRoot(), 0.0001);

    // Test case 2
    solver = new BrentSolver();
    function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    result = solver.findRoot(function, null, 100, 0, -1, 2, 1);
    Assert.assertTrue(result);
    Assert.assertEquals(0.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_edgeCases() throws FunctionException {
    // Test case 1
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 0;
      }
    };
    boolean result = solver.findRoot(function, null, 100, 1, 0, 2, 0);
    Assert.assertTrue(result);
    Assert.assertEquals(1.0, solver.getRoot(), 0.0001);

    // Test case 2
    solver = new BrentSolver();
    function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 3);
      }
    };
    result = solver.findRoot(function, null, 100, -2, -8, 0, 0);
    Assert.assertTrue(result);
    Assert.assertEquals(0.0, solver.getRoot(), 0.0001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_exceptionHandling() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Test exception");
      }
    };
    solver.findRoot(function, null, 100, 0, 0, 1, 1);
  }

}