import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() throws FunctionException {
    // Initialize test inputs
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // Example function: x^2 - 4
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH; // Convergence at upper bound
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW; // Convergence at lower bound
        } else {
          return ConvergenceChecker.NONE; // No convergence yet
        }
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 5;
    double f1 = function.valueAt(x1);
    
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();
    
    // Test findRoot method
    boolean success = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assert(success); // The function should converge to a root
    assert(Math.abs(solver.getRoot() - 2) < 1e-6); // The root should be close to 2
  }
  
  @Test
  public void testFindRootNoConvergence() throws FunctionException {
    // Initialize test inputs
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x); // Example function: sin(x)
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE; // No convergence
      }
    };
    int maxIter = 10;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 5;
    double f1 = function.valueAt(x1);
    
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();
    
    // Test findRoot method
    boolean success = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assert(!success); // The function should not converge within the given iterations
    assert(Double.isNaN(solver.getRoot())); // The root should be NaN
  }
  
  // Add additional test cases for potential edge cases and exception handling scenarios
  
}