import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create a BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Define the function and checker
    ComputableFunction function = new MyFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();

    // Test case 1
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    boolean result1 = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(result1);
    assertEquals(0.5, solver.getRoot(), 1e-8);

    // Test case 2
    double x2 = -1.0;
    double f2 = function.valueAt(x2);
    double x3 = 0.0;
    double f3 = function.valueAt(x3);
    boolean result2 = solver.findRoot(function, checker, 100, x2, f2, x3, f3);
    assertTrue(result2);
    assertEquals(-0.5, solver.getRoot(), 1e-8);
  }

  private class MyFunction implements ComputableFunction {
    public double valueAt(double x) {
      return x * x - 4;
    }
  }

  private class MyConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 1e-8) {
        return ConvergenceChecker.LOW;
      } else if (Math.abs(fHigh) < 1e-8) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    }
  }

}