import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(new ComputableFunctionMock(), new ConvergenceCheckerMock(), 100, 1.0, 0.0, 2.0, 0.0);
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 1e-15);
    
    // Edge case: FunctionException thrown
    try {
      solver.findRoot(new ComputableFunctionMock(), new ConvergenceCheckerMock(), 100, 1.0, 0.0, 2.0, Double.NaN);
      fail("FunctionException expected");
    } catch (FunctionException e) {
      // Expected exception
    }

    // Edge case: Max iterations reached without finding root
    rootFound = solver.findRoot(new ComputableFunctionMock(), new ConvergenceCheckerMock(), 5, 0.0, 0.0, 2.0, 0.0);
    assertFalse(rootFound);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  // Mock classes for testing
  private class ComputableFunctionMock implements ComputableFunction {
    @Override
    public double valueAt(double x) throws FunctionException {
      if (x == Double.NaN) {
        throw new FunctionException("Invalid input");
      }
      return x * x - 1;
    }
  }

  private class ConvergenceCheckerMock implements ConvergenceChecker {
    @Override
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 1e-15) {
        return ConvergenceChecker.LOW;
      } else if (Math.abs(fHigh) < 1e-15) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NONE;
      }
    }
  }
}