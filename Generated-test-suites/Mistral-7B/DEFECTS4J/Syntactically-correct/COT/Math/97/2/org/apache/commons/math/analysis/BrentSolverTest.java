package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test with a simple function
    ComputableFunction function = FunctionFactory.createFunction(Math::pow, 1.0);
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 1.0;
    double x1 = 2.0;
    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, Math.pow(x0, 2.0), x1, Math.pow(x1, 2.0)));
    Assert.assertEquals(Math.sqrt(2.0), solver.getRoot(), 0.01);

    // Test with a function with multiple roots
    function = FunctionFactory.createFunction(x -> x * (x - 1.0) * (x + 1.0), 0.0);
    solver = new BrentSolver();
    checker = new ConvergenceChecker.Default();
    x0 = -3.0;
    x1 = 3.0;
    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(-1.0, solver.getRoot(), 0.01);
    Assert.assertTrue(solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0)));
    Assert.assertEquals(1.0, solver.getRoot(), 0.01);

    // Test with a function with no root in the interval
    function = FunctionFactory.createFunction(x -> x * x - 2.0, 0.0);
    solver = new BrentSolver();
    checker = new ConvergenceChecker.Default();
    x0 = -2.0;
    x1 = 2.0;
    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));

    // Test exception handling
    function = (ComputableFunction) () -> { throw new RuntimeException("Function exception"); };
    solver = new BrentSolver();
    checker = new ConvergenceChecker.Default();
    x0 = 0.0;
    x1 = 1.0;
    Assert.assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 100, x0, 0.0, x1, 1.0));
  }

  /**
   * Test method for {@link BrentSolver#getRoot()}
   */
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    Assert.assertTrue(Double.isNaN(solver.getRoot()));
    solver.findRoot(FunctionFactory.createFunction(x -> x * x - 2.0, 0.0), new ConvergenceChecker.Default(), 100, -2.0, -4.0, 2.0, 4.0);
    Assert.assertEquals(-1.0, solver.getRoot(), 0.01);
  }

}