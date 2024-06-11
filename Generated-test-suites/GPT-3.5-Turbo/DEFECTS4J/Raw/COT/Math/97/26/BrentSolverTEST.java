import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1: Basic functionality
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return IN_PROGRESS;
        }
      }
    };
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    int maxIter = 100;
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-6);
    
    // Test case 2: Edge case of the interval containing a root
    x0 = -10.0;
    f0 = function.valueAt(x0);
    x1 = 10.0;
    f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-6);
    
    // Test case 3: Edge case of a constant function
    function = new ComputableFunction() {
      public double valueAt(double x) {
        return 2.0;
      }
    };
    x0 = 0.0;
    f0 = function.valueAt(x0);
    x1 = 1.0;
    f1 = function.valueAt(x1);
    
    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    
    // Test case 4: Edge case of no root in the interval
    function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x + 4;
      }
    };
    x0 = 0.0;
    f0 = function.valueAt(x0);
    x1 = 2.0;
    f1 = function.valueAt(x1);
    
    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }

}