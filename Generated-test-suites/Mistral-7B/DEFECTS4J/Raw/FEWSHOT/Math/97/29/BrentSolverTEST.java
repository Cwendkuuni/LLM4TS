package org.apache.commons.cli.test; // Change the package name if necessary

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  private ComputableFunction function;
  private ConvergenceChecker checker;
  private BrentSolver solver;

  @Before
  public void setup() {
    function = mock(ComputableFunction.class);
    checker = mock(ConvergenceChecker.class);
    solver = new BrentSolver();
  }

  /**
   * Test a simple case with a known root.
   */
  @Test
  public void testFindRootSimpleCase() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0));
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  /**
   * Test a case where the root is outside the given interval.
   */
  @Test
  public void testFindRootRootOutsideInterval() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.0);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0));
  }

  /**
   * Test a case where the function has no root in the given interval.
   */
  @Test
  public void testFindRootNoRootInInterval() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0));
  }

  /**
   * Test a convergence case with a simple convergence checker.
   */
  @Test
  public void testFindRootSimpleConvergenceChecker() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.5);
    when(function.valueAt(2.0)).thenReturn(0.0);

    ConvergenceCheckerSimple convergenceChecker = new ConvergenceCheckerSimple();
    assertTrue(solver.findRoot(function, convergenceChecker, 10, 0.0, 1.0, 1.0, 1.0));
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  /**
   * Test a convergence case with a custom convergence checker.
   */
  @Test
  public void testFindRootCustomConvergenceChecker() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(function.valueAt(2.0)).thenReturn(0.0);

    ConvergenceCheckerCustom convergenceChecker = new ConvergenceCheckerCustom();
    assertTrue(solver.findRoot(function, convergenceChecker, 10, 0.0, 1.0, 1.0, 1.0));
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  /**
   * A simple convergence checker.
   */
  static class ConvergenceCheckerSimple implements ConvergenceChecker {

    @Override
    public int converged(double x, double fx, double xPrev, double fxPrev) {
      if (Math.abs(fx) < Double.MIN_VALUE) {
        return ConvergenceChecker.LOW;
      }
      return ConvergenceChecker.NO_CONVERGENCE;
    }
  }

  /**
   * A custom convergence checker.
   */
  static class ConvergenceCheckerCustom implements ConvergenceChecker {

    @Override
    public int converged(double x, double fx, double xPrev, double fxPrev) {
      if (Math.abs(fx) < epsilon && Math.abs(fx - fxPrev) < epsilon) {
        return ConvergenceChecker.LOW;
      }
      return ConvergenceChecker.NO_CONVERGENCE;
    }
  }
}