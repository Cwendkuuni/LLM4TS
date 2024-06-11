package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/** This class tests the methods of the BrentSolver class.

 */

public class BrentSolverTest {

  @Test
  public void testFindRoot_convergedAtLowerBound() {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    when(function.valueAt(0.0)).thenReturn(0.0);
    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedAtHigherBound() {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    when(function.valueAt(1.0)).thenReturn(1.0);
    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedInTheMiddle() {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    when(function.valueAt(0.5)).thenReturn(0.0);
    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.CONVERGED);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertEquals(0.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_noConvergence() {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.NO_CONVERGENCE);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertNull(solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    when(function.valueAt(0.0)).thenThrow(new FunctionException("Function exception"));

    solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0);
  }

}