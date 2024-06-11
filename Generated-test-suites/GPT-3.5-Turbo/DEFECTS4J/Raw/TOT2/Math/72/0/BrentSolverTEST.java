import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create a sample function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Create a checker for convergence
    ConvergenceChecker checker = new ConvergenceChecker();

    // Create a BrentSolver instance
    BrentSolver solver = new BrentSolver();

    // Test finding the root in the interval [1, 3]
    boolean rootFound = solver.findRoot(function, checker, 100, 1, -3, 3, 5);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testGetRoot() {
    // Create a BrentSolver instance
    BrentSolver solver = new BrentSolver();

    // Set a root value
    solver.root = 5.0;

    // Test getting the root value
    assertEquals(5.0, solver.getRoot(), 0.0001);
  }
}