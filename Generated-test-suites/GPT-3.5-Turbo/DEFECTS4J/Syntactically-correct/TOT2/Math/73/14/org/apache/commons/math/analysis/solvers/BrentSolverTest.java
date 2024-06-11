import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  private BrentSolver brentSolver;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
  }

  @Test
  public void testFindRoot() {
    // Typical use case
    assertEquals(true, brentSolver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return (fLow < 0 && fHigh > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
        }
    }, 100, 0, -4, 2, 0));
    
    // Edge case
    assertEquals(false, brentSolver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return (fLow < 0 && fHigh > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
        }
    }, 100, 0, 10, 2, 0));
    
    // Error scenario
    try {
        brentSolver.findRoot(null, null, 100, 0, -4, 2, 0);
        fail("Expected exception not thrown");
    } catch (FunctionException e) {
        // Expected exception
    }
  }

  @Test
  public void testGetRoot() {
    // Typical use case
    assertEquals(2.0, brentSolver.getRoot(), 0.0);
    
    // Edge case
    brentSolver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 2;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return (fLow < 0 && fHigh > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
        }
    }, 100, 0, -2, 2, 0);
    assertEquals(1.4142135623730951, brentSolver.getRoot(), 1e-15);
  }

}