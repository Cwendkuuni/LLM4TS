
import org.junit.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver brentSolver = new BrentSolver();
    
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };
    
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 3.0;
    double f1 = 5.0;
    
    boolean result = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    Assert.assertTrue(result);
    Assert.assertEquals(2.0, brentSolver.getRoot(), 1e-6);
  }

  // Test for potential edge cases and exception handling scenarios
  // ...
  
}
