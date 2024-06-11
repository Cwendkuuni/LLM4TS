import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new SampleFunction(), new SampleConvergenceChecker(), 100, -1.0, -0.5, 1.0, 0.5);
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-10);

    // Edge cases
    // Test when the interval bounds are the same
    rootFound = solver.findRoot(new SampleFunction(), new SampleConvergenceChecker(), 100, 1.0, 0.5, 1.0, 0.5);
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 1e-10);

    // Test with maxIter = 0
    rootFound = solver.findRoot(new SampleFunction(), new SampleConvergenceChecker(), 0, -1.0, -0.5, 1.0, 0.5);
    assertFalse(rootFound);
  }

  @Test
  public void testGetRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new SampleFunction(), new SampleConvergenceChecker(), 100, -1.0, -0.5, 1.0, 0.5);
    assertEquals(0.0, solver.getRoot(), 1e-10);
  }

  // Sample implementation of ComputableFunction for testing
  class SampleFunction implements ComputableFunction {
    public double valueAt(double x) {
      return x * x - 4;
    }
  }

  // Sample implementation of ConvergenceChecker for testing
  class SampleConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (fLow > 0 && fHigh < 0) {
        return ConvergenceChecker.LOW;
      } else if (fLow < 0 && fHigh > 0) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NONE;
      }
    }
  }

}