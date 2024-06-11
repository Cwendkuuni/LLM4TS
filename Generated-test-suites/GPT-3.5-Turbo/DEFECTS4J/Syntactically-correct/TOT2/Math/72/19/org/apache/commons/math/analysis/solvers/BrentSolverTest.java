import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();

    // Test when a root is found in the given interval
    double expectedRoot = 2.0;
    double x0 = 1.0;
    double f0 = -1.0;
    double x1 = 3.0;
    double f1 = 2.0;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 2.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return ConvergenceChecker.LOW;
      }
    };
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();

    // Test getting the abscissa of the root
    double expectedRoot = Double.NaN;
    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

}