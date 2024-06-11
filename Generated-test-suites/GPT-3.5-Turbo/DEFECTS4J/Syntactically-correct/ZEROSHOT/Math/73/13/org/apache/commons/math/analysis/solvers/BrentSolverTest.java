import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    boolean foundRoot = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(foundRoot);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = 2.0;
    solver.root = root;
    assertEquals(root, solver.getRoot(), 0.0);
  }

}