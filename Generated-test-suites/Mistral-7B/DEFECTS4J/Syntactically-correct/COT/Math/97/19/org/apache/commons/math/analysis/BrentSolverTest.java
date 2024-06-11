import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleCase() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = 3.0;
    double x1 = 3.0;
    double f1 = 1.0;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertEquals("Root not found", -2.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function error");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, 0.0, 0.0, 0.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_IllegalArgumentException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
  }

  @Test
  public void testFindRoot_MaxIter() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI * x) / x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = -1.0;

    assertFalse("Root found in less than maxIter iterations", solver.findRoot(function, checker, 1, x0, f0, x1, f1));
  }
}