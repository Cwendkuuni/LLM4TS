import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

  @Test
  public void testDoOptimize() {
    // Test basic functionality of doOptimize method
    // TODO: Add test code here
  }
  
  @Test(expected = NullArgumentException.class)
  public void testDoOptimizeNullChecker() {
    // Test exception for null convergence checker
    // TODO: Add test code here
  }

  @Test
  public void testDoOptimizeConvergence() {
    // Test convergence of doOptimize method
    // TODO: Add test code here
  }

  @Test(expected = ConvergenceException.class)
  public void testDoOptimizeSingularMatrix() {
    // Test exception for singular matrix
    // TODO: Add test code here
  }

}