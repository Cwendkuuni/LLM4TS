import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Typical use case
    // Create a sample function and test finding the root in the given interval
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4; // f(x) = x^2 - 4, root at x = 2
      }
    };
    assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, 1.0, 1.0, 3.0, 9.0));
    
    // Edge cases
    // Test with interval outside the root
    assertFalse(solver.findRoot(function, new ConvergenceChecker(), 100, -10.0, 100.0, -1.0, 16.0));
    
    // Test with very high number of iterations
    assertTrue(solver.findRoot(function, new ConvergenceChecker(), 1000000, 1.0, 1.0, 3.0, 9.0));
    
    // Error scenarios
    // Test with a function that throws an exception
    ComputableFunction exceptionFunction = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function error");
      }
    };
    try {
      solver.findRoot(exceptionFunction, new ConvergenceChecker(), 100, 1.0, 1.0, 3.0, 9.0);
      fail("Exception not thrown");
    } catch (FunctionException e) {
      // Exception caught, test passed
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Typical use case
    // Ensure the root value is returned correctly
    solver.findRoot(new ComputableFunction() { public double valueAt(double x) { return x - 2.0; } }, new ConvergenceChecker(), 100, 1.0, -1.0, 3.0, 1.0);
    assertEquals(2.0, solver.getRoot(), 1e-6);
    
    // Edge cases
    // Test with negative root value
    solver.findRoot(new ComputableFunction() { public double valueAt(double x) { return x * x - 9.0; } }, new ConvergenceChecker(), 100, 1.0, -8.0, 3.0, 0.0);
    assertEquals(-3.0, solver.getRoot(), 1e-6);
  }

}