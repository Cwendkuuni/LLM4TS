import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1
    double x0 = -10.0;
    double f0 = 10.0;
    double x1 = 10.0;
    double f1 = -10.0;
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 100;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    }, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(Math.sqrt(100), solver.getRoot(), 1e-6);

    // Test case 2
    x0 = -100.0;
    f0 = -100.0;
    x1 = 0.0;
    f1 = 0.0;
    result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    }, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }
}