import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRoot() {
    // Create a mock ComputableFunction for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // function x^2 - 4
      }
    };

    // Create a mock ConvergenceChecker for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.CONTINUE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, 1.0, -3.0, 2.0, 0.0);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  // Add more test methods for other public methods in BrentSolver class

}