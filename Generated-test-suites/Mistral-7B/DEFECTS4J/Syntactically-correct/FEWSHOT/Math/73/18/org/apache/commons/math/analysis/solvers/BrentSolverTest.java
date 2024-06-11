package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_Convergence() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    BrentSolver brentSolver = new BrentSolver();

    assertTrue(brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertEquals(0.5, brentSolver.getRoot(), 0.001);

    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.0);

    assertFalse(brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
  }

  @Test
  public void testFindRoot_DifferentSigns() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(-1.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);

    BrentSolver brentSolver = new BrentSolver();

    assertTrue(brentSolver.findRoot(function, checker, 10, -1.0, 1.0, 1.0, -1.0));
    assertEquals(0.0, brentSolver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_MaxIterations() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    BrentSolver brentSolver = new BrentSolver();

    assertFalse(brentSolver.findRoot(function, checker, 0, 0.0, 0.0, 1.0, 1.0));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenThrow(new FunctionException("Test exception"));

    BrentSolver brentSolver = new BrentSolver();

    brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0);
  }
}