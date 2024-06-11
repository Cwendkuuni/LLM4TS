import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {
  
  @Test
  public void testFindRootTypicalCase() {
    // Typical case where a root is found in the given interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4; // Function that has a root at x = 2
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    assertTrue(solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 5.0));
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootNoRootInInterval() {
    // Case where no root is found in the given interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x + 1; // Function that has no real roots
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    assertFalse(solver.findRoot(function, checker, 100, 1.0, -1.0, 2.0, 5.0));
  }

  @Test
  public void testFindRootErrorScenario() {
    // Case where an exception is thrown by the function
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function evaluation error");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    try {
      solver.findRoot(function, checker, 100, -1.0, 1.0, 2.0, 4.0);
      fail("Exception expected");
    } catch (FunctionException e) {
      assertTrue(true);
    }
  }

}