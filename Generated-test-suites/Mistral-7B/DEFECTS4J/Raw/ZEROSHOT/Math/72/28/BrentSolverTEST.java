import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a simple function and default checker
   */
  @Test
  public void testFindRootSimpleFunction() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = 1.0;
    double b = 3.0;
    double root = 2.0;
    boolean expectedResult = true;

    try {
      Assert.assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(root, solver.getRoot(), 1e-8);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a simple function and custom checker
   */
  @Test
  public void testFindRootSimpleFunctionCustomChecker() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        if (Math.abs(fx) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx1) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.UNDEFINED;
        }
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = 1.0;
    double b = 3.0;
    double root = 2.0;
    boolean expectedResult = true;

    try {
      Assert.assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(root, solver.getRoot(), 1e-8);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has no root in the given interval
   */
  @Test
  public void testFindRootNoRoot() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = -1.0;
    double b = 1.0;
    boolean expectedResult = false;

    try {
      Assert.assertFalse(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(Double.NaN, solver.getRoot(), 0.0);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has multiple roots in the given interval
   */
  @Test
  public void testFindRootMultipleRoots() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = -1.0;
    double b = 3.0;
    double root = 0.0; // there are two roots, but this test only checks if one is found
    boolean expectedResult = true;

    try {
      Assert.assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(root, solver.getRoot(), 1e-8);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has a root at a boundary of the given interval
   */
  @Test
  public void testFindRootRootAtBoundary() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = 0.0;
    double b = 1.0;
    double root = 0.0;
    boolean expectedResult = true;

    try {
      Assert.assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(root, solver.getRoot(), 1e-8);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has a root very close to a boundary of the given interval
   */
  @Test
  public void testFindRootRootCloseToBoundary() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = 0.0;
    double b = 1.0;
    double root = 0.1;
    boolean expectedResult = true;

    try {
      Assert.assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(root, solver.getRoot(), 1e-8);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has a root very close to the middle of the given interval
   */
  @Test
  public void testFindRootRootMiddle() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = 0.0;
    double b = 2.0;
    double root = 1.0;
    boolean expectedResult = true;

    try {
      Assert.assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(root, solver.getRoot(), 1e-8);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has a root very deep in the given interval
   */
  @Test
  public void testFindRootRootDeep() {
    SimpleFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double a = 0.0;
    double b = 10.0;
    double root = 3.0;
    boolean expectedResult = true;

    try {
      Assert.assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      Assert.assertEquals(root, solver.getRoot(), 1e-8);
    } catch (FunctionException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
}