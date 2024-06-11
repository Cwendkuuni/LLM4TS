package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test constructor
   */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertFalse(solver.isRootFound());
  }

  /**
   * Test findRoot method with simple function and default settings
   */
  @Test
  public void testFindRootSimpleFunction() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with simple function and custom settings
   */
  @Test
  public void testFindRootSimpleFunctionCustomSettings() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;
    int maxIter = 5;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function having a root that is not found within the given interval
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionWithNoRootInInterval() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 9.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    fail("FindRoot method should have thrown an exception when the root is not in the given interval");
  }

  /**
   * Test findRoot method with a function having a root that is close to the boundary of the given interval
   */
  @Test
  public void testFindRootFunctionWithRootCloseToBoundary() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = -1.1;
    double x1 = 1.1;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function having a root that is very close to the boundary of the given interval
   */
  @Test
  public void testFindRootFunctionWithRootVeryCloseToBoundary() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1e-9;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function having multiple roots
   */
  @Test
  public void testFindRootFunctionWithMultipleRoots() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root1 = 2.0;
    double root2 = -2.0;

    boolean result1 = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result1);
    assertEquals(root1, solver.getRoot(), epsilon);

    // reset solver to initial state
    solver = new BrentSolver();

    boolean result2 = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result2);
    assertEquals(root2, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function having no real roots
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionWithNoRealRoots() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    fail("FindRoot method should have thrown an exception when the function has no real roots");
  }

  /**
   * Test findRoot method with a function having roots very close to each other
   */
  @Test
  public void testFindRootFunctionWithRootsVeryClose() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - (0.0001 * 0.0001);
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = 0.001;
    double root1 = 0.0;
    double root2 = 0.0001;

    boolean result1 = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result1);
    assertEquals(root1, solver.getRoot(), epsilon);

    // reset solver to initial state
    solver = new BrentSolver();

    boolean result2 = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(result2);
    assertEquals(root2, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function having roots that are very far from the given interval
   */
  @Test
  public void testFindRootFunctionWithRootsVeryFar() throws Exception {
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 100.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xp1, double fp1) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = -1.0;
    double x1 = 1.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertFalse(result);
  }

}