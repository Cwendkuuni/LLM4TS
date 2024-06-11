package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ScaledFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.RootsFinder;

public class BrentSolverTest {

  private ComputableFunction function;
  private ConvergenceChecker checker;
  private RootsFinder solver;

  @Before
  public void setUp() {
    function = new DummyFunction();
    checker = mock(ConvergenceChecker.class);
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_convergenceAtLowerBound() throws FunctionException {
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(0.0, solver.getRoot(), 1.0e-9);
  }

  @Test
  public void testFindRoot_convergenceAtHigherBound() throws FunctionException {
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(1.0, solver.getRoot(), 1.0e-9);
  }

  @Test
  public void testFindRoot_convergenceInInterval() throws FunctionException {
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.MEDIUM);

    ScaledFunction scaledFunction = new ScaledFunction(function, 1.0e-6);

    double root = 0.5;
    when(checker.converged(root - 0.01, scaledFunction.valueAt(root - 0.01), root + 0.01, scaledFunction.valueAt(root + 0.01)))
        .thenReturn(ConvergenceChecker.MEDIUM);
    when(checker.converged(root, scaledFunction.valueAt(root), root, scaledFunction.valueAt(root)))
        .thenReturn(ConvergenceChecker.LOW);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(root, solver.getRoot(), 1.0e-9);
  }

  @Test
  public void testFindRoot_noConvergence() throws FunctionException {
    when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.MEDIUM);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0));
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

}