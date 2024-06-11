package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.AbstractFunction;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a simple function and a simple checker.
   */
  @Test
  public void testFindRootWithSimpleFunctionAndSimpleChecker() {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.NONE);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertTrue(Math.abs(solver.getRoot() - 0.5) < 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a simple function and a checker that do not converge.
   */
  @Test
  public void testFindRootWithSimpleFunctionAndNonConvergingChecker() {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.NONE);
    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.NONE);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has multiple roots and a simple checker.
   */
  @Test
  public void testFindRootWithFunctionMultipleRoots() {
    ComputableFunction function = new AbstractFunction("function") {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) - Math.pow(x - 2.0, 2.0);
      }
    };

    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, -1.0, 5.0, 1.0, 1.0));
    assertTrue(Math.abs(solver.getRoot() - 0.0) < 0.001);
    assertTrue(solver.findRoot(function, checker, 10, 1.0, 3.0, 1.0, 1.0));
    assertTrue(Math.abs(solver.getRoot() - 2.0) < 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has no roots and a simple checker.
   */
  @Test
  public void testFindRootWithFunctionNoRoots() {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that has roots outside of the search interval and a simple checker.
   */
  @Test
  public void testFindRootWithFunctionRootsOutsideOfInterval() {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(-1.0)).thenReturn(1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
  }

}