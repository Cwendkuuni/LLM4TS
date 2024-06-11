package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionMock;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setup() {
    function = mock(FunctionMock.class);
    checker = mock(ConvergenceChecker.class);
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_Converged_Low() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_Converged_High() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.0);
    when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_Converged_Interpolation() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.5);
    when(function.valueAt(2.0)).thenReturn(-0.5);
    when(checker.converged(0.0, 1.0, 1.0, 0.5)).thenReturn(ConvergenceChecker.INTERPOLATION);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.5));
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_NotConverged() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.5);
    when(function.valueAt(2.0)).thenReturn(-0.5);
    when(checker.converged(0.0, 1.0, 1.0, 0.5)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.5));
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionThrowsException() throws FunctionException {
    when(function.valueAt(0.0)).thenThrow(new FunctionException());

    solver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0);
  }

}

class FunctionMock implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    return 0;
  }
}

interface ConvergenceChecker {
  int converged(double x, double fx, double x1, double fx1);
}

enum ConvergenceChecker {
  LOW,
  HIGH,
  INTERPOLATION,
  NOT_CONVERGED
}