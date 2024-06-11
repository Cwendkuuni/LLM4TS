import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case for finding root in a valid interval
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    }, 100, 1.0, -3.0, 2.0, 0.0);
    
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testGetRoot() {
    // Test case for getting the root after finding it
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 9;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    }, 100, 1.0, -8.0, 4.0, 7.0);
    
    assertEquals(3.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootInvalidInterval() {
    // Test case for finding root in an invalid interval
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 16;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    }, 100, 4.0, 0.0, 3.0, 7.0);
    
    assertFalse(rootFound);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    // Test case for handling FunctionException
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Error in function evaluation");
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    }, 100, 1.0, 3.0, 2.0, 5.0);
  }

}