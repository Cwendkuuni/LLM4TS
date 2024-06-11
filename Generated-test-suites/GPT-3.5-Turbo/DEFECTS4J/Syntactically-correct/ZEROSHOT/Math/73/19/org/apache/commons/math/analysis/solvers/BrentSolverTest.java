import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // create a new BrentSolver instance
    BrentSolver solver = new BrentSolver();

    // create a dummy function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // create a dummy convergence checker for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // set up initial values for testing
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    // call findRoot method and check the output
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  // Add more test cases for other methods if necessary

}