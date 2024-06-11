import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  // Test findRoot method with typical use case
  @Test
  public void testFindRootTypicalCase() {
    BrentSolver solver = new BrentSolver();
    double result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow < 0 && fHigh > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    }, 100, 1.0, -3.0, 3.0, 5.0);
    
    assertEquals(2.0, result, 1e-10);
  }

  // Test findRoot method with edge case when maximum iterations are reached
  @Test
  public void testFindRootMaxIterationsReached() {
    BrentSolver solver = new BrentSolver();
    
    assertFalse(solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return 0.0;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    }, 100, 1.0, -1.0, 2.0, 0.0));
  }

  // Test findRoot method with error scenario when function throws an exception
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionThrowsException() {
    BrentSolver solver = new BrentSolver();
    
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Error in function evaluation");
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    }, 100, 1.0, -1.0, 2.0, 0.0);
  }

  // Test getRoot method
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow < 0 && fHigh > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    }, 100, 1.0, -3.0, 3.0, 5.0);
    
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

}