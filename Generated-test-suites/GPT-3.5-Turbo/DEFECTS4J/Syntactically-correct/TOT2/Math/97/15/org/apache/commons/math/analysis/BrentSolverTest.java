import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test for typical use case
    // Function with a root in the interval [1, 2]
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 3;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    boolean rootFound = solver.findRoot(function, checker, 100, 1, -2, 2, 1);
    assertTrue(rootFound);
    assertEquals(1.7320508075688772, solver.getRoot(), 1e-10);
    
    // Test for edge case
    // Function with no root in the given interval [0, 1]
    ComputableFunction noRootFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x + 3;
      }
    };
    boolean noRootFound = solver.findRoot(noRootFunction, checker, 100, 0, 3, 1, 4);
    assertFalse(noRootFound);
    assertEquals(Double.NaN, solver.getRoot(), 0);
    
    // Test exception handling
    // Function with an error that should throw a FunctionException
    ComputableFunction errorFunction = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Error");
      }
    };
    try {
      solver.findRoot(errorFunction, checker, 100, 0, 0, 1, 1);
      fail("FunctionException not thrown");
    } catch (FunctionException e) {
      // Exception thrown as expected
    }
  }
  
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test for typical use case with a known root
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker(), 100, 1, -3, 2, 4);
    assertEquals(2.0, solver.getRoot(), 0);
  }

}