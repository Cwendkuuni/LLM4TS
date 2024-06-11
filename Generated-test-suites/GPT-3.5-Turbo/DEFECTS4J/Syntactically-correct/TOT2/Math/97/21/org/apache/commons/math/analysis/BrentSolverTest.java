import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver brentSolver;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
  }

  @Test
  public void testFindRoot() {
    // Typical test case
    double expectedResult = 3.0;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 9;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };

    boolean result = brentSolver.findRoot(function, checker, 100, 0, -9, 5, 16);
    assertTrue(result);
    assertEquals(expectedResult, brentSolver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_edgeCases() {
    // Testing edge cases
    // Edge case: Maximal number of iterations reached
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 9;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };

    boolean result = brentSolver.findRoot(function, checker, 1, 0, -9, 5, 16);
    assertFalse(result);

    // Edge case: x0 equals x1
    result = brentSolver.findRoot(function, checker, 100, 5, 16, 5, 16);
    assertTrue(result);
    assertEquals(5, brentSolver.getRoot(), 0.001);
  }

  @Test
  public void testGetRoot() {
    // Typical test case for getRoot method
    double expectedResult = 3.0;
    brentSolver.getRoot();
    assertEquals(expectedResult, brentSolver.getRoot(), 0.001);
  }

}