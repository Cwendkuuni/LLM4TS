import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.ConvergenceChecker;

/**
 * JUnit test file for the BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test for the constructor
   */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    Assert.assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  /**
   * Test for findRoot method with simple function and default settings
   */
  @Test
  public void testFindRootSimpleFunction() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };
    int maxIter = 10;
    double x0 = -2.0;
    double x1 = 2.0;
    double root = 1.0; // expected root

    try {
      boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.assertTrue(result);
      Assert.assertEquals(root, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test for findRoot method with simple function and convergence checker
   */
  @Test
  public void testFindRootSimpleFunctionWithConvergenceChecker() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(Math.sin(x), 2.0) - 1.0;
      }
    };
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        if (Math.abs(fx1) < 0.001 && Math.abs(fx2) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.UNKNOWN;
      }
    };
    int maxIter = 100;
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.57079632679; // expected root

    try {
      boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.assertTrue(result);
      Assert.assertEquals(root, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test for findRoot method with simple function and maximum number of iterations
   */
  @Test
  public void testFindRootSimpleFunctionMaxIter() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 3.0;
      }
    };
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };
    int maxIter = 2;
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.332057; // expected root

    try {
      boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.assertFalse(result);
      Assert.assertEquals(Double.NaN, solver.getRoot(), 0.0);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    solver = new BrentSolver();
    maxIter = 1000;
    root = 1.0; // expected root

    try {
      boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.assertTrue(result);
      Assert.assertEquals(root, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test for findRoot method with function throwing exception
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x > 0) {
          throw new FunctionException("Error!");
        }
        return Math.sin(x) / x;
      }
    };
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };
    int maxIter = 10;
    double x0 = -2.0;
    double x1 = 0.0;

    try {
      solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test for findRoot method with function throwing exception at root
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionExceptionAtRoot() {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x == 0) {
          throw new FunctionException("Error!");
        }
        return Math.sin(x) / x;
      }
    };
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };
    int maxIter = 10;
    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0; // expected root

    try {
      solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

}