import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  // Test case for the findRoot method

  // Typical use case: Function has a root within the given interval
  @Test
  public void testFindRoot_typical() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    int maxIter = 100;

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(5.0, solver.getRoot(), 0.0001);
  }

  // Edge case: Function has multiple roots within the given interval
  @Test
  public void testFindRoot_multipleRoots() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return (x - 5) * (x + 5);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE; // No convergence check required for this test case
      }
    };

    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    int maxIter = 100;

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot()) < 0.0001);
  }

  // Error scenario: Maximal number of iterations exceeded
  @Test
  public void testFindRoot_maxIterationsExceeded() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE; // No convergence check required for this test case
      }
    };

    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    int maxIter = 1; // Set a small number of iterations

    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }

}