import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(f) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 2) - 4.0;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 2) - 4.0;

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Test exception");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = 0.0;
    double x1 = 2.0;
    double f1 = 0.0;

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);
  }

  @Test
  public void testFindRootConvergence() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(f) < 0.00001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 2) - 4.0;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 2) - 4.0;

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, solver.getRoot());
  }

  @Test
  public void testFindRootMaxIter() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = Math.sin(x0);
    double x1 = Math.PI;
    double f1 = Math.sin(x1);

    assertFalse(solver.findRoot(function, checker, 0, x0, f0, x1, f1));
  }
}