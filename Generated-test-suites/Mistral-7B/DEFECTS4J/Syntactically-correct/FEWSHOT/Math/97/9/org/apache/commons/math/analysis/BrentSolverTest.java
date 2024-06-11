package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() throws FunctionException {
    function = mock(ComputableFunction.class);
    checker = mock(ConvergenceChecker.class);
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_ConvergedLow() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    ArgumentCaptor<ComputableFunction> functionArgumentCaptor = ArgumentCaptor.forClass(ComputableFunction.class);
    ArgumentCaptor<ConvergenceChecker> checkerArgumentCaptor = ArgumentCaptor.forClass(ConvergenceChecker.class);
    ArgumentCaptor<int[]> iterationsArgumentCaptor = ArgumentCaptor.forClass(int[].class);

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    // Check that the expected arguments have been passed to the findRoot method
    assertTrue(checkerArgumentCaptor.getValue().getClass().isInstance(checker));
    assertTrue(functionArgumentCaptor.getValue().getClass().isInstance(function));
    int[] actualIterations = iterationsArgumentCaptor.getValue();
    int[] expectedIterations = new int[10];
    assertArrayEquals(expectedIterations, actualIterations);

    // Check that the root has been found
    double expectedRoot = (x0 + x1) / 2.0;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.0);
  }

  @Test
  public void testFindRoot_ConvergedHigh() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    double expectedRoot = x1;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.0);
  }

  @Test
  public void testFindRoot_NotConverged() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

    solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    assertFalse(solver.getRoot() == Double.NaN);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;

    when(checker.converged(x0, 0.0, x1, 0.0)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

    solver.findRoot(mock(ComputableFunction.class), checker, 10, x0, 0.0, x1, 0.0);
  }
}