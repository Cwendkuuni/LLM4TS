import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter1 = 100;
    double x01 = 0;
    double f01 = -2;
    double x11 = 2;
    double f11 = 2;
    assertTrue(solver.findRoot(function1, checker1, maxIter1, x01, f01, x11, f11));
    assertEquals(1.414, solver.getRoot(), 0.001);
    
    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x * x - x + 1;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter2 = 100;
    double x02 = -2;
    double f02 = -9;
    double x12 = 2;
    double f12 = 7;
    assertTrue(solver.findRoot(function2, checker2, maxIter2, x02, f02, x12, f12));
    assertEquals(0.694, solver.getRoot(), 0.001);
    
    // Test case 3
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter3 = 100;
    double x03 = 0;
    double f03 = 0;
    double x13 = 2 * Math.PI;
    double f13 = 0;
    assertTrue(solver.findRoot(function3, checker3, maxIter3, x03, f03, x13, f13));
    assertEquals(0, solver.getRoot(), 0.001);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter1 = 100;
    double x01 = 0;
    double f01 = -4;
    double x11 = 3;
    double f11 = 5;
    assertTrue(solver.findRoot(function1, checker1, maxIter1, x01, f01, x11, f11));
    assertEquals(2, solver.getRoot(), 0.001);
    
    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x * x - x + 1;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter2 = 100;
    double x02 = -1;
    double f02 = 1;
    double x12 = 1;
    double f12 = 1;
    assertTrue(solver.findRoot(function2, checker2, maxIter2, x02, f02, x12, f12));
    assertEquals(-0.682, solver.getRoot(), 0.001);
    
    // Test case 3
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter3 = 100;
    double x03 = -2 * Math.PI;
    double f03 = 0;
    double x13 = -Math.PI;
    double f13 = 0;
    assertTrue(solver.findRoot(function3, checker3, maxIter3, x03, f03, x13, f13));
    assertEquals(-3.142, solver.getRoot(), 0.001);
  }
  
}