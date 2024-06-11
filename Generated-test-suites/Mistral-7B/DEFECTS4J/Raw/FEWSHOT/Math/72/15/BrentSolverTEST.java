package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private ComputableFunction function;
  private ConvergenceChecker checker;
  private BrentSolver solver;

  @Before
  public void setUp() {
    function = mock(ComputableFunction.class);
    checker = mock(ConvergenceChecker.class);
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_converged_low() throws FunctionException {
    double x0 = 0.0;
    double f0 = 1.0;
    double x1 = 1.0;
    double f1 = 0.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, x0, f0, x1, f1));
    assertEquals(x0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_converged_high() throws FunctionException {
    double x0 = 0.0;
    double f0 = 1.0;
    double x1 = 1.0;
    double f1 = 0.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(solver.findRoot(function, checker, 10, x0, f0, x1, f1));
    assertEquals(x1, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_notConverged() throws FunctionException {
    double x0 = 0.0;
    double f0 = 1.0;
    double x1 = 1.0;
    double f1 = 0.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

    assertFalse(solver.findRoot(function, checker, 10, x0, f0, x1, f1));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    double x0 = 0.0;
    double f0 = 1.0;
    double x1 = 1.0;
    double f1 = 0.0;

    when(function.valueAt(x0)).thenThrow(new FunctionException());

    assertFalse(solver.findRoot(function, checker, 10, x0, f0, x1, f1));
  }

}