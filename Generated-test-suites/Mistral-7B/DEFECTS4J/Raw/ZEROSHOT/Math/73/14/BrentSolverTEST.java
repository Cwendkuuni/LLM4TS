import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.SquareFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {
    // Test with a simple function: f(x) = x^2
    SquareFunction function = new SquareFunction();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return (Math.abs(fx) < 1e-10) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, solver.getRoot(), 1e-10);

    // Test with a function with a root close to the interval boundary
    function = new SquareFunction();
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return (Math.abs(fx) < 1e-10) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };

    x0 = 2.0;
    x1 = 3.0;
    root = 2.5;

    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, solver.getRoot(), 1e-10);

    // Test with a function having no root in the interval
    function = new FunctionUtils.CubicFunction(-1.0, 1.0, 2.0, 3.0);

    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    x0 = 0.0;
    x1 = 4.0;

    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertFalse(result);
  }

}