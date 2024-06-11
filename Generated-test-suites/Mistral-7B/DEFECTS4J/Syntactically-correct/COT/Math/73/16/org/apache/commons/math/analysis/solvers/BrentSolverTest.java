import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_basic() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new DummyFunction();

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    try {
      boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
      assertTrue(result);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testFindRoot_convergence() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    ComputableFunction function = new DummyFunction();

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    try {
      boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
      assertEquals(ConvergenceChecker.LOW, checker.converged(x0, f0, x1, f1));
      assertTrue(result);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testFindRoot_maxIter() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new DummyFunction();

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    try {
      boolean result = solver.findRoot(function, checker, 0, x0, f0, x1, f1);
      assertFalse(result);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new DummyFunctionWithException();

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    try {
      solver.findRoot(function, checker, 10, x0, f0, x1, f1);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new DummyFunction(), new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    }, 10, 1.0, 2.0, 1.0, 1.0);

    double root = solver.getRoot();
    assertEquals(1.0, root, 1e-6);
  }

}

class DummyFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return x * x - 1.0;
  }
}

class DummyFunctionWithException implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    if (x > 0.0) {
      throw new FunctionException("function exception");
    }
    return x * x - 1.0;
  }
}