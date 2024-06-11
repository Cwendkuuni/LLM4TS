package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test finding the root of a simple function.
   */
  @Test
  public void testSimpleFunction() {
    ComputableFunction function = FunctionFactory.createFunction(x -> x * x - 2);
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .getRoot();
    Assert.assertEquals("Root not found", -1.0, root, 0.001);
  }

  /**
   * Test finding the root of a function with multiple roots.
   */
  @Test
  public void testFunctionWithMultipleRoots() {
    ComputableFunction function = FunctionFactory.createFunction(x -> Math.sin(x) - 0.5);
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .getRoot();
    Assert.assertEquals("Root not found", 0.5235987755982989, root, 0.01);
  }

  /**
   * Test finding the root of a function with a large interval.
   */
  @Test
  public void testFunctionWithLargeInterval() {
    ComputableFunction function = FunctionFactory.createFunction(x -> Math.exp(x) - 2);
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    double x0 = -5.0;
    double x1 = 5.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .getRoot();
    Assert.assertEquals("Root not found", Math.log(2), root, 0.01);
  }

  /**
   * Test finding the root of a function with invalid input parameters.
   */
  @Test(expected = FunctionException.class)
  public void testFunctionWithInvalidInput() {
    ComputableFunction function = null;
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  /**
   * Test finding the root of a function with a non-ComputableFunction.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFunctionNotComputable() {
    Object nonComputableFunction = new Object();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot((ComputableFunction) nonComputableFunction, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  /**
   * Test finding the root of a function with a checker that does not implement ConvergenceChecker.
   */
  @Test(expected = ClassCastException.class)
  public void testInvalidChecker() {
    ComputableFunction function = FunctionFactory.createFunction(x -> x * x - 2);
    Object invalidChecker = new Object();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, (ConvergenceChecker) invalidChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}