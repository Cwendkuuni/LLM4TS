package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
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
 * Test class for {@link BrentSolver}
 */
public class BrentSolverTest {

  private ComputableFunction function;
  private ConvergenceChecker checker;

  /**
   * Setup test environment
   */
  @Before
  public void setup() {
    function = mock(ComputableFunction.class);
    checker = mock(ConvergenceChecker.class);
  }

  /**
   * Test the {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * method with a simple example
   * @throws FunctionException if an error occurs during the root finding process
   */
  @Test
  public void testFindRootSimpleExample() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.0, 1.0, 2.0, 1.0);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.001);
  }

  /**
   * Test the {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * method with a more complex example where the function has multiple roots
   * @throws FunctionException if an error occurs during the root finding process
   */
  @Test
  public void testFindRootComplexExample() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(0.5);
    when(function.valueAt(2.0)).thenReturn(1.0);
    when(function.valueAt(3.0)).thenReturn(0.5);

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.0, 3.0, 2.0, 1.0);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.001);

    solver.reset();
    result = solver.findRoot(function, checker, 10, 0.0, 3.0, 0.0, 0.5);
    assertTrue(result);
    assertEquals(0.5, solver.getRoot(), 0.001);
  }

  /**
   * Test the {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * method with a test case where the function does not have a root in the given interval
   * @throws FunctionException if an error occurs during the root finding process
   */
  @Test
  public void testFindRootNoRoot() throws FunctionException {
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.0, 2.0, 1.0, 1.0);
    assertFalse(result);
  }

  /**
   * Test the {@link BrentSolver#getRoot()} method with a test case where a root has been
   * found
   */
  @Test
  public void testGetRootFound() {
    double root = 1.0;
    BrentSolver solver = new BrentSolver();
    solver.setRoot(root);
    assertEquals(root, solver.getRoot(), 0.001);
  }

  /**
   * Test the {@link BrentSolver#getRoot()} method with a test case where no root has been found
   */
  @Test
  public void testGetRootNotFound() {
    BrentSolver solver = new BrentSolver();
    assertFalse(Double.isFinite(solver.getRoot()));
  }
}