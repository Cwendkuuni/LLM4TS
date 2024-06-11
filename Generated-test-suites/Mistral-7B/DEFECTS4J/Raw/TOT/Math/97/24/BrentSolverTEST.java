import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionWithRoots;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setup() {
    checker = Mockito.spy(new ConvergenceChecker() {
      @Override
      public int convergenceCheck(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    });
    function = Mockito.mock(ComputableFunction.class);
    solver = new BrentSolver();
  }

  @Test
  public void testFindRootWithFunctionWithTwoRoots() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double tolerance = 0.001;

    Mockito.when(function.valueAt(x0)).thenReturn(1.0);
    Mockito.when(function.valueAt(x1)).thenReturn(-1.0);

    solver.findRoot(function, checker, 100, x0, 1.0, x1, -1.0);

    ArgumentCaptor<Double> rootCaptor = ArgumentCaptor.forClass(Double.class);
    Mockito.verify(checker).converged(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble());
    Mockito.verify(checker).converged(Mockito.eq(1.536226328753013), Mockito.eq(0.0), Mockito.anyDouble(), Mockito.anyDouble());
    assertEquals(1.536226328753013, solver.getRoot(), tolerance);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionWithNoRoots() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;

    Mockito.when(function.valueAt(x0)).thenReturn(-1.0);
    Mockito.when(function.valueAt(x1)).thenReturn(-1.0);

    solver.findRoot(function, checker, 100, x0, -1.0, x1, -1.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionThrowingException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;

    Mockito.doThrow(new FunctionException("Test Error")).when(function).valueAt(x0);

    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test
  public void testGetRootWithNoRootFound() {
    assertNaN(solver.getRoot());
  }

  @Test
  public void testGetRootWithRootFound() throws FunctionException {
    double root = 1.536226328753013;

    Mockito.when(function.valueAt(Mockito.anyDouble())).thenAnswer(i -> Math.pow(i.getArgument(), 2) - 4);

    solver.findRoot(function, checker, 100, 1.0, 1.0, 3.0, 3.0);

    assertEquals(root, solver.getRoot(), 0.001);
  }

  private void assertNaN(Double value) {
    assertEquals(Double.NaN, value);
  }
}