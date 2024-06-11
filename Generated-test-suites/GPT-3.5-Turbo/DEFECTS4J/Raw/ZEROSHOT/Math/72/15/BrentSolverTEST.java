import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 2;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter1 = 100;
    double x01 = 0;
    double f01 = -2;
    double x11 = 10;
    double f11 = 8;
    
    boolean result1 = solver.findRoot(function1, checker1, maxIter1, x01, f01, x11, f11);
    double root1 = solver.getRoot();
    
    assertEquals(true, result1);
    assertEquals(2.0, root1, 1e-6);

    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter2 = 100;
    double x02 = -2;
    double f02 = 0;
    double x12 = 2;
    double f12 = 0;
    
    boolean result2 = solver.findRoot(function2, checker2, maxIter2, x02, f02, x12, f12);
    double root2 = solver.getRoot();
    
    assertEquals(true, result2);
    assertEquals(2.0, root2, 1e-6);
  }
}