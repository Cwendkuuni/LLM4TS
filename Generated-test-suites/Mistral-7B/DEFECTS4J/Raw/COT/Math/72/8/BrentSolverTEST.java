import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    // Test case 1: function with a root in the given interval
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2) - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(f) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(1.0, solver.getRoot(), 0.001);

    // Test case 2: function with roots outside the given interval
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI * x) - 0.5;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(f) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();
    x0 = 0.0;
    f0 = function.valueAt(x0);
    x1 = 2.0;
    f1 = function.valueAt(x1);

    try {
      solver.findRoot(function, checker, 100, x0, f0, x1, f1);
      Assert.fail("Expected FunctionException");
    } catch (FunctionException ex) {
      // Expected
    }

    // Test case 3: function with roots at the boundary of the given interval
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.cos(Math.PI * x) - 1.0;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(f) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();
    x0 = -1.0;
    f0 = function.valueAt(x0);
    x1 = 1.0;
    f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(-1.0, solver.getRoot(), 0.01);
    Assert.assertEquals(1.0, solver.getRoot(), 0.01);

  }

}