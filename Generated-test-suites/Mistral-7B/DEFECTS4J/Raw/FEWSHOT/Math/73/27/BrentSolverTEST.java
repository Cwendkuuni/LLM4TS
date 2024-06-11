package org.apache.commons.cli.mantissa.roots; // adjusted package name for the test class

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit 4 test cases for the BrentSolver class.
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test with a simple function
    ComputableFunction function = new MockFunction(0.0, 2.0);
    ConvergenceChecker checker = new ConvergenceChecker.AbsDiff(1e-6);
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 2.0;
    try {
      boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertTrue(found);
      double root = solver.getRoot();
      assertEquals(1.0, root, 1e-6);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    // Test with a function having multiple roots
    function = new MockFunction(-1.0, 1.0, 3.0, 5.0);
    checker = new ConvergenceChecker.AbsDiff(1e-6);
    solver = new BrentSolver();
    x0 = -2.0;
    x1 = 6.0;
    try {
      boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertTrue(found);
      double root = solver.getRoot();
      // One of the roots is expected to be returned
      double expectedRoot = 1.0; // You may want to test other roots if needed
      assertEquals(expectedRoot, root, 1e-6);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    // Test with a function having no root in the interval
    function = new MockFunction(1.0, 5.0);
    checker = new ConvergenceChecker.AbsDiff(1e-6);
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 6.0;
    try {
      boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertFalse(found);
    } catch (FunctionException e) {
      // Expected exception
    }
  }
}