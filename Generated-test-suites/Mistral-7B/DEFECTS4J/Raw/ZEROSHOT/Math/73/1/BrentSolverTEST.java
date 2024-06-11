import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {

    // Test function f(x) = x^3 - 2
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 0.00001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 0.00001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, Math.pow(x0, 3), x1, Math.pow(x1, 3));

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertTrue(Math.abs(solver.getRoot() - 1.0) < 0.001);

    // Test function f(x) = sin(x) - x
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 0.01) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 0.5;
    solver.findRoot(function, checker, 100, x0, Math.sin(x0) - x0, x1, Math.sin(x1) - x1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertTrue(Math.abs(solver.getRoot() - 0.392699) < 0.01);

    // Test function f(x) = exp(x) - x - 1
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(x) - x - 1;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    solver = new BrentSolver();
    x0 = -1.0;
    x1 = 0.0;
    solver.findRoot(function, checker, 100, x0, Math.exp(x0) - x0 - 1, x1, Math.exp(x1) - x1 - 1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertTrue(Math.abs(solver.getRoot() - 0.542969) < 0.001);

  }

}