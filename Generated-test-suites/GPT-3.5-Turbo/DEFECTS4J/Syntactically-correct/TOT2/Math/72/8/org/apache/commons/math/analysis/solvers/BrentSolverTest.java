import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Initialize BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Define a sample function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // function: f(x) = x^2 - 4
      }
    };

    // Use ConvergenceChecker to check the convergence of the function
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow > 0 && fHigh < 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    // Test the findRoot method with a valid interval
    boolean rootFound = solver.findRoot(function, checker, 100, 1, -3, 2, 0);
    assertTrue(rootFound);

    // Check the value of the root
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testInvalidInterval() {
    // Initialize BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Define a sample function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // function: f(x) = x^2 - 4
      }
    };

    // Use ConvergenceChecker to check the convergence of the function
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow > 0 && fHigh < 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    // Test the findRoot method with an invalid interval
    boolean rootFound = solver.findRoot(function, checker, 100, 7, -5, -3, 2);
    assertFalse(rootFound);
  }

  // Add more test cases for edge cases and error scenarios as needed

}