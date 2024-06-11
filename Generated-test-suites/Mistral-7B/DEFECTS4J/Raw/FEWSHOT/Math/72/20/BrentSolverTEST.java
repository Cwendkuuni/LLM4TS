package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleCase() {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(-1.0);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, x0, 1.0, x1, -1.0));
    assertTrue(Double.isFinite(solver.getRoot()));
  }

  @Test
  public void testFindRoot_convergedAtLowerBound() {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(function.valueAt(x0)).thenReturn(0.0);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, x0, 0.0, x1, Double.NaN));
    assertTrue(Double.isFinite(solver.getRoot()));
  }

  @Test
  public void testFindRoot_convergedAtHigherBound() {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(function.valueAt(x1)).thenReturn(0.0);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, x0, Double.NaN, x1, 0.0));
    assertTrue(Double.isFinite(solver.getRoot()));
  }

  @Test
  public void testFindRoot_convergedAtMidpoint() {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(-1.0);
    when(checker.converged(x0, 1.0, x1, -1.0)).thenReturn(ConvergenceChecker.MIDPOINT);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, x0, 1.0, x1, -1.0));
    assertTrue(Double.isFinite(solver.getRoot()));
  }

  @Test
  public void testFindRoot_maxIterationsReached() {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(-1.0);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 0, x0, 1.0, x1, -1.0));
    assertNull(solver.getRoot());
  }

}