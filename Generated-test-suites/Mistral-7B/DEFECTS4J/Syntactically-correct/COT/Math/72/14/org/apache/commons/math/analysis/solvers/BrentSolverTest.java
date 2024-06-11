import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xOld, double fOld) {
        return x > xOld ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    double x0 = 1.0;
    double f0 = Math.pow(1.0, 2) - 4;
    double x1 = 3.0;
    double f1 = Math.pow(3.0, 2) - 4;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(-2.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_dividedByZero() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 1.0 / (x - 2.0);
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xOld, double fOld) {
        return x > xOld ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    double x0 = 1.0;
    double f0 = 1.0 / (x0 - 2.0);
    double x1 = 3.0;
    double f1 = 1.0 / (x1 - 2.0);

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

  @Test
  public void testFindRoot_convergence() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xOld, double fOld) {
        return Math.abs(x - xOld) < 0.001 ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double x0 = 0.0;
    double f0 = Math.pow(x0, 3) - 2;
    double x1 = 1.0;
    double f1 = Math.pow(x1, 3) - 2;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(-0.5877852519532742, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_noRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 1.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xOld, double fOld) {
        return Math.abs(f) < 0.001 ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double x0 = 0.0;
    double f0 = Math.sin(x0) - 1.0;
    double x1 = 4.0;
    double f1 = Math.sin(x1) - 1.0;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertEquals(Double.NaN, solver.getRoot());
  }

}