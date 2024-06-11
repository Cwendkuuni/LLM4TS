package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionException;

public class BrentSolverTest {

  private ConvergenceChecker checker;
  private ComputableFunction function;
  private BrentSolver solver;

  @Before
  public void setup() {
    function = mock(ComputableFunction.class);
    checker = mock(ConvergenceChecker.class);
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_simpleRoot() {
    when(function.valueAt(0.0)).thenReturn(1.0);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_rootOutsideInterval() {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
  }

  @Test
  public void testFindRoot_convergedRoot() {
    when(function.valueAt(0.5)).thenReturn(0.0);

    when(checker.converged(0.5, 0.0, 0.5, 0.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.5, 0.5, 0.0));
    assertEquals(0.5, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    when(function.valueAt(0.0)).thenThrow(new FunctionException());

    solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0);
  }

}

enum ConvergenceChecker {
  LOW,
  HIGH,
  NOT_CONVERGED
}