import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();

    // Test case 1
    double tol = 1e-10;
    int maxIter = 1000;
    double x0 = -10.0;
    double f0 = 100.0;
    double x1 = 10.0;
    double f1 = -100.0;

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 16.0;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < tol) {
          return HIGH;
        } else if (Math.abs(fLow) < tol) {
          return LOW;
        } else {
          return NONE;
        }
      }
    };

    try {
      boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertTrue(rootFound);
      assertEquals(4.0, solver.getRoot(), tol);
    } catch (FunctionException e) {
      fail("Unexpected exception thrown");
    }

    // Test case 2
    // ...

    // Additional test cases can be added
  }
}