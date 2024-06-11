import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private double epsilon = Math.pow(2.0, -52);

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot() throws FunctionException {
    // Create a simple linear function f(x) = 2x - 1
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 2 * x - 1;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fLow - fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;

    boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(found);
    assertEquals(0.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootNoRoot() throws FunctionException {
    // Create a function with no root f(x) = x^2 + 1
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x + 1;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fLow - fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    int maxIter = 100;
    double x0 = 0.0;
    double f0 = 1.0;
    double x1 = 1.0;
    double f1 = 2.0;

    boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(found);
  }

  // Add more test cases to cover edge cases and exception scenarios

}