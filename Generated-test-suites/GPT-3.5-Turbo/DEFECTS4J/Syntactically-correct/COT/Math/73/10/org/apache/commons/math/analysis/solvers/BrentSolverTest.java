import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();
    
    // Define a test function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    
    // Define a test convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    // Call the findRoot method with known interval containing the root
    boolean rootFound = solver.findRoot(function, checker, 100, 2, -4, 0, 4);
    
    // Check if a root has been found in the given interval
    assertTrue(rootFound);
    
    // Check if the root is close to the expected value
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();
    
    // Set a known root value
    solver.root = 2.0;
    
    // Check if the getRoot method returns the correct root value
    assertEquals(2.0, solver.getRoot(), 0.0);
  }

}