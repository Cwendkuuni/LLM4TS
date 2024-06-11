package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * A simple test case for a function with a root.
   */
  @Test
  public void testSimpleCase() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);

    assertEquals(0.5, solver.getRoot(), 1e-6);
  }

  /**
   * A test case for a function with two roots.
   */
  @Test
  public void testTwoRoots() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(function.valueAt(-2.0)).thenReturn(-1.0);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, -2.0, -1.0, 1.0, 1.0);

    assertEquals(-1.0, solver.getRoot(), 1e-6);

    solver.findRoot(function, checker, 100, -2.0, -1.0, 1.0, 1.0);

    assertEquals(1.0, solver.getRoot(), 1e-6);
  }

  /**
   * A test case for a function with no roots.
   */
  @Test(expected = FunctionException.class)
  public void testNoRoots() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);

    // The test should throw a FunctionException if no root is found after the
    // maximum number of iterations.
  }

  /**
   * A test case for a function with a root outside the interval.
   */
  @Test(expected = FunctionException.class)
  public void testRootOutsideInterval() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(-1.0)).thenReturn(-1.0);
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);

    // The test should throw a FunctionException if the root is outside the
    // interval.
  }

  /**
   * A test case for a function with a root very close to the boundary of the
   * interval.
   */
  @Test
  public void testRootCloseToBoundary() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(-1.0)).thenReturn(-1.0);
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(function.valueAt(1.01)).thenReturn(0.01);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);

    assertEquals(1.0, solver.getRoot(), 1e-6);
  }

}