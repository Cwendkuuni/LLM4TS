import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1: Basic functionality test
    // Test with a linear function that has a root at x=0
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return HIGH;
        } else {
          return CONTINUE;
        }
      }
    };
    int maxIter1 = 100;
    double x0 = -1.0;
    double f0 = function1.valueAt(x0);
    double x1 = 1.0;
    double f1 = function1.valueAt(x1);
    boolean result1 = solver.findRoot(function1, checker1, maxIter1, x0, f0, x1, f1);
    assertTrue(result1);
    assertEquals(0.0, solver.getRoot(), epsilon);
    
    // Test case 2: Edge case test
    // Test with a function that has a root at a very small value
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x) - 1.0;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return HIGH;
        } else {
          return CONTINUE;
        }
      }
    };
    int maxIter2 = 100;
    double x2 = -1.0;
    double f2 = function2.valueAt(x2);
    double x3 = 1.0;
    double f3 = function2.valueAt(x3);
    boolean result2 = solver.findRoot(function2, checker2, maxIter2, x2, f2, x3, f3);
    assertTrue(result2);
    assertEquals(0.0, solver.getRoot(), 0.0001);
    
    // Test case 3: Exception handling test
    // Test with a function that throws an exception
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function evaluation error");
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // This convergence checker will never return a converged result
        // since the function always throws an exception
        return CONTINUE;
      }
    };
    int maxIter3 = 100;
    double x4 = -1.0;
    double f4 = Double.NaN; // Function evaluation error
    double x5 = 1.0;
    double f5 = Double.NaN; // Function evaluation error
    boolean result3 = solver.findRoot(function3, checker3, maxIter3, x4, f4, x5, f5);
    assertFalse(result3);
  }
}