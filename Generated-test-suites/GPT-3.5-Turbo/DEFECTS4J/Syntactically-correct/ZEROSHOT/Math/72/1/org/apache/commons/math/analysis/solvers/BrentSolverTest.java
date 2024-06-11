import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Create a test function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Create a convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Create a BrentSolver instance
    BrentSolver solver = new BrentSolver();

    // Test finding roots
    assertTrue(solver.findRoot(function, checker, 100, 0, -1, 5, -19));
    assertTrue(solver.findRoot(function, checker, 100, -5, 19, -10, 96));
    assertFalse(solver.findRoot(function, checker, 100, 1, 2, 100, 200));

    // Test getting the root
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
}