import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot() throws FunctionException {
    ComputingFunction function = new ComputingFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, 0, -4, 10, 56);

    assertTrue(result);
    assertEquals(2.00048828, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootInvalidInterval() throws FunctionException {
    ComputingFunction function = new ComputingFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, 0, -4, -1, -5);

    assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    ComputingFunction function = new ComputingFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Function error");
      }
    };

    solver.findRoot(function, new ConvergenceChecker(), 100, 0, -4, 10, 56);
  }
}