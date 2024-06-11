package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case 1: find root of function f(x) = x^2 - 4
    ComputableFunction function = FunctionFactory.create(x -> x * x - 4.0);
    ConvergenceChecker checker = new ConvergenceChecker.Difference();
    BrentSolver solver = new BrentSolver();

    double x0 = -3.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", true, result);
    assertEquals("Root is not correct", root, solver.getRoot(), 0.001);

    // Test case 2: find root of function f(x) = sin(x)
    function = FunctionFactory.create(Math::sin);
    checker = new ConvergenceChecker.Difference();
    solver = new BrentSolver();

    x0 = 1.0;
    x1 = 3.0;
    root = Math.PI / 2.0;

    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", true, result);
    assertEquals("Root is not correct", root, solver.getRoot(), Math.PI / 180.0);

    // Test case 3: edge case with x0 = x1
    function = FunctionFactory.create(x -> x * x - 4.0);
    checker = new ConvergenceChecker.Difference();
    solver = new BrentSolver();

    x0 = 2.0;
    x1 = 2.0;
    root = 2.0;

    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }

    // Test case 4: edge case with x0 = root
    function = FunctionFactory.create(x -> x * x - 4.0);
    checker = new ConvergenceChecker.Difference();
    solver = new BrentSolver();

    x0 = root;
    x1 = 3.0;
    root = 2.0;

    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", true, result);
    assertEquals("Root is not correct", root, solver.getRoot(), 0.001);

    // Test case 5: edge case with x1 = root
    function = FunctionFactory.create(x -> x * x - 4.0);
    checker = new ConvergenceChecker.Difference();
    solver = new BrentSolver();

    x0 = -3.0;
    x1 = root;
    root = 2.0;

    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("Root not found", true, result);
    assertEquals("Root is not correct", root, solver.getRoot(), 0.001);

    // Test case 6: edge case with maxIter = 0
    function = FunctionFactory.create(x -> x * x - 4.0);
    checker = new ConvergenceChecker.Difference();
    solver = new BrentSolver();

    x0 = -3.0;
    x1 = 3.0;
    root = 2.0;

    try {
      solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }

    // Test case 7: exception handling with FunctionException
    function = new FunctionException("Test function with exception");
    checker = new ConvergenceChecker.Difference();
    solver = new BrentSolver();

    x0 = 0.0;
    x1 = 0.0;
    root = 0.0;

    try {
      solver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

}