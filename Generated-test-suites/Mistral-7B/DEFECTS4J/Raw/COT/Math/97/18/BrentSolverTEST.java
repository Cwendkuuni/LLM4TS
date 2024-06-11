import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit test cases for BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() {
    // Given
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(0.5);
    when(checker.converged(x0, 1.0, x1, 0.5)).thenReturn(ConvergenceChecker.NONE);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, 1.0, x1, 0.5);

    // Then
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with convergence checker indicating low
   */
  @Test
  public void testFindRootLowConvergence() {
    // Given
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(0.5);
    when(checker.converged(x0, 1.0, x1, 0.5)).thenReturn(ConvergenceChecker.LOW);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, 1.0, x1, 0.5);

    // Then
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with convergence checker indicating high
   */
  @Test
  public void testFindRootHighConvergence() {
    // Given
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(0.5);
    when(checker.converged(x0, 1.0, x1, 0.5)).thenReturn(ConvergenceChecker.HIGH);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, 1.0, x1, 0.5);

    // Then
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function not converging
   */
  @Test
  public void testFindRootNoConvergence() {
    // Given
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(0.5);
    when(checker.converged(x0, 1.0, x1, 0.5)).thenReturn(ConvergenceChecker.NONE);

    // When
    boolean result = solver.findRoot(function, checker, 1, x0, 1.0, x1, 0.5);

    // Then
    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with exception thrown by function
   */
  @Test
  public void testFindRootFunctionException() {
    // Given
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    BrentSolver solver = new BrentSolver();

    when(function.valueAt(1.0)).thenThrow(new FunctionException("Test exception"));

    // When & Then
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 100, 1.0, 1.0, 2.0, 0.5));
  }

}