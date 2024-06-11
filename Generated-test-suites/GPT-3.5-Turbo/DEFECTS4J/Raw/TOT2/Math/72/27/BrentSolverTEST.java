import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;


public class BrentSolverTest {

  @Test
  public void testFindRoot() {

    BrentSolver solver = new BrentSolver();

    // Test case for finding root in a valid interval
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 4; // function that has root at x = 2
      }
    };

    try {
      boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, 1, -3, 5, 21);
      assertTrue(rootFound);
      assertEquals(2.0, solver.getRoot(), 0.0001); // assert that the root is found at x = 2.0
    } catch (FunctionException e) {
      fail("Exception occurred during root finding");
    }

    // Test case for finding root with invalid interval
    try {
      boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, 5, -3, 7, 21);
      assertFalse(rootFound);
    } catch (FunctionException e) {
      fail("Exception occurred during root finding");
    }

  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0001); // assert initial root value is NaN
  }

}