import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRoot() {
    // Add test cases for findRoot method here
  }

  @Test
  public void testGetRoot() {
    // Add test cases for getRoot method here
  }

  // Add additional test cases here for edge cases and error scenarios

}