import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {
  
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }
  
  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    // Add test cases here for findRoot method
  }
  
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    // Add test cases here for getRoot method
  }
  
}