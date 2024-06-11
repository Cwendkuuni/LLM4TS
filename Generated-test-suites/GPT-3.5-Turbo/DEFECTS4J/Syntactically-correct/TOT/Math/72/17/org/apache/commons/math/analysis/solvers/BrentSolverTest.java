import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
// Please add necessary imports

public class BrentSolverTest {

  @Test
  public void testFindRoot_validInput_success() {
    // Test case for a valid input where a root is expected to be found
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
    }};
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 1000;
    double x0 = -5.0;
    double f0 = 21.0;
    double x1 = 5.0;
    double f1 = -19.0;

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 0.0);
  }
  
  @Test
  public void testFindRoot_noRootInInterval_returnFalse() {
    // Test case where there is no root in the given interval
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x + 1; // No roots for this quadratic equation
    }};
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 1000;
    double x0 = -5.0;
    double f0 = 26.0;
    double x1 = 5.0;
    double f1 = 26.0;

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }
  
  @Test
  public void testFindRoot_maximumIterationsExceeded_returnFalse() {
    // Test case where the maximum number of iterations is exceeded
     ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 2 * x;
    }};
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 10;
    double x0 = -5.0;
    double f0 = -10.0;
    double x1 = 5.0;
    double f1 = 10.0;

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }
  
  // Please add additional test cases for other methods
  
}