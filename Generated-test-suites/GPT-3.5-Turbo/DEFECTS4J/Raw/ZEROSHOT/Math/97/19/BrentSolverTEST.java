import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return LOW;
        } else if (Math.abs(x2 - x1) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    boolean result = solver.findRoot(function, checker, 1000, 1, -1, 2, 2);
    assertTrue(result);
    assertEquals(Math.sqrt(2), solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = solver.getRoot();
    assertTrue(Double.isNaN(root));
  }

}