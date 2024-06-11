import org.junit.Assert;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1: Function with a root in the interval
    double x0 = -10.0;
    double f0 = Math.pow(x0, 3) + 3 * Math.pow(x0, 2) + 2 * x0 - 1;
    double x1 = 5.0;
    double f1 = Math.pow(x1, 3) + 3 * Math.pow(x1, 2) + 2 * x1 - 1;
    
    boolean hasRoot = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 3) + 3 * Math.pow(x, 2) + 2 * x - 1;
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
    }, 1000, x0, f0, x1, f1);
    
    Assert.assertTrue(hasRoot);
    Assert.assertTrue(Math.abs(solver.getRoot() - 0.821053) < 1e-6);
    
    // Test case 2: Function with no root in the interval
    x0 = -5.0;
    f0 = Math.pow(x0, 2) - 5 * x0 + 6;
    x1 = 5.0;
    f1 = Math.pow(x1, 2) - 5 * x1 + 6;
    
    hasRoot = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 2) - 5 * x + 6;
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
    }, 1000, x0, f0, x1, f1);
    
    Assert.assertFalse(hasRoot);
    Assert.assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1: Root has been found
    solver.root = 2.5;
    Assert.assertEquals(2.5, solver.getRoot(), 0.0);
    
    // Test case 2: No root has been found
    solver.root = Double.NaN;
    Assert.assertTrue(Double.isNaN(solver.getRoot()));
  }

}