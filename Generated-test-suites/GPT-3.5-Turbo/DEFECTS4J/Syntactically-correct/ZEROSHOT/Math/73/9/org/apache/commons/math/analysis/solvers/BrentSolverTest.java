import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() throws FunctionException {
    // Create the BrentSolver object
    BrentSolver solver = new BrentSolver();
    
    // Create a dummy function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4;
      }
    };
    
    // Create a convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    // Set the maximum number of iterations
    int maxIter = 100;
    
    // Set the interval boundaries and function values
    double x0 = -2.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    
    // Find the root
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    // Check if the root was found
    assertTrue(rootFound);
    
    // Check the value of the root
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
}