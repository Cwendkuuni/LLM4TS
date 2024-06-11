
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
  
  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
    
    // Test case 1
    double min = optimizer.doOptimize();
    assertEquals(expectedValue, min, 0.001);
    
    // Test case 2
    // ...
  }
  
  // ... create test methods for other public methods in BrentOptimizer
  
}

