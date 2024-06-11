package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.ScalarFunction;

/**
 * JUnit test cases for the BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#BrentSolver()}.
   */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNaN(solver.getRoot());
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test simple case: f(x) = x^2 - 4
    ScalarFunction function = FunctionUtils.squareFunction("-4");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;
    boolean result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 1e-6);
    assertTrue(result);

    // Test failure case: f(x) = 1/x
    function = FunctionUtils.reciprocalFunction();
    checker = new ConvergenceChecker.Linear();
    solver = new BrentSolver();
    x0 = 1.0;
    x1 = 2.0;
    root = 1.5;
    try {
      result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // pass
    }
  }

  /**
   * Test edge cases for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRootEdgeCases() throws Exception {
    // Test case with x0 = x1
    ScalarFunction function = FunctionUtils.squareFunction("-1");
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 1.0;
    double root = 0.0;
    try {
      solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // pass
    }

    // Test case with x0 = root
    function = FunctionUtils.squareFunction("-4");
    checker = new ConvergenceChecker.Quadratic();
    solver = new BrentSolver();
    x0 = root;
    x1 = root + 0.1;
    root = 2.0;
    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 1e-6);
    assertTrue(result);

    // Test case with x1 = root
    function = FunctionUtils.squareFunction("-4");
    checker = new ConvergenceChecker.Quadratic();
    solver = new BrentSolver();
    x0 = root - 0.1;
    x1 = root;
    root = 2.0;
    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 1e-6);
    assertTrue(result);

    // Test case with x0 < root < x1
    function = FunctionUtils.squareFunction("-1");
    checker = new ConvergenceChecker.Quadratic();
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 4.0;
    root = 2.0;
    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 1e-6);
    assertTrue(result);

    // Test case with root = x0
    function = FunctionUtils.squareFunction("0");
    checker = new ConvergenceChecker.Quadratic();
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 0.1;
    root = 0.0;
    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(root, solver.getRoot(), 1e-6);

    // Test case with root = x1
    function = FunctionUtils.squareFunction("0");
    checker = new ConvergenceChecker.Quadratic();
    solver = new BrentSolver();
    x0 = 0.1;
    x1 = 0.0;
    root = 0.0;
    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(root, solver.getRoot(), 1e-6);

    // Test case with root between x0 and x1
    function = FunctionUtils.squareFunction("-1");
    checker = new ConvergenceChecker.Quadratic();
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 4.0;
    root = 2.0;
    result = solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 1e-6);
    assertTrue(result);
  }

  /**
   * Test exception handling for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRootExceptions() {
    // Test NullPointerException
    ScalarFunction function = null;
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;
    try {
      solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // pass
    }

    // Test FunctionException
    function = FunctionUtils.reciprocalFunction();
    checker = new ConvergenceChecker.Linear();
    solver = new BrentSolver();
    x0 = 1.0;
    x1 = 2.0;
    root = 1.5;
    try {
      solver.findRoot(function, checker, 50, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // pass
    }

    // Test ArrayIndexOutOfBoundsException
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        int result = 0;
        if (Double.isNaN(fx)) {
          result = ConvergenceChecker.LOW;
        } else if (Double.isNaN(fx2)) {
          result = ConvergenceChecker.HIGH;
        } else if (fx > 0) {
          result = ConvergenceChecker.LOW;
        } else if (fx2 < 0) {
          result = ConvergenceChecker.HIGH;
        } else {
          result = ConvergenceChecker.UNKNOWN;
        }
        return result;
      }

      @Override
      public int[] getConvergedValues() {
        throw new ArrayIndexOutOfBoundsException("getConvergedValues not implemented");
      }
    };
    ScalarFunction function1 = FunctionUtils.squareFunction("-1");
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 4.0;
    root = 2.0;
    try {
      solver.findRoot(function1, checker1, 50, x0, function1.valueAt(x0), x1, function1.valueAt(x1));
      fail("Expected ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException e) {
      // pass
    }

    // Test IllegalArgumentException
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        int result = 0;
        if (Double.isNaN(fx) || Double.isNaN(fx2)) {
          result = ConvergenceChecker.UNKNOWN;
        } else if (fx > 0) {
          result = ConvergenceChecker.LOW;
        } else if (fx2 < 0) {
          result = ConvergenceChecker.HIGH;
        } else {
          result = ConvergenceChecker.CONVERGED;
        }
        return result;
      }

      @Override
      public int[] getConvergedValues() {
        return new int[0];
      }
    };
    ScalarFunction function2 = FunctionUtils.squareFunction("-1");
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 4.0;
    root = 2.0;
    try {
      solver.findRoot(function2, checker2, 50, x0, function2.valueAt(x0), x1, function2.valueAt(x1));
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }

  }

  /**
   * Helper method to check the equality of two doubles up to a given tolerance.
   * @param d1 first double value
   * @param d2 second double value
   * @param tol tolerance
   */
  private void assertEquals(double d1, double d2, double tol) {
    double diff = Math.abs(d1 - d2);
    assertTrue("Expected: " + d1 + ", but was: " + d2 + ", tolerance: " + tol, diff <= tol);
  }

  /**
   * Helper method to check that a NaN value is returned.
   * @param d double value
   */
  private void assertNaN(double d) {
    assertFalse("Expected NaN, but was: " + d, Double.isFinite(d));
  }

}