import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  // Test constructor
  @Test
  public void testBrentSolverConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  // Test findRoot method with a basic case
  @Test
  public void testFindRootBasicCase() {
    // Define a simple linear function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, 0, -5, 10, 5);

    assertTrue(rootFound);
    assertEquals(5.0, solver.getRoot(), 1e-6);
  }

  // Add more test cases here for other methods and scenarios

}