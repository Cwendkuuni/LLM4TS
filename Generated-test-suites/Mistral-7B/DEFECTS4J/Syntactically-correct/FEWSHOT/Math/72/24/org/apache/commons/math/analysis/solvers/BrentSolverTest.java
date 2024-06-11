package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {

    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, 0.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, -1.0));
    assertEquals(0.0, solver.getRoot(), epsilon);

    when(checker.converged(0.0, 0.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.HIGH);

    solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, -1.0));
    assertEquals(1.0, solver.getRoot(), epsilon);

    when(checker.converged(0.0, 0.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

    solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, -1.0));

  }
}