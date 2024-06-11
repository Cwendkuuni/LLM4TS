package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test method for the findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double) method.
   */
  @Test
  public void testFindRoot() {
    // Test with a simple function
    RationalFunction function = new RationalFunction("1.0", "x", "1.0", "x", "2.0");
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();
    BrentSolver solver = new BrentSolver();
    double root = solver.findRoot(function, checker, 100, -1.0, -1.0, 1.0, 1.0) ? solver.getRoot() : Double.NaN;
    Assert.assertEquals(0.0, root, epsilon);

    // Test with a function with multiple roots
    function = new RationalFunction("1.0", "x", "1.0", "x", "x");
    checker = new ConvergenceChecker.SimpleChecker();
    solver = new BrentSolver();
    root = solver.findRoot(function, checker, 100, -2.0, 2.0, 0.0, 0.0) ? solver.getRoot() : Double.NaN;
    Assert.assertEquals(-1.0, root);
    root = solver.findRoot(function, checker, 100, 0.0, 2.0, 1.0, 0.0) ? solver.getRoot() : Double.NaN;
    Assert.assertEquals(1.0, root);

    // Test with a function with no roots
    function = new RationalFunction("1.0", "x", "1.0", "x", "x + 1.0");
    checker = new ConvergenceChecker.SimpleChecker();
    solver = new BrentSolver();
    Assert.assertFalse(solver.findRoot(function, checker, 100, -1.0, 1.0, 0.0, 0.0));

    // Test with a function with negative roots
    function = new RationalFunction("1.0", "x", "1.0", "x", "x * x - 1.0");
    checker = new ConvergenceChecker.SimpleChecker();
    solver = new BrentSolver();
    root = solver.findRoot(function, checker, 100, -2.0, 2.0, 0.0, 0.0) ? solver.getRoot() : Double.NaN;
    Assert.assertEquals(-1.0, root);
    root = solver.findRoot(function, checker, 100, 0.0, 2.0, 1.0, 0.0) ? solver.getRoot() : Double.NaN;
    Assert.assertEquals(-1.0, root);

    // Test with a function with very close roots
    function = new RationalFunction("1.0", "x", "1.0", "x", "x * x - 0.001");
    checker = new ConvergenceChecker.SimpleChecker();
    solver = new BrentSolver();
    root = solver.findRoot(function, checker, 100, -2.0, 2.0, 0.0, 0.0) ? solver.getRoot() : Double.NaN;
    Assert.assertEquals(-1.0, root);
    root = solver.findRoot(function, checker, 100, 0.0, 2.0, 1.0, 0.0) ? solver.getRoot() : Double.NaN;
    Assert.assertEquals(-1.0, root);

  }

}