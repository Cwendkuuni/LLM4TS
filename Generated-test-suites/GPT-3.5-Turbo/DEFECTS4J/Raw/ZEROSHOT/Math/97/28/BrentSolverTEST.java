import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {
  
  private BrentSolver brentSolver;
  
  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return -3 * x + 2;
      }
    };
    
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    
    assertTrue(brentSolver.findRoot(function1, checker1, 10, -10, -28, 10, 22));
    assertEquals(2, brentSolver.getRoot(), 1e-6);
    
    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    
    assertTrue(brentSolver.findRoot(function2, checker2, 10, -Math.PI/2, -1, Math.PI/2, 1));
    assertEquals(0, brentSolver.getRoot(), 1e-6);
  }
}