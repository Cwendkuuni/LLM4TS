package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private ComputableFunction function;
  private ConvergenceChecker checker;
  private BrentSolver brentSolver;

  @Before
  public void setup() {
    function = mock(ComputableFunction.class);
    checker = mock(ConvergenceChecker.class);
    brentSolver = new BrentSolver();
  }

  @Test
  public void testFindRoot_convergedLow() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(1.0, brentSolver.getRoot(), 0.0);
  }

  @Test
  public void testFindRoot_convergedHigh() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(-1.0, brentSolver.getRoot(), 0.0);
  }

  @Test
  public void testFindRoot_convergedBisection() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, -1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.NONE);

    when(function.valueAt(0.5)).thenReturn(0.1);

    assertTrue(brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(0.5, brentSolver.getRoot(), 0.0);
  }

  @Test
  public void testFindRoot_notConverged() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, -1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.NONE);

    when(function.valueAt(0.5)).thenReturn(0.2);

    assertFalse(brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
  }
}