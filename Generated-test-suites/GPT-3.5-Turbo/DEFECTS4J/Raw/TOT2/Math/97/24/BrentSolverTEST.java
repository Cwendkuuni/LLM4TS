import static org.junit.Assert.*;

import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case for finding a root in a given interval
    BrentSolver solver = new BrentSolver();
    
    // Define a dummy function for testing
    ComputableFunction dummyFunction = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4; // Function: f(x) = x^2 - 4
      }
    };
    
    ConvergenceChecker dummyChecker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.HIGH; // Force convergence at higher bound
      }
    };
    
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 3.0;
    double f1 = 5.0;
    
    boolean rootFound = solver.findRoot(dummyFunction, dummyChecker, maxIter, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-9); // Expected root: 2.0
  }

  @Test
  public void testGetRoot() {
    // Test case for getting the root after solving
    BrentSolver solver = new BrentSolver();
    
    solver.findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x); // Function: f(x) = sin(x)
      }
    }, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW; // Force convergence at lower bound
      }
    }, 100, 0.0, 0.0, Math.PI, 0.0);
    
    assertEquals(0.0, solver.getRoot(), 1e-9); // Expected root: 0.0
  }

}