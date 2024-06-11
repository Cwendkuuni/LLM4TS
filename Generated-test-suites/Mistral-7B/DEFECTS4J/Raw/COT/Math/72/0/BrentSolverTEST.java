import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -3.0, -2.0, 2.0, 4.0);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_FunctionWithMultipleRoots() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -2.0, -1.5, 1.5, 2.0);
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 0.0001);

    solver.findRoot(function, checker, 100, -1.5, -1.0, 1.0, 2.0);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.0001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_DivergentFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Divide by zero");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 0.0, 1.0, 2.0, 3.0);
  }

  @Test
  public void testFindRoot_FunctionWithZeroDerivative() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - x * x;
      }

      @Override
      public double derivativeAt(double x) {
        return 3 * x * x - 2 * x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -1.5, -1.0, 1.0, 2.0);
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_FunctionWithNearZeroDerivative() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 0.001 * x * x;
      }

      @Override
      public double derivativeAt(double x) {
        return 3 * x * x - 0.002 * x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -1.5, -1.0, 1.0, 2.0);
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_FunctionWithSmallInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.0, Math.PI / 2, Math.PI / 2, Math.PI);
    assertTrue(result);
    assertEquals(Math.PI / 2, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_FunctionWithLargeInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -10.0, 10.0, 10.0, 20.0);
    assertFalse(result);
  }

  @Test
  public void testFindRoot_FunctionWithFunctionException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0.0) {
          throw new FunctionException("Function definition domain error");
        }
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, -2.0, -1.0, 1.0, 2.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRoot_FunctionWithZeroArgument() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x / 0.0);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, 0.0, 1.0, 2.0, 3.0);
      fail("Expected ArithmeticException");
    } catch (ArithmeticException e) {
      // expected
    }
  }

}