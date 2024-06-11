package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.CubicFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Tests for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test for the root finding capabilities of {@link BrentSolver}.
   */
  @Test
  public void testFindRoot() throws Exception {
    ComputableFunction function = new CubicFunction(1.0, 1.0, -1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker.LinearFunction();
    BrentSolver solver = new BrentSolver();
    double[] roots = {-1.0, 1.0, 1.0};

    // Test a simple root
    double x0 = 0.0;
    double x1 = 1.0;
    double[] result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result[0] > -epsilon);
    assertTrue(result[0] < epsilon + roots[0]);

    // Test a multiple root
    x0 = -1.0;
    x1 = 1.0;
    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result[0] > roots[1] - epsilon);
    assertTrue(result[0] < roots[1] + epsilon);

    // Test a root between the interval bounds
    x0 = 0.5;
    x1 = 1.5;
    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result[0] > roots[2] - epsilon);
    assertTrue(result[0] < roots[2] + epsilon);

    // Test a root outside the interval bounds
    x0 = 0.0;
    x1 = -1.0;
    assertFalse(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) != null);

    // Test a non-converging function
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return Math.sin(x) / x;
      }
    };
    assertFalse(solver.findRoot(function, checker, 10, 0.0, function.valueAt(0.0), 1.0, function.valueAt(1.0)) != null);

    // Test function with a "cusp" at the root
    double eps = Math.pow(2.0, -20);
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
      }
    };
    x0 = -1.5;
    x1 = 1.5;
    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result[0] > roots[0] - eps);
    assertTrue(result[0] < roots[0] + eps);
  }

}