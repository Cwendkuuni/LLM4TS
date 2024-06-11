import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    double expected = 0.0;
    double result = solver.getRoot();
    assertEquals(expected, result, 0.0);
  }
  
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double expected = Double.NaN;
    double result = solver.getRoot();
    assertEquals(expected, result, 0.0);
  }

}