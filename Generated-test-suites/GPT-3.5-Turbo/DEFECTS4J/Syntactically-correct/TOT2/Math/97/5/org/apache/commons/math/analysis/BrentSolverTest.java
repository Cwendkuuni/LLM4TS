import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 4; // function: f(x) = x^2 - 4
      }
    };

    boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, 0.0, -4.0, 3.0, 5.0);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 2; // function: f(x) = x^2 - 2
      }
    };

    solver.findRoot(function, new ConvergenceChecker(), 100, 0.0, -2.0, 3.0, 7.0);
    assertEquals(Math.sqrt(2), solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootEdgeCases() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x == 0.0) {
          throw new FunctionException("division by zero");
        }
        return 1.0 / x; // function: f(x) = 1/x
      }
    };

    boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, -1.0, -1.0, 1.0, 1.0);
    assertFalse(rootFound);
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("test exception");
      }
    };

    solver.findRoot(function, new ConvergenceChecker(), 100, -1.0, -1.0, 1.0, 1.0);
  }
}