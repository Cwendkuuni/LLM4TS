import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  // Create an instance of BrentSolver
  BrentSolver solver = new BrentSolver();

  // Test findRoot() method
  @Test
  public void testFindRoot() throws FunctionException {
    // Define a test function
    ComputableFunction testFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4 * x + 3;
      }
    };

    // Define a convergence checker
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ROOT_FOUND_LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ROOT_FOUND_HIGH;
        } else {
          return NOT_CONVERGED;
        }
      }
    };

    // Call the findRoot() method
    boolean result = solver.findRoot(testFunction, convergenceChecker, 100, 0, -1, 3, 4);

    // Verify the result
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }

  // Test getRoot() method
  @Test
  public void testGetRoot() {
    // Set the root manually
    solver.root = 2.0;

    // Call the getRoot() method and verify the result
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

}