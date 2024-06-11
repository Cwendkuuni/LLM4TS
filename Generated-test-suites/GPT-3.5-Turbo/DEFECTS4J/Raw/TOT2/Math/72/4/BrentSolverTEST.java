import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Test input values
    double x0 = -1.0;
    double f0 = -2.0;
    double x1 = 1.0;
    double f1 = 2.0;

    // Test the findRoot method
    boolean rootFound = solver.findRoot(f -> f * f - 4, (a, b, c, d) -> 0, 100, x0, f0, x1, f1);

    // Check if root is found
    assertTrue(rootFound);

    // Check the root value
    assertEquals(2.0, solver.getRoot(), 0.01);
  }

  @Test
  public void testGetRoot() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Set root value
    solver.root = 3.0;

    // Check getRoot method
    assertEquals(3.0, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRootNoRootInInterval() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Test input values
    double x0 = 1.0;
    double f0 = -2.0;
    double x1 = 3.0;
    double f1 = -2.0;

    // Test the findRoot method with interval that does not contain a root
    boolean rootFound = solver.findRoot(f -> f * f - 4, (a, b, c, d) -> 0, 100, x0, f0, x1, f1);

    // Check if root is not found
    assertFalse(rootFound);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Test input values
    double x0 = -1.0;
    double f0 = -2.0;
    double x1 = 1.0;
    double f1 = 2.0;

    // Test the findRoot method with a function that throws an exception
    solver.findRoot(f -> { throw new FunctionException("Function exception"); }, (a, b, c, d) -> 0, 100, x0, f0, x1, f1);
  }

}