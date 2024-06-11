import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRoot() {
    // Create a sample function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Create a sample convergence checker for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return x1 > x2 ? ConvergenceChecker.HIGH : ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      boolean rootFound = solver.findRoot(function, checker, 100, 0.0, -4.0, 2.0, 0.0);
      assertTrue(rootFound);
      assertEquals(2.0, solver.getRoot(), 1e-10);
    } catch (FunctionException e) {
      fail("Exception should not have been thrown");
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  // Add additional test cases for edge cases and error scenarios

}