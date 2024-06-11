import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Typical use case
    // Testing findRoot function when a root is found in the given interval
    // Check if the root is correctly calculated
    assertTrue(solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow * fHigh <= 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    }, 100, 1, -3, 4, 12));
    assertEquals(2, solver.getRoot(), 1e-10);
    
    // Edge case
    // Testing findRoot function when a root is at the lower bound of the interval
    // Check if the root is correctly calculated
    assertTrue(solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow * fHigh <= 0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    }, 100, 0, 0, 10, 10));
    assertEquals(0, solver.getRoot(), 1e-10);
    
    // Exception handling
    // Testing findRoot function when the function value is invalid
    // Check if FunctionException is thrown
    try {
      solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
          throw new FunctionException("Invalid function value");
        }
      }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
          return ConvergenceChecker.NONE;
        }
      }, 100, 1, 2, 3, 4);
      // If exception is not thrown, fail test case
      fail("FunctionException should have been thrown");
    } catch (FunctionException e) {
      // Exception thrown, test passed
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Typical use case
    // Testing getRoot function to get the abscissa of the root
    // Check if the correct root value is returned
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow * fHigh <= 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    }, 100, 1, -3, 4, 12);
    assertEquals(2, solver.getRoot(), 1e-10);
  }

}