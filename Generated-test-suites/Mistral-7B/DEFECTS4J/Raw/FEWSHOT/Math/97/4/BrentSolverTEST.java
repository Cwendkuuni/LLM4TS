package org.apache.commons.cli;

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
 * Test class for BrentSolver
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

  @Test
  public void testFindRoot_ConvergedAtLowerBound() {
    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(checker.converged(0.0, -1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 0.0, 0.0));
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ConvergedAtHigherBound() {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(checker.converged(0.0, 1.0, 0.0, 0.0)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ConvergedInInterval() {
    when(function.valueAt(0.0)).thenReturn(-1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(checker.converged(0.0, -1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.CONVERGED);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 0.0, 1.0));
    assertFalse(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testFindRoot_MaxIterationsExceeded() {
    when(function.valueAt(0.0)).thenReturn(Math.sin(Math.PI));
    when(function.valueAt(1.0)).thenReturn(Math.sin(Math.PI * 2));
    when(checker.converged(0.0, Math.sin(Math.PI), 1.0, Math.sin(Math.PI * 2))).thenReturn(ConvergenceChecker.NOT_CONVERGED);

    assertFalse(solver.findRoot(function, checker, 5, 0.0, 1.0, 0.0, 1.0));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    when(function.valueAt(0.0)).thenThrow(new FunctionException("Function exception"));

    solver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0);
  }
}