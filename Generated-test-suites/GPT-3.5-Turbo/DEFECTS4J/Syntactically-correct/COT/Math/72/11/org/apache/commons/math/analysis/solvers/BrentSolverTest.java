import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create a dummy function
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        // Implement the function here
        return 0.0;
      }
    };
    
    // Create a dummy convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Implement the convergence checker here
        return 0;
      }
    };

    // Create a BrentSolver instance
    BrentSolver solver = new BrentSolver();

    // Test case 1: Test when the function has a root in the given interval
    boolean result = solver.findRoot(function, checker, 100, -1.0, -0.5, 0.5, 1.0);
    assertTrue(result);
    // Check the correctness of the found root
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
    
    // Test case 2: Test when the function has no root in the given interval
    boolean result2 = solver.findRoot(function, checker, 100, -10.0, -9.0, -8.0, -7.0);
    assertFalse(result2);
    // Check that the root is NaN
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  // Other test cases for the remaining public methods in the BrentSolver class
  // ...

}