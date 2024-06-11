import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(function.valueAt(Double.NEGATIVE_INFINITY)).thenReturn(Double.NEGATIVE_INFINITY);
    when(function.valueAt(Double.POSITIVE_INFINITY)).thenReturn(Double.POSITIVE_INFINITY);
    when(function.valueAt(-1.0)).thenReturn(1.0);
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);

    BrentSolver solver = new BrentSolver();

    // Test with negative x0, x1
    assertFalse(solver.findRoot(function, checker, 10, -1.0, -2.0, 0.0, 1.0));

    // Test with x0 == x1
    assertFalse(solver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0));

    // Test with x0 >= x1
    assertFalse(solver.findRoot(function, checker, 10, 1.0, 0.0, 1.0, 1.0));

    // Test with convergence
    double[] expectedRoots = {-1.0, 0.0};
    Function<Double, Double> expectedFunctionValues = x -> (x < 0.0) ? 1.0 : -1.0;
    when(function.valueAt(Double.class.cast(anyObject()))).then(expectedFunctionValues);

    double[] roots = new double[2];
    assertTrue(solver.findRoot(function, checker, 10, -1.0, 1.0, 0.0, 1.0));
    assertArrayEquals(expectedRoots, roots);

    // Test with no convergence
    when(function.valueAt(Double.class.cast(anyObject()))).thenAnswer(x -> Math.sin(Math.PI * x));
    assertFalse(solver.findRoot(function, checker, 10, -1.0, 1.0, 0.0, 1.0));

    // Test with root at infinity
    when(function.valueAt(Double.POSITIVE_INFINITY)).thenReturn(Double.NEGATIVE_INFINITY);
    when(function.valueAt(Double.NEGATIVE_INFINITY)).thenReturn(Double.POSITIVE_INFINITY);

    assertFalse(solver.findRoot(function, checker, 10, -1.0, 1.0, 0.0, 1.0));
  }

}