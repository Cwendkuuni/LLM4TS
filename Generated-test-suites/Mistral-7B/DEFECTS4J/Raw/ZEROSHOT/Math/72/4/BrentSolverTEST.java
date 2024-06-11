import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = Mockito.mock(ConvergenceChecker.class);
    function = Mockito.mock(ComputableFunction.class);
  }

  @Test
  public void testFindRoot_converged_low() throws FunctionException {
    // given
    double x0 = 1.0;
    double f0 = 4.0;
    double x1 = 2.0;
    double f1 = 0.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    // when
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    // then
    Mockito.verify(checker).converged(x0, f0, x1, f1);
    assertEquals(x0, solver.getRoot(), 0.0);
    assertEquals(true, result);
  }

  @Test
  public void testFindRoot_converged_high() throws FunctionException {
    // given
    double x0 = 1.0;
    double f0 = 4.0;
    double x1 = 2.0;
    double f1 = 0.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    // when
    when(checker.converged(Mockito.anyDouble(), Mockito.anyDouble(),
                           Mockito.anyDouble(), Mockito.anyDouble()))
        .thenReturn(ConvergenceChecker.HIGH);

    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    // then
    Mockito.verify(checker).converged(x0, f0, x1, f1);
    assertEquals(x1, solver.getRoot(), 0.0);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_function_exception() throws FunctionException {
    // given
    double x0 = 1.0;
    double x1 = 2.0;

    when(function.valueAt(x0)).thenThrow(new FunctionException());

    // when
    boolean result = solver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN);

    // then
    Mockito.verify(checker).converged(Mockito.anyDouble(), Mockito.anyDouble(),
                                     Mockito.anyDouble(), Mockito.anyDouble());
    assertEquals(Double.NaN, solver.getRoot());
    assertEquals(false, result);
  }

  @Test
  public void testFindRoot_max_iterations() throws FunctionException {
    // given
    double x0 = 1.0;
    double f0 = 4.0;
    double x1 = 2.0;
    double f1 = 0.0;

    when(function.valueAt(x0)).thenReturn(f0);
    when(function.valueAt(x1)).thenReturn(f1);

    // when
    boolean result = solver.findRoot(function, checker, 0, x0, f0, x1, f1);

    // then
    assertEquals(false, result);
  }

}