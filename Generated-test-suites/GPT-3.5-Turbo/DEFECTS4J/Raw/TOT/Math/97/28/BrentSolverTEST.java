import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_TypicalCase() {
    // Typical case where a root exists in the given interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }  
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    boolean result = solver.findRoot(function, checker, 100, 0, -4, 3, 5);
    assertTrue(result);
    assertEquals(2, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_EdgeCase() {
    // Edge case where the interval size is minimum (0)
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }  
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    boolean result = solver.findRoot(function, checker, 100, 0, -4, 0, -4);
    assertTrue(result);
    assertEquals(0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_ErrorCase() {
    // Error case where the function does not have a root in the given interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }  
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    boolean result = solver.findRoot(function, checker, 100, 0, -1, 3, 1);
    assertFalse(result);
  }

}