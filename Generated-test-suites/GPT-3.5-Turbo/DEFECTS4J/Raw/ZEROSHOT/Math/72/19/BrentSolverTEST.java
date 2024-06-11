import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();

    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return KEEP_GOING;
        }
      }
    };
    int maxIter1 = 100;
    double x01 = -10.0;
    double f01 = function1.valueAt(x01);
    double x11 = 10.0;
    double f11 = function1.valueAt(x11);
    boolean result1 = solver.findRoot(function1, checker1, maxIter1, x01, f01, x11, f11);
    assertTrue(result1);
    assertEquals(2.0, solver.getRoot(), 1e-6);

    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return KEEP_GOING;
        }
      }
    };
    int maxIter2 = 100;
    double x02 = -10.0;
    double f02 = function2.valueAt(x02);
    double x12 = 10.0;
    double f12 = function2.valueAt(x12);
    boolean result2 = solver.findRoot(function2, checker2, maxIter2, x02, f02, x12, f12);
    assertTrue(result2);
    assertEquals(0.0, solver.getRoot(), 1e-6);

    // Test case 3
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x * x - 2;
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return KEEP_GOING;
        }
      }
    };
    int maxIter3 = 100;
    double x03 = -10.0;
    double f03 = function3.valueAt(x03);
    double x13 = 10.0;
    double f13 = function3.valueAt(x13);
    boolean result3 = solver.findRoot(function3, checker3, maxIter3, x03, f03, x13, f13);
    assertTrue(result3);
    assertEquals(Math.cbrt(2.0), solver.getRoot(), 1e-6);
  }
}