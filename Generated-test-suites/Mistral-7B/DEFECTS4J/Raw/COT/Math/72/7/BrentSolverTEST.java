import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.PolynomialFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleCase() {
    ComputableFunction function = new PolynomialFunction(new double[]{-1, 1, 1});
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    double root = solver.getRoot();
    assertEquals("Root not found", -0.7127, root, 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    ComputableFunction function = () -> { throw new FunctionException("Test exception"); };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Double.NaN;
    double x1 = 0.0;
    double f1 = 0.0;

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException2() {
    ComputableFunction function = () -> { throw new FunctionException("Test exception", -1); };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Double.NaN;
    double x1 = 0.0;
    double f1 = 0.0;

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);
  }

  @Test
  public void testFindRoot_ConvergenceChecker_LOW() {
    ComputableFunction function = new PolynomialFunction(new double[]{-1, 1, 1});
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    double root = solver.getRoot();
    assertEquals("Root not found", -0.7127, root, 0.001);
  }

  @Test
  public void testFindRoot_ConvergenceChecker_HIGH() {
    ComputableFunction function = new PolynomialFunction(new double[]{-1, 1, 1});
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.HIGH;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    double root = solver.getRoot();
    assertEquals("Root not found", 0.7127, root, 0.001);
  }

  @Test
  public void testFindRoot_ConvergenceChecker_None() {
    ComputableFunction function = new PolynomialFunction(new double[]{-1, 1, 1});
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    double root = solver.getRoot();
    assertEquals("Root not found", -0.7127, root, 0.001);
  }

  @Test
  public void testFindRoot_MaxIter() {
    ComputableFunction function = new PolynomialFunction(new double[]{-1, 1, 1});
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, 0, x0, f0, x1, f1);

    assertFalse("Root not found", solver.getRoot() != Double.NaN);
  }

}