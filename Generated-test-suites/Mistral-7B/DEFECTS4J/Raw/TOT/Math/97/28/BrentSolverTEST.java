package org.spaceroots.mantissa.roots.test;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionTestUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.RootsFinder;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = FunctionTestUtils.simpleFunction(1.0);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        // A simple checker that returns ConvergenceChecker.LOW when x is close enough
        // to the root.
        return Math.abs(x - 1.0) < 0.001 ? ConvergenceChecker.LOW : ConvergenceChecker.UNDEFINED;
      }
    };
  }

  /**
   * Tests that {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * correctly finds a root for a simple function.
   */
  @Test
  public void testSimpleFunction() throws FunctionException {
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.001);
  }

  /**
   * Tests that {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * correctly handles the case where the root is not found in the given interval.
   */
  @Test
  public void testRootNotInInterval() throws FunctionException {
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = -2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertFalse(result);
  }

  /**
   * Tests that {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * correctly handles the case where the function is not computable at the end point of the interval.
   */
  @Test(expected = FunctionException.class)
  public void testFunctionNotComputableAtIntervalEnd() throws FunctionException {
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = Double.NaN;
    double f1 = Double.NaN;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

  /**
   * Tests that {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * correctly handles the case where the function has multiple roots in the given interval.
   */
  @Test
  public void testFunctionWithMultipleRoots() throws FunctionException {
    double x0 = -2.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertFalse(result);
  }

  /**
   * Tests that {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * correctly handles the case where the function is not monotonic in the given interval.
   */
  @Test
  public void testFunctionNotMonotonic() throws FunctionException {
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = -function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertFalse(result);
  }

  /**
   * Tests that {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * correctly handles the case where the function is not continuous at the end point of the interval.
   */
  @Test
  public void testFunctionNotContinuousAtIntervalEnd() throws FunctionException {
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1) + Math.pow(2.0, 31);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertFalse(result);
  }
}