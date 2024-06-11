package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

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
  public void testFindRoot_simple() {
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(checker.converged(0.0, 0.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 0.0));
    assertEquals(0.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRoot_complex() {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(0.5, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRoot_noRoot() {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(checker.converged(0.0, 1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.NONE);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0));
    assertEquals(Double.NaN, solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    when(function.valueAt(0.0)).thenThrow(new FunctionException("test"));

    solver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0);
  }

}

interface ConvergenceChecker {
  int converged(double x, double fx, double x2, double fx2);

  static final int LOW = 0;
  static final int HIGH = 1;
  static final int NONE = 2;
}