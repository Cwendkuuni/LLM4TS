import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simple() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return (Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -2.0, -1.5, 2.0, 3.0);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Test Exception");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 0.0, 0.0, 0.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_illegalArgument() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2);
      }
    };

    ConvergenceChecker checker = null;
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -2.0, -1.5, 2.0, 3.0);
  }

  @Test
  public void testFindRoot_notConverged() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, 0.0, 0.0, 0.0, 0.0);
    assertFalse(result);
  }

}