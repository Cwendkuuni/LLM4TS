import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  // Test findRoot method
  @Test
  public void testFindRoot() {
    // Test with a basic linear function: f(x) = 2x - 1
    BrentSolver solver = new BrentSolver();
    double x0 = 0;
    double f0 = -1;
    double x1 = 1;
    double f1 = 1;
    boolean result = solver.findRoot(new LinearFunction(), new LinearConvergenceChecker(), 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(0.5, solver.getRoot(), 0.0001);

    // Test with a constant function: f(x) = 5
    solver = new BrentSolver();
    x0 = -10;
    f0 = 5;
    x1 = 10;
    f1 = 5;
    result = solver.findRoot(new ConstantFunction(), new ConstantConvergenceChecker(), 100, x0, f0, x1, f1);
    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 0.0001);

    // Test with a quadratic function: f(x) = x^2
    solver = new BrentSolver();
    x0 = 0;
    f0 = 0;
    x1 = 2;
    f1 = 4;
    result = solver.findRoot(new QuadraticFunction(), new QuadraticConvergenceChecker(), 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(0, solver.getRoot(), 0.0001);
  }
  
  // Test getRoot method
  @Test
  public void testGetRoot() {
    // Test when the root has not been found yet
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0001);

    // Test when the root has been found
    solver = new BrentSolver();
    solver.findRoot(new LinearFunction(), new LinearConvergenceChecker(), 100, 0, -1, 1, 1);
    assertEquals(0.5, solver.getRoot(), 0.0001);
  }

  // Additional test helper classes

  private class LinearFunction implements ComputableFunction {
    public double valueAt(double x) {
      return 2 * x - 1;
    }
  }

  private class LinearConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 0.0001) {
        return ConvergenceChecker.LOW;
      }
      if (Math.abs(fHigh) < 0.0001) {
        return ConvergenceChecker.HIGH;
      }
      return ConvergenceChecker.NONE;
    }
  }

  private class ConstantFunction implements ComputableFunction {
    public double valueAt(double x) {
      return 5;
    }
  }

  private class ConstantConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 0.0001) {
        return ConvergenceChecker.LOW;
      }
      if (Math.abs(fHigh) < 0.0001) {
        return ConvergenceChecker.HIGH;
      }
      return ConvergenceChecker.NONE;
    }
  }

  private class QuadraticFunction implements ComputableFunction {
    public double valueAt(double x) {
      return x * x;
    }
  }

  private class QuadraticConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 0.0001) {
        return ConvergenceChecker.LOW;
      }
      if (Math.abs(fHigh) < 0.0001) {
        return ConvergenceChecker.HIGH;
      }
      return ConvergenceChecker.NONE;
    }
  }

}