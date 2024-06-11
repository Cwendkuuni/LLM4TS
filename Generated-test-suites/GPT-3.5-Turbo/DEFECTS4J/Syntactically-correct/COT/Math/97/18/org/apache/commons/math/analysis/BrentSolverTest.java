import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Create the BrentSolver instance
    BrentSolver solver = new BrentSolver();
    
    // Create a dummy ComputableFunction for testing
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x - 5;
      }
    };
    
    // Create a dummy ConvergenceChecker for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    
    // Set the input values
    int maxIter = 100;
    double x0 = 0;
    double f0 = -5;
    double x1 = 10;
    double f1 = 5;
    
    // Call the findRoot method
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    // Check the result
    assertTrue(result);
    assertEquals(5.0, solver.getRoot(), 1e-6);
  }
  
  // TODO: Write additional test cases for potential edge cases and exception handling scenarios
  
}