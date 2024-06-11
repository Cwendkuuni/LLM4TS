import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double f0 = Math.sin(x0);
    double x1 = 1.0;
    double f1 = Math.sin(x1);
    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
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
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRoot_NoRoot() {
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = Math.sin(x0);
    double x1 = 1.0;
    double f1 = Math.sin(x1);
    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
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
    assertFalse(rootFound);
    assertTrue(Double.isNaN(solver.getRoot()));
  }
  
  @Test
  public void testFindRoot_ConvergenceCheckerLow() {
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return LOW;
      }
    }, 100, 0.0, Math.sin(0.0), 1.0, Math.sin(1.0));
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRoot_ConvergenceCheckerHigh() {
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return HIGH;
      }
    }, 100, 0.0, Math.sin(0.0), 1.0, Math.sin(1.0));
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRoot_Exception() {
    BrentSolver solver = new BrentSolver();
    try {
      boolean rootFound = solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          throw new FunctionException("Exception");
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
      }, 100, -1.0, Math.sin(-1.0), 1.0, Math.sin(1.0));
      assertFalse(rootFound);
      assertTrue(Double.isNaN(solver.getRoot()));
    } catch (FunctionException e) {
      fail("Exception thrown");
    }
  }
}