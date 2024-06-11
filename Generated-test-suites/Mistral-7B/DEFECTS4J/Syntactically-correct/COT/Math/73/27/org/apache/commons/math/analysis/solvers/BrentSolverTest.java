import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 2;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return (f > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();

    Assert.assertNotEquals(Double.NaN, root);
    Assert.assertEquals(0.0, Math.abs(Math.pow(root, 2) + 2), 0.001);

    // edge case: root very close to the boundary
    x0 = 0.01;
    x1 = 1.0;
    root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    Assert.assertNotEquals(Double.NaN, root);
    Assert.assertEquals(0.0, Math.abs(Math.pow(root, 2) + 2), 0.001);

    // edge case: root at the boundary
    x0 = -1.0;
    x1 = -0.1;
    root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    Assert.assertNotEquals(Double.NaN, root);
    Assert.assertEquals(0.0, Math.abs(Math.pow(root, 2) + 2), 0.001);

    // exception handling: function throws an exception
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        if (x > 1) {
          throw new Exception("Function not defined for this x");
        }
        return Math.pow(x, 2) - 2;
      }
    };
    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }

    // exception handling: checker throws an exception
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) throws Exception {
        if (x > 1) {
          throw new Exception("Checker not defined for this x");
        }
        return (f > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };
    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.fail("Expected ConvergenceCheckerException");
    } catch (Exception e) {
      // expected
    }
  }

}