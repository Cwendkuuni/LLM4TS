import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
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
    boolean result = solver.findRoot(function, checker, 100, 0.0, function.valueAt(0.0),
        10.0, function.valueAt(10.0));

    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.root = 5.0;

    double result = solver.getRoot();
    assertEquals(5.0, result, 0.0);
  }

}