package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    checker = mock(ConvergenceChecker.class);
    function = mock(ComputableFunction.class);
  }

  @Test
  public void testFindRoot_convergedLow() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(0.1);
    when(function.valueAt(1.0)).thenReturn(-0.1);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.1, 1.0, -0.1));
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedHigh() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(0.1);
    when(function.valueAt(1.0)).thenReturn(-0.1);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.1, 1.0, -0.1));
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedMid() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(0.1);
    when(function.valueAt(1.0)).thenReturn(0.2);
    when(function.valueAt(2.0)).thenReturn(-0.1);

    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.1, 1.0, -0.1));
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_notConverged() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(0.1);
    when(function.valueAt(1.0)).thenReturn(0.2);
    when(function.valueAt(2.0)).thenReturn(0.3);
    when(function.valueAt(3.0)).thenReturn(0.4);
    when(function.valueAt(4.0)).thenReturn(0.5);
    when(function.valueAt(5.0)).thenReturn(0.6);

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.1, 1.0, -0.1));
  }

  @Test
  public void testFindRoot_convergenceCheckerThrows() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(0.1);
    when(function.valueAt(1.0)).thenReturn(-0.1);

    when(checker.converged(0.0, 0.1, 1.0, -0.1)).thenThrow(new RuntimeException());

    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.1, 1.0, -0.1));
  }
}