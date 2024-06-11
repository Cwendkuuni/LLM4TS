import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_validInput() {
    // Test valid input with a known root in the interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    boolean success = solver.findRoot(function, checker, 100, 0, -4, 5, 21);
    assertTrue(success);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_invalidInput() {
    // Test invalid input where the function does not have a root in the interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x + 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    boolean success = solver.findRoot(function, checker, 100, 0, 1, 2, 5);
    assertFalse(success);
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testFindRoot_boundaryInput() {
    // Test boundary input with the root at the lower bound of the interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    boolean success = solver.findRoot(function, checker, 100, 0, -1, 2, 1);
    assertTrue(success);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_maxIterationsExceeded() {
    // Test case where the maximum number of iterations is exceeded
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    boolean success = solver.findRoot(function, checker, 10, 0, 1, 2, 3);
    assertFalse(success);
    assertTrue(Double.isNaN(solver.getRoot()));
  }

}