import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    
    boolean rootFound = solver.findRoot(new TestFunction(), new TestConvergenceChecker(), 100, 0.0, -1.0, 3.0, 1.0);
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 1e-10);
    
    // Edge case: Function is zero at initial interval
    rootFound = solver.findRoot(new TestFunction(), new TestConvergenceChecker(), 100, -2.0, 0.0, 2.0, 0.0);
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-10);
    
    // Error scenario: Max iterations exceeded
    rootFound = solver.findRoot(new TestFunction(), new TestConvergenceChecker(), 3, -2.0, -1.0, 2.0, 1.0);
    assertFalse(rootFound);
  }

  // Helper class for testing
  private class TestFunction implements ComputableFunction {
    public double valueAt(double x) {
      return x*x - 4.0;
    }
  }

  // Helper class for testing
  private class TestConvergenceChecker implements ConvergenceChecker {
    public int converged(double x0, double f0, double x1, double f1) {
      if (Math.abs(f0) < 1e-10) {
        return LOW;
      } else if (Math.abs(f1) < 1e-10) {
        return HIGH;
      } else {
        return UNCONVERGED;
      }
    }
  }
}