import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 100, -2.0, 3.0));
    assertEquals(-1.2, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithComplexFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI / x) * Math.exp(-x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, 1.0, 2.0);
      fail("FunctionException expected");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRootWithFunctionOutsideBounds() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, -10.0, 10.0);
      fail("FunctionException expected");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRootWithMaxIterReached() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.cos(x) - x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 1, 0.0, 1.0);
      fail("FunctionException expected");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sqrt(x) - 2;
      }
    }, new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.LOW;
      }
    }, 100, 4.0, 8.0);
    assertEquals(3.0, solver.getRoot(), 0.01);
  }

}