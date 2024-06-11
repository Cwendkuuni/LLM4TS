package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test method for the findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double) method.
   * @throws FunctionException
   */
  @Test
  public void testFindRoot() throws FunctionException {

    // Test with a simple function
    ComputableFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver brentSolver = new BrentSolver();
    double root = brentSolver.findRoot(function, checker, 100, 1.0, 2.0, 1.5, 3.0)[0];
    assertEquals("Root not found", -1.0, root, epsilon);

    // Test with a function with multiple roots
    function = new RationalFunction("x^3 - 3 * x^2 + 2 * x");
    brentSolver = new BrentSolver();
    root = brentSolver.findRoot(function, checker, 100, 0.5, 1.0, 1.5, 2.0)[0];
    assertEquals("Root not found or incorrect", -1.0, root, epsilon);

    root = brentSolver.findRoot(function, checker, 100, 1.5, 1.0, 2.5, 3.0)[0];
    assertEquals("Root not found or incorrect", 1.0, root, epsilon);

    // Test with a function with no root in the interval
    function = new RationalFunction("x^2 + 1");
    brentSolver = new BrentSolver();
    assertFalse("Root found in an interval with no root", brentSolver.findRoot(function, checker, 100, 0.0, 1.0, 2.0, 3.0)[0]);

  }

  /**
   * Test method for the getRoot() method.
   */
  @Test
  public void testGetRoot() {
    BrentSolver brentSolver = new BrentSolver();
    ComputableFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double[] roots = brentSolver.findRoot(function, checker, 100, 1.0, 2.0, 1.5, 3.0);
    brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 100, roots[0], function.valueAt(roots[0]), roots[1], function.valueAt(roots[1]));
    assertEquals("Root not found or incorrect", roots[0], brentSolver.getRoot(), epsilon);
  }

}