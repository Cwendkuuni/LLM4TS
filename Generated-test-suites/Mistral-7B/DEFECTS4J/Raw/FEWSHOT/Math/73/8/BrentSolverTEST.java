package org.apache.commons.cli.test; // or your preferred package name

import static org.junit.Assert.assertArrayEquals;
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
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(0.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootConvergenceHigh() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(checker.converged(0.0, 1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.HIGH);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0));
    assertEquals(1.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootConverged() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(0.0);
    when(checker.converged(0.0, 0.0, 0.0, 0.0)).thenReturn(ConvergenceChecker.CONVERGED);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0));
    assertEquals(0.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootMaxIter() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 0, 0.0, 1.0, 1.0, -1.0));
  }

}