package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionTestUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

/**
 * Test cases for {@link BrentSolver}.
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot_functionWithOneRoot() throws FunctionException {
    ComputableFunction function = FunctionTestUtils.createFunctionWithOneRoot(0.5);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(0.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_functionWithNoRoot() throws FunctionException {
    ComputableFunction function = FunctionTestUtils.createFunctionWithNoRoot();
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

    assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
  }

  @Test
  public void testFindRoot_functionWithTwoRoots() throws FunctionException {
    ComputableFunction function = FunctionTestUtils.createFunctionWithTwoRoots(1.0, 3.0);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot() - 1.0) < epsilon);
  }

  @Test
  public void testFindRoot_functionWithTwoRoots_highConverged() throws FunctionException {
    ComputableFunction function = FunctionTestUtils.createFunctionWithTwoRoots(1.0, 3.0);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot() - 3.0) < epsilon);
  }

  @Test
  public void testFindRoot_functionWithTwoRoots_tolerance() throws FunctionException {
    ComputableFunction function = FunctionTestUtils.createFunctionWithTwoRoots(1.0, 3.0);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

    solver.setEpsilon(0.1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot() - 1.5) < 0.1);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionWithException() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(anyDouble())).thenThrow(new FunctionException("Test exception"));
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double f0 = Double.NaN;
    double f1 = Double.NaN;

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

}