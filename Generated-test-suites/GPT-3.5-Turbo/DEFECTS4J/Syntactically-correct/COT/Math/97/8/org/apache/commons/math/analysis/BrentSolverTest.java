import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigher) < 1e-6) {
          return HIGH;
        } else {
          return 0;
        }
      }
    }, 100, 0, -4, 3, 5);
    assertTrue(result);
    assertEquals(2, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootWithException() {
    BrentSolver solver = new BrentSolver();
    try {
      boolean result = solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          throw new FunctionException("Function evaluation failed");
        }
      }, new ConvergenceChecker() {
        public int converged(double xLower, double fLower, double xHigher, double fHigher) {
          return 0;
        }
      }, 100, 0, -4, 3, 5);
      assertFalse(result);
    } catch (FunctionException e) {
      assertEquals("Function evaluation failed", e.getMessage());
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }

}