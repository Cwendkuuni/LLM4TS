import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot() throws FunctionException {
    // Two methods with possible edge cases and exceptions:
    // findRoot(ComputableFunction function, ConvergenceChecker checker, int maxIter, double x0, double f0, double x1, double f1)
    // getRoot()

    // Test case 1: Basic test case with function that has a root in the interval
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 16;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NO_CONVERGENCE;
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -16.0;
    double x1 = 5.0;
    double f1 = 9.0;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(4.0, solver.getRoot(), 1e-6);

    // Test case 2: Function with no root in the interval
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.cos(x) - 1.0;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NO_CONVERGENCE;
      }
    };
    double x2 = 0.0;
    double f2 = 0.0;
    double x3 = 2.0;
    double f3 = -1.0;
    assertFalse(solver.findRoot(function2, checker2, maxIter, x2, f2, x3, f3));

    // Test case 3: Function with multiple roots in the interval
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * (x - 1.0) * (x - 2.0);
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NO_CONVERGENCE;
      }
    };
    double x4 = 0.0;
    double f4 = 0.0;
    double x5 = 3.0;
    double f5 = 6.0;
    assertTrue(solver.findRoot(function3, checker3, maxIter, x4, f4, x5, f5));
    assertEquals(1.0, solver.getRoot(), 1e-6);

    // Test case 4: Function with exception thrown during computation
    ComputableFunction function4 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Error in function computation");
      }
    };
    ConvergenceChecker checker4 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NO_CONVERGENCE;
      }
    };
    double x6 = 0.0;
    double f6 = 0.0;
    double x7 = 1.0;
    double f7 = 1.0;
    try {
      solver.findRoot(function4, checker4, maxIter, x6, f6, x7, f7);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Exception expected
    }

    // Test case 5: Function with extremely large number of iterations
    ComputableFunction function5 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 16;
      }
    };
    ConvergenceChecker checker5 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NO_CONVERGENCE;
      }
    };
    int maxIter5 = Integer.MAX_VALUE;
    double x8 = 0.0;
    double f8 = -16.0;
    double x9 = 5.0;
    double f9 = 9.0;
    assertFalse(solver.findRoot(function5, checker5, maxIter5, x8, f8, x9, f9));

    // Add more test cases and edge cases for each method as needed

  }

}