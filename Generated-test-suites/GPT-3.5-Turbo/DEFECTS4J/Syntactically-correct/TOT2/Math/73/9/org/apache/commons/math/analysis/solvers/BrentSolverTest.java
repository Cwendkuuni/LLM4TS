import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot() {
    // Test normal functionality
    // Create a sample function with known root in the interval
    ComputableFunction function = x -> x - 5;
    ConvergenceChecker checker = (xLow, fLow, xHigh, fHigh) -> {
      if (Math.abs(fHigh) < 1e-6) {
        return ConvergenceChecker.HIGH;
      }
      return ConvergenceChecker.ONGOING;
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10; 
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(5.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootWithInvalidInput() {
    // Test scenario with invalid input for the function
    // Create a function that throws an exception
    ComputableFunction function = x -> {
      throw new FunctionException("Invalid input");
    };
    ConvergenceChecker checker = (xLow, fLow, xHigh, fHigh) -> ConvergenceChecker.ONGOING;
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10; 
    double f1 = function.valueAt(x1);

    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Exception caught as expected
    }
  }

  // Add more test cases for other methods as needed

}