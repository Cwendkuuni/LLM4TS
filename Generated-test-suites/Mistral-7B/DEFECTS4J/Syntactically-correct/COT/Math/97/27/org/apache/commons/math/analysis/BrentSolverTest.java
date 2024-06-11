package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} .
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test with a simple function, x^2 - 1
    ComputableFunction function = (x) -> Math.pow(x, 2) - 1;
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;
    double root = solver.findRoot(function, checker, maxIter, x0, Math.pow(x0, 2) - 1, x1, Math.pow(x1, 2) - 1);

    assertEquals("The root should be between -1 and 1", -0.7127, root, 0.001);

    // Test with a function that has multiple roots
    function = (x) -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
    solver = new BrentSolver();

    x0 = 1.0;
    x1 = 2.0;
    maxIter = 100;
    root = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // The Brent method should find only one root
    assertEquals("The root should be between 1 and 2", 1.323, root, 0.001);

    // Test with a function that doesn't converge within the interval
    function = (x) -> Math.log(Math.abs(x - 2));
    solver = new BrentSolver();

    x0 = 0.0;
    x1 = 3.0;
    maxIter = 100;
    assertFalse("The function doesn't converge within the given interval", solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));

    // Test with a function that throws an exception
    function = (x) -> { throw new ArithmeticException("Divide by zero"); };
    solver = new BrentSolver();

    x0 = 0.0;
    x1 = 1.0;
    maxIter = 100;
    try {
      solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("An exception should have been thrown");
    } catch (FunctionException ex) {
      // expected
    }
  }

}