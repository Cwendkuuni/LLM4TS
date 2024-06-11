package org.spaceroots.mantissa.roots;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.SquareFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test the root finding for a simple function.
   */
  @Test
  public void testRootFinding() throws Exception {
    // setup
    BrentSolver solver = new BrentSolver();
    SquareFunction function = new SquareFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();

    // test case 1: root is in the interval [1, 2]
    double x0 = 1.0;
    double x1 = 2.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    double root = solver.getRoot();
    Assert.assertEquals(1.5, root, 0.001);

    // test case 2: root is very close to the lower boundary
    x0 = 1.0 - 0.01;
    x1 = 1.0 + 0.01;
    result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    root = solver.getRoot();
    Assert.assertEquals(1.0, root, 0.001);

    // test case 3: root is very close to the upper boundary
    x0 = 1.99;
    x1 = 2.01;
    result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    root = solver.getRoot();
    Assert.assertEquals(2.0, root, 0.001);

    // test case 4: root is between the interval (function values have opposite signs)
    x0 = 0.5;
    x1 = 2.0;
    result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    root = solver.getRoot();
    Assert.assertEquals(1.0, root, 0.001);

    // test case 5: function has no roots
    function = new SquareFunctionNegative();
    result = solver.findRoot(function, checker, 100, -2.0, function.valueAt(-2.0), 2.0, function.valueAt(2.0));
    Assert.assertFalse(result);

    // test case 6: root is at a boundary (a=b)
    x0 = 2.0;
    x1 = 2.0;
    result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    root = solver.getRoot();
    Assert.assertEquals(2.0, root, 0.001);
  }

  /**
   * Test the convergence checker.
   */
  @Test
  public void testConvergenceChecker() {
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    Assert.assertEquals(ConvergenceChecker.LOW, checker.converged(1.0, 1.0, 1.1, 0.5));
    Assert.assertEquals(ConvergenceChecker.HIGH, checker.converged(1.0, 1.0, 1.1, 1.5));
    Assert.assertEquals(ConvergenceChecker.NEITHER, checker.converged(1.0, 1.0, 1.1, 1.0));
  }

  /**
   * Test the function used for the test cases.
   */
  static class SquareFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return FunctionUtils.square(x);
    }

  }

  /**
   * Test function with no roots.
   */
  static class SquareFunctionNegative implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return FunctionUtils.square(x) * -1.0;
    }

  }

}