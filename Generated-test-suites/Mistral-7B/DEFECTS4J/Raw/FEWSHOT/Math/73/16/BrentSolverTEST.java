package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

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

  @Test
  public void testFindRoot_ConvergedAtLowPoint() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.LOW);

    // when
    boolean result = solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0);

    // then
    assertTrue(result);
  }

  @Test
  public void testFindRoot_ConvergedAtHighPoint() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.HIGH);

    // when
    boolean result = solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0);

    // then
    assertTrue(result);
  }

  @Test
  public void testFindRoot_ConvergedAtRoot() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);
    when(checker.converged(0.0, 1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.LOW);

    // when
    boolean result = solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0);

    // then
    assertTrue(result);
  }

  @Test
  public void testFindRoot_MaxIterationsExceeded() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);
    when(checker.converged(0.0, 1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.UNKNOWN);

    // when
    boolean result = solver.findRoot(function, checker, 1, 0.0, 1.0, 1.0, 1.0);

    // then
    assertFalse(result);
  }
}