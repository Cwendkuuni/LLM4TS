import org.junit.Test;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() {
    // Create a BrentSolver object
    BrentSolver solver = new BrentSolver();
    
    // Define a sample function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4; // Function: f(x) = x^2 - 4
      }
    };
    
    // Define a sample convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double tol = 1e-6; // Tolerance for convergence
        if (Math.abs(fLow) < tol) {
          return ConvergenceChecker.LOW; // Converged to lower bound
        } else if (Math.abs(fHigh) < tol) {
          return ConvergenceChecker.HIGH; // Converged to upper bound
        } else {
          return ConvergenceChecker.NONE; // Not converged
        }
      }
    };
    
    // Define initial values for the interval
    double x0 = 1;
    double f0 = function.valueAt(x0);
    double x1 = 3;
    double f1 = function.valueAt(x1);
    
    // Call the findRoot method and assert the result
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6); // Check if root is close to 2.0
  }

  // Additional test cases for potential edge cases and exceptions
  // ...

}