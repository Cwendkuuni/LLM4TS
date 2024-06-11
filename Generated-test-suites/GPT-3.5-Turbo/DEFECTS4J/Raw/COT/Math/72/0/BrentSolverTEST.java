import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  // Test findRoot method
  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    double x0 = 0;
    double f0 = 1;
    double x1 = 2;
    double f1 = -1;
    double expectedRoot = 1;
    boolean result = solver.findRoot(new DummyFunction(), new DummyConvergenceChecker(), 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  // Test getRoot method
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double expectedRoot = Double.NaN;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  // Dummy implementation of ComputableFunction interface for testing
  private class DummyFunction implements ComputableFunction {
    public double valueAt(double x) throws FunctionException {
      return 0;
    }
  }

  // Dummy implementation of ConvergenceChecker interface for testing
  private class DummyConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      return ConvergenceChecker.LOW;
    }
  }

}