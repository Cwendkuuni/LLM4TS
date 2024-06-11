import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test finding root in a given interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new CustomFunction();
    ConvergenceChecker checker = new CustomConvergenceChecker();

    boolean rootFound = solver.findRoot(function, checker, 100, 1.0, 1.0, 2.0, -1.0);

    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testGetRoot() {
    // Test getting the abscissa of the root
    BrentSolver solver = new BrentSolver();

    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    // Test exception handling for finding root
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new CustomFunction();
    ConvergenceChecker checker = new CustomConvergenceChecker();

    solver.findRoot(function, checker, 100, 1.0, 1.0, 1.0, -3.0);
  }

  // Custom ComputableFunction implementation for testing
  private static class CustomFunction implements ComputableFunction {
    public double valueAt(double x) {
      return x * x - 4;
    }
  }

  // Custom ConvergenceChecker implementation for testing
  private static class CustomConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (fLow < 0 && fHigh > 0) {
        return LOW;
      } else if (fLow > 0 && fHigh < 0) {
        return HIGH;
      } else {
        return NEITHER;
      }
    }
  }

}