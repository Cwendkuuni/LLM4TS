import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRootValidInput() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();

    try {
      boolean rootFound = solver.findRoot(function, checker, 1000, 0, -2, 2, 2);
      assertTrue(rootFound);
      assertEquals(1.4142135623730951, solver.getRoot(), 1e-10);
    } catch (FunctionException e) {
      fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x;
      }
    }, new ConvergenceChecker(), 1000, 0, 0, 1, 1);
    assertEquals(0.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootInvalidInput() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function evaluation error");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();

    try {
      boolean rootFound = solver.findRoot(function, checker, 1000, 0, 0, 0, 0);
      assertFalse(rootFound);
      assertEquals(Double.NaN, solver.getRoot(), 1e-10);
    } catch (FunctionException e) {
      assertTrue(true);
    }
  }

}