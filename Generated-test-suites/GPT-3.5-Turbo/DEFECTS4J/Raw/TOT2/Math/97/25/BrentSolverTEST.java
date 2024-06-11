import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Initialize the BrentSolver
    BrentSolver solver = new BrentSolver();
    // Create a simple linear function to test
    ComputableFunction linearFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return 2*x - 3;
      }
    };

    // Test when the function contains the root in the interval
    assertTrue(solver.findRoot(linearFunction, new ConvergenceChecker(), 100, 1, -1, 3, 3));
    assertEquals(1.5, solver.getRoot(), 1e-6);
    
    // Test when the function does not contain the root in the interval
    assertFalse(solver.findRoot(linearFunction, new ConvergenceChecker(), 100, 1, 1, 3, 5));
  }

  @Test
  public void testGetRoot() {
    // Initialize the BrentSolver
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    // Initialize the BrentSolver
    BrentSolver solver = new BrentSolver();
    // Create a function that always throws an exception
    ComputableFunction exceptionFunction = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Exception");
      }
    };
    // Call findRoot with the exception function
    solver.findRoot(exceptionFunction, new ConvergenceChecker(), 100, 1, -1, 3, 3);
  }

  // Add more test cases for other methods as needed...

}