package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  interface ConvergenceChecker {
    int converged(double x, double f, double xLow, double fLow);

    static final int LOW = 0;
    static final int HIGH = 1;
  }

  @Test
  public void testFindRoot_success() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(Double.NaN)).thenThrow(new FunctionException("Function evaluation failed."));
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0));
    assertTrue(Double.isFinite(solver.getRoot()));
  }

  @Test
  public void testFindRoot_convergence() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(Double.NaN)).thenThrow(new FunctionException("Function evaluation failed."));
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.0);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(0.0, 1.0, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(1.0, 0.0, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.HIGH);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0));
    assertTrue(Double.isFinite(solver.getRoot()));
  }

  @Test
  public void testFindRoot_maxIterations() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(Double.NaN)).thenThrow(new FunctionException("Function evaluation failed."));
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 0, 0.0, 0.0, 1.0, 1.0));
    assertNull(solver.getRoot());
  }

  @Test
  public void testFindRoot_failure() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(Double.NaN)).thenThrow(new FunctionException("Function evaluation failed."));
    when(function.valueAt(0.0)).thenThrow(new FunctionException("Function evaluation failed."));

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0));
    assertNull(solver.getRoot());
  }

  @Test
  public void testFindRoot_failure_functionEvaluation() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(Double.NaN)).thenThrow(new FunctionException("Function evaluation failed."));

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);
      fail("Expected FunctionException.");
    } catch (FunctionException e) {
      // expected
    }
    assertNull(solver.getRoot());
  }

  @Test
  public void testFindRoot_failure_convergence() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(Double.NaN)).thenReturn(Double.NaN);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0);
      fail("Expected FunctionException.");
    } catch (FunctionException e) {
      // expected
    }
    assertNull(solver.getRoot());
  }

  @Test
  public void testFindRoot_failure_maxIterations_convergence() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(Double.NaN)).thenReturn(Double.NaN);

    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN))
        .thenReturn(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 1, 0.0, 0.0, 1.0, 1.0);
      fail("Expected FunctionException.");
    } catch (FunctionException e) {
      // expected
    }
    assertNull(solver.getRoot());
  }
}