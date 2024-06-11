import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (x1 == x2) {
          return ConvergenceChecker.HIGH;
        }
        if (Math.abs(f1) <= 1e-6) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(f2) <= 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    
    // Test typical use case
    assertTrue(solver.findRoot(function, checker, 100, -10, 96, 5, -11));
    assertEquals(2, solver.getRoot(), 1e-6);

    // Test edge case of a function with only one root
    assertTrue(solver.findRoot(function, checker, 100, -10, 96, 10, -36));
    assertEquals(2, solver.getRoot(), 1e-6);

    // Test edge case of maximum number of iterations reached
    assertFalse(solver.findRoot(function, checker, 3, -10, 96, 10, -36));
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);

    // Test error scenario when function throws an exception
    ComputableFunction errorFunction = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("An error occurred in the function");
      }
    };

    try {
      solver.findRoot(errorFunction, checker, 100, -10, 96, 10, -36);
      fail("No exception thrown");
    } catch (FunctionException e) {
      assertEquals("An error occurred in the function", e.getMessage());
    }
  }
}