import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create an instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Test when the function converges to a root in the given interval
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));

    // Test an edge case when the function does not converge
    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }

  @Test
  public void testGetRoot() {
    // Create an instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Test getting the root when a root is found
    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);

    // Test getting the root when no root is found
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

}