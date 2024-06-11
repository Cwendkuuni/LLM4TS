package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for the {@link BrentSolver} class.
 */
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
  public void testFindRoot_FunctionThrowsException_ReturnsFalse() throws Exception {
    when(function.valueAt(0.0)).thenThrow(new FunctionException());

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 2.0, 3.0));
  }

  @Test
  public void testFindRoot_NoRootInInterval_ReturnsFalse() {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);

    assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 2.0, 3.0));
  }

  @Test
  public void testFindRoot_RootFound_ReturnsTrue() {
    double root = 0.5;
    when(function.valueAt(root)).thenReturn(0.0);

    assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 2.0, 3.0));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_RootAtBoundary_ReturnsTrue() {
    double root = 0.0;
    when(function.valueAt(root)).thenReturn(0.0);

    assertTrue(solver.findRoot(function, checker, 10, root - epsilon, root, root + epsilon, root + epsilon));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_RootFoundByBisectonMethod_ReturnsTrue() {
    double root = 1.0 / 3.0;
    when(function.valueAt(root)).thenReturn(0.0);

    assertTrue(solver.findRoot(function, checker, 10, 1.0 / 4.0, 1.0 / 2.0, 3.0 * root, 2.0 * root));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_RootFoundBySecantMethod_ReturnsTrue() {
    double root = Math.log(2.0) / 2.0;
    double x1 = root - epsilon;
    double x2 = root + epsilon;
    when(function.valueAt(x1)).thenReturn(-1.0);
    when(function.valueAt(x2)).thenReturn(1.0);

    assertTrue(solver.findRoot(function, checker, 10, x1, x2, x1, x2));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_RootFoundByInterpolationMethod_ReturnsTrue() {
    double root = Math.sqrt(2.0);
    double x1 = root - epsilon;
    double x2 = root + epsilon;
    double x3 = root - epsilon / 2.0;
    when(function.valueAt(x1)).thenReturn(-2.0);
    when(function.valueAt(x2)).thenReturn(2.0);
    when(function.valueAt(x3)).thenReturn(0.0);

    assertTrue(solver.findRoot(function, checker, 10, x1, x2, x1, x3));
    assertEquals(root, solver.getRoot(), epsilon);
  }
}