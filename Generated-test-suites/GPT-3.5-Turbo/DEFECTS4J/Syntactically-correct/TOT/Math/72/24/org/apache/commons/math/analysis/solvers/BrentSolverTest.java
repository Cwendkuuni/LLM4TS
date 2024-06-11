import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {
  
  private BrentSolver solver;
  
  @Before
  public void setUp() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot_withValidIntervalAndFunction_ReturnsTrueAndSetsRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 2 * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(0.5, solver.getRoot(), 0.0001);
  }
  
  @Test
  public void testFindRoot_withInvalidInterval_ReturnsFalseAndNoRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    };
    int maxIter = 100;
    double x0 = 1.0;
    double f0 = 0.0;
    double x1 = 2.0;
    double f1 = 3.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertFalse(result);
    assertTrue(Double.isNaN(solver.getRoot()));
  }
  
  @Test(expected = FunctionException.class)
  public void testFindRoot_withInvalidFunction_ThrowsFunctionException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Invalid function");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = 0.0;
    double x1 = 1.0;
    double f1 = 1.0;
    
    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }
  
  @Test
  public void testFindRoot_withConvergingChecker_ReturnsTrueAndSetsRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < Math.abs(fHigh)) {
          return 1;
        } else if (Math.abs(fLow) > Math.abs(fHigh)) {
          return -1;
        } else {
          return 0;
        }
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.0001);
  }
  
  @Test
  public void testFindRoot_withMaxIterationsReached_ReturnsFalseAndNoRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    };
    int maxIter = 5;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertFalse(result);
    assertTrue(Double.isNaN(solver.getRoot()));
  }
}