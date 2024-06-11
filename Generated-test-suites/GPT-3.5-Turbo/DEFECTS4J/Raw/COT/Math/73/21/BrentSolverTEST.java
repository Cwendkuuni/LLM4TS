import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Create a mock function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4;
      }
    };
    
    // Test finding the root in a known interval
    try {
      boolean result = solver.findRoot(function, null, 100, 1, -3, 3, 7);
      assertTrue(result);
      assertEquals(2.0, solver.getRoot(), 0.00001);
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
  }

  @Test
  public void testFindRoot_noRootInInterval() {
    BrentSolver solver = new BrentSolver();
    
    // Create a mock function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return (x-1)*(x-2);
      }
    };
    
    // Test finding the root in a known interval where no root exists
    try {
      boolean result = solver.findRoot(function, null, 100, 1, -3, 3, 7);
      assertFalse(result);
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
  }

  @Test
  public void testFindRoot_maxIterationsExceeded() {
    BrentSolver solver = new BrentSolver();
    
    // Create a mock function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4;
      }
    };
    
    // Test finding the root in a known interval with maximum iterations reached
    try {
      boolean result = solver.findRoot(function, null, 5, 1, -3, 3, 7);
      assertFalse(result);
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
  }

}