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
    // Test case for findRoot method
    // Typical use case
    // Create a sample function and convergence checker
    ComputableFunction function = new SampleFunction();
    ConvergenceChecker checker = new SampleConvergenceChecker();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }

  @Test
  public void testGetRoot() {
    // Test case for getRoot method
    // Create a sample function and convergence checker
    ComputableFunction function = new SampleFunction();
    ConvergenceChecker checker = new SampleConvergenceChecker();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    int maxIter = 100;
    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertEquals(solver.getRoot(), 0.0, 1e-15);
  }

  // Additional test cases for edge cases and exceptions can be added here

}