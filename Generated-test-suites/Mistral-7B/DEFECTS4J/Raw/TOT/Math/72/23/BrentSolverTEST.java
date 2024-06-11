package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRoot() throws FunctionException {
    // Create mocks
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    // Set up expectations
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(1.0);
    when(function.valueAt(2.0)).thenReturn(1.0);

    when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.LOW);
    when(checker.converged(1.0, 1.0, 2.0, 1.0)).thenReturn(ConvergenceChecker.HIGH);
    when(checker.converged(1.5, 0.5, 2.5, -0.5)).thenReturn(ConvergenceChecker.INVALID);

    // Create BrentSolver instance
    BrentSolver solver = new BrentSolver();

    // Test successful root finding
    assertEquals(1.5, solver.findRoot(function, checker, 100, 0.0, 0.0, 2.5, -0.5).getRoot(), 0.001);

    // Test error scenarios
    try {
      solver.findRoot(function, checker, 1, 0.0, 0.0, 2.0, 1.0);
      // This should not be reached
    } catch (FunctionException e) {
      // Expected
    }

    try {
      solver.findRoot(function, null, 100, 0.0, 0.0, 2.0, 1.0);
      // This should not be reached
    } catch (NullPointerException e) {
      // Expected
    }
  }

}