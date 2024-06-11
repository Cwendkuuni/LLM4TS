import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    // Test case 1
    double x0 = 1.0;
    double f0 = 0.0;
    double x1 = 2.0;
    double f1 = 0.0;
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    }, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 1e-6);
    // Test case 2
    x0 = -1.0;
    f0 = -1.0;
    x1 = 1.0;
    f1 = 1.0;
    result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    }, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    }, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }
}