package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit test file for BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() {
    // Test case 1: Function f(x) = x^3 - 2
    SimpleFunction function = FunctionFactory.newSimpleFunction("x^3 - 2", 3.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .orElseThrow(() -> new AssertionError("Root not found"));
    assertEquals(2.0, root, 0.001);

    // Test case 2: Function f(x) = sin(x)
    function = FunctionFactory.newSimpleFunction("sin(x)", 1.0);
    x0 = 0.0;
    x1 = Math.PI;
    root = solver.findRoot(function, checker, 30, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .orElseThrow(() -> new AssertionError("Root not found"));
    assertEquals(1.273211828, root, 0.0001);

    // Test case 3: Function f(x) = x^2 - 4
    function = FunctionFactory.newSimpleFunction("x^2 - 4", 4.0);
    checker = new ConvergenceChecker.Default() {
      @Override
      public int convergenceCheck(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.INTERMEDIATE;
      }
    };
    solver = new BrentSolver();
    x0 = 2.0;
    x1 = 5.0;
    root = solver.findRoot(function, checker, 30, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .orElseThrow(() -> new AssertionError("Root not found"));
    assertEquals(2.0, root, 0.001);

    // Test case 4: Function f(x) = exp(x) - 2
    function = FunctionFactory.newSimpleFunction("exp(x) - 2", 0.0);
    solver = new BrentSolver();
    x0 = -1.0;
    x1 = 0.0;
    root = solver.findRoot(function, checker, 30, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .orElseThrow(() -> new AssertionError("Root not found"));
    assertEquals(-0.693147181, root, 0.001);

    // Test case 5: Function f(x) with root outside the input interval
    function = FunctionFactory.newSimpleFunction("x^2 - 4", 1.0);
    solver = new BrentSolver();
    x0 = 1.0;
    x1 = 2.0;
    assertFalse(solver.findRoot(function, checker, 30, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .isPresent());

    // Test case 6: Function f(x) with singularity inside the input interval
    function = FunctionFactory.newSimpleFunction("1 / x", 1.0);
    solver = new BrentSolver();
    x0 = 0.5;
    x1 = 1.0;
    assertFalse(solver.findRoot(function, checker, 30, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .isPresent());

  }
}