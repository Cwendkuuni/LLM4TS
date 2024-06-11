package org.apache.commons.cli.roots; // Assuming a package name change for testing

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot_Converged_Low() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(ArgumentMatchers.doubleValue(0.0))).thenReturn(0.0);
    when(checker.converged(ArgumentMatchers.doubleValue(0.0), ArgumentMatchers.doubleValue(0.0),
        ArgumentMatchers.doubleValue(1.0), ArgumentMatchers.doubleValue(0.0))).thenReturn(
        ConvergenceChecker.LOW);

    BrentSolver brentSolver = new BrentSolver();
    assertTrue(brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 0.0));
    assertEquals(0.0, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_Converged_High() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(ArgumentMatchers.doubleValue(1.0))).thenReturn(1.0);
    when(checker.converged(ArgumentMatchers.doubleValue(1.0), ArgumentMatchers.doubleValue(1.0),
        ArgumentMatchers.doubleValue(0.0), ArgumentMatchers.doubleValue(0.0))).thenReturn(
        ConvergenceChecker.HIGH);

    BrentSolver brentSolver = new BrentSolver();
    assertTrue(brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
    assertEquals(1.0, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_Converged_Zero() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(ArgumentMatchers.doubleValue(0.0))).thenReturn(0.0);
    when(checker.converged(ArgumentMatchers.doubleValue(0.0), ArgumentMatchers.doubleValue(0.0),
        ArgumentMatchers.doubleValue(0.0), ArgumentMatchers.doubleValue(0.0))).thenReturn(
        ConvergenceChecker.ZERO);

    BrentSolver brentSolver = new BrentSolver();
    assertTrue(brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0));
    assertEquals(0.0, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_MaxIterationsReached() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(ArgumentMatchers.doubleValue(0.0))).thenReturn(0.0);
    when(function.valueAt(ArgumentMatchers.doubleValue(1.0))).thenReturn(1.0);
    when(checker.converged(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble(),
        ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble())).thenReturn(
        ConvergenceChecker.NONE);

    BrentSolver brentSolver = new BrentSolver();
    assertFalse(brentSolver.findRoot(function, checker, 1, 0.0, 0.0, 1.0, 0.0));
    assertNull(brentSolver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(ArgumentMatchers.doubleValue(0.0))).thenThrow(new FunctionException());

    BrentSolver brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 1, 0.0, 0.0, 0.0, 0.0);
  }

  @Test
  public void testFindRoot_DifferentFunctionValues() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    when(function.valueAt(ArgumentMatchers.doubleValue(0.0))).thenReturn(0.0);
    when(function.valueAt(ArgumentMatchers.doubleValue(1.0))).thenReturn(1.0);
    when(checker.converged(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble(),
        ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble())).thenReturn(
        ConvergenceChecker.NONE);

    BrentSolver brentSolver = new BrentSolver();
    assertFalse(brentSolver.findRoot(function, checker, 1, 0.0, 0.0, 1.0, 0.0));
    assertNull(brentSolver.getRoot());
  }

}