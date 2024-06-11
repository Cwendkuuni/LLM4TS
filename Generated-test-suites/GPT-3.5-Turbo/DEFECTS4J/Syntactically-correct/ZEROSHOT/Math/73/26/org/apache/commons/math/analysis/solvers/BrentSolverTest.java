import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot() {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) <= 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) <= 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    boolean rootFound = solver.findRoot(function, checker, 100, -2, -8, 2, 8);

    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  // Add more test cases here

}