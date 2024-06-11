import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() throws FunctionException {
    // Create a test instance of the BrentSolver class
    BrentSolver solver = new BrentSolver();
    
    // Create a mock function for testing purposes
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    
    // Create a mock convergence checker for testing purposes
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    // Set the initial interval bounds and function values
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);
    
    // Call the findRoot method of BrentSolver
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    // Check that a root was found and that the value is close to 2
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  // Additional test cases for edge cases and exceptions:
  
  @Test
  public void testFindRoot_IntervalContainsRoot() throws FunctionException {
    // Create a test instance of the BrentSolver class
    BrentSolver solver = new BrentSolver();
    
    // Create a mock function for testing purposes
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    
    // Create a mock convergence checker for testing purposes
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    // Set the initial interval bounds and function values
    double x0 = 1.5;
    double f0 = function.valueAt(x0);
    double x1 = 2.5;
    double f1 = function.valueAt(x1);
    
    // Call the findRoot method of BrentSolver
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    // Check that a root was found and that the value is close to 2
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRoot_IntervalDoesNotContainRoot() throws FunctionException {
    // Create a test instance of the BrentSolver class
    BrentSolver solver = new BrentSolver();
    
    // Create a mock function for testing purposes
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    
    // Create a mock convergence checker for testing purposes
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    // Set the initial interval bounds and function values
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    
    // Call the findRoot method of BrentSolver
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    // Check that a root was not found
    assertFalse(rootFound);
    assertTrue(Double.isNaN(solver.getRoot()));
  }
}