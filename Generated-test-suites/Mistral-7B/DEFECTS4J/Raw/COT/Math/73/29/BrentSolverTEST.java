import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    // Test case 1: Function with a simple root
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = x0 * x0 - 2.0;
    double x1 = 2.0;
    double fx1 = x1 * x1 - 2.0;
    boolean result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());

    // Test case 2: Function with a root outside the interval
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.cos(Math.PI * x);
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    fx0 = Math.cos(Math.PI * x0);
    x1 = 1.0;
    fx1 = Math.cos(Math.PI * x1);
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertFalse(result);

    // Test case 3: Function with roots at the interval endpoints
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };
    solver = new BrentSolver();
    x0 = 2.0;
    fx0 = x0 * x0 - 4.0;
    x1 = 3.0;
    fx1 = x1 * x1 - 4.0;
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertFalse(result);

    // Test case 4: Function with roots inside the interval
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };
    solver = new BrentSolver();
    x0 = 1.0;
    fx0 = x0 * x0 - 4.0;
    x1 = 3.0;
    fx1 = x1 * x1 - 4.0;
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());

    // Test case 5: Function with roots at the interval midpoint
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.NONE;
      }
    };
    solver = new BrentSolver();
    x0 = 2.0;
    fx0 = x0 * x0 - 4.0;
    x1 = 2.0;
    fx1 = x1 * x1 - 4.0;
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());

    // Test case 6: Function with roots at the interval edges and convergence
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3.0) - 2.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    fx0 = Math.pow(x0, 3.0) - 2.0;
    x1 = 1.0;
    fx1 = Math.pow(x1, 3.0) - 2.0;
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());

    // Test case 7: Function with roots at the interval edges and divergence
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3.0) - 2.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.HIGH;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    fx0 = Math.pow(x0, 3.0) - 2.0;
    x1 = 1.0;
    fx1 = Math.pow(x1, 3.0) - 2.0;
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertFalse(result);

    // Test case 8: Function with roots at the interval edges and bisecting
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3.0) - 2.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.LOW;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    fx0 = Math.pow(x0, 3.0) - 2.0;
    x1 = 1.0;
    fx1 = Math.pow(x1, 3.0) - 2.0;
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertTrue(result);
    Assert.assertNotEquals(Double.NaN, solver.getRoot());

    // Test case 9: Function with roots at the interval edges and secant
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3.0) - 2.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        return ConvergenceChecker.HIGH;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    fx0 = Math.pow(x0, 3.0) - 2.0;
    x1 = 1.0;
    fx1 = Math.pow(x1, 3.0) - 2.0;
    result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    Assert.assertFalse(result);
  }

}