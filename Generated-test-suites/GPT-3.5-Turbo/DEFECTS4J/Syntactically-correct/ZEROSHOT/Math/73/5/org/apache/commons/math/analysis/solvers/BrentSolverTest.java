import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.HIGH;
        }
      }
    };

    boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 100, 96);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testGetRoot() throws Exception {
    BrentSolver solver = new BrentSolver();
    double root = solver.getRoot();
    assertEquals(Double.NaN, root, 0.0);
  }
}