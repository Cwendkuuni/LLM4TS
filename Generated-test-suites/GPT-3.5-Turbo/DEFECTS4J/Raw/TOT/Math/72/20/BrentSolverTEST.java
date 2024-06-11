import org.junit.*;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Typical use case
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.ROOT_FOUND;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = -4;
    double x1 = 3;
    double f1 = 5;
    
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(2, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootWithNegativeRoot() throws FunctionException {
    // Edge case - root is negative
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.ROOT_FOUND;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = -5;
    double f0 = 21;
    double x1 = 3;
    double f1 = 5;
    
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(-2, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootNoRootFound() throws FunctionException {
    // Error scenario - no root found within the given interval and tolerance
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.0001) {
          return ConvergenceChecker.ROOT_FOUND;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = -2.5;
    double f0 = 0.25;
    double x1 = -1.5;
    double f1 = -0.75;
    
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertFalse(result);
  }

  @Test
  public void testFindRootWithStepConvergenceChecker() throws FunctionException {
    // Typical use case - using a custom convergence checker
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh - fLow) < 0.0001) {
          return ConvergenceChecker.ROOT_FOUND;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = -5;
    double f0 = 21;
    double x1 = 3;
    double f1 = 5;
    
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(-2, solver.getRoot(), 0.0001);
  }

}