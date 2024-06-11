import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test typical use case
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-10) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-10) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }, 100, 1.0, -3.0, 2.0, 4.0);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testGetRoot() {
    // Test typical use case
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-10) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-10) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }, 100, 1.0, -3.0, 2.0, 4.0);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootEdgeCases() {
    // Test edge cases
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-10) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-10) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }, 100, 0.0, 0.0, 1.0, 1.0);
    assertFalse(rootFound);
    assertEquals(Double.NaN, solver.getRoot(), 1e-10);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    // Test exception handling
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            if (x == 0) {
                throw new FunctionException("Division by zero");
            }
            return 1.0 / x;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-10) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-10) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }, 100, 1.0, 1.0, 2.0, 0.5);
  }

}