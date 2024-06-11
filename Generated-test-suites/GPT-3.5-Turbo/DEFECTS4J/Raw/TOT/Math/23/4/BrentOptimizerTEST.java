import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testDoOptimize() {
    // Test case #1
    BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
    optimizer.setMin(-10);
    optimizer.setMax(10);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertEquals(0.0, result.getPoint(), 1e-6);
    
    // Test case #2
    BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
    optimizer.setMin(0);
    optimizer.setMax(100);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertEquals(0.0, result.getPoint(), 1e-6);
    
    // Test case #3
    BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
    optimizer.setMin(-100);
    optimizer.setMax(0);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertEquals(0.0, result.getPoint(), 1e-6);
    
    // Test case #4
    BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
    optimizer.setMin(-100);
    optimizer.setMax(100);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertEquals(0.0, result.getPoint(), 1e-6);
  }
  
  @Test
  public void testBest() {
    // Test case #1
    UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 10.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 5.0);
    boolean isMinim = true;
    UnivariatePointValuePair result = best(a, b, isMinim);
    assertEquals(0.0, result.getPoint(), 1e-6);
    assertEquals(5.0, result.getValue(), 1e-6);
    
    // Test case #2
    UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 5.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 10.0);
    boolean isMinim = true;
    UnivariatePointValuePair result = best(a, b, isMinim);
    assertEquals(0.0, result.getPoint(), 1e-6);
    assertEquals(5.0, result.getValue(), 1e-6);
    
    // Test case #3
    UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 10.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 5.0);
    boolean isMinim = false;
    UnivariatePointValuePair result = best(a, b, isMinim);
    assertEquals(0.0, result.getPoint(), 1e-6);
    assertEquals(10.0, result.getValue(), 1e-6);
    
    // Test case #4
    UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 5.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 10.0);
    boolean isMinim = false;
    UnivariatePointValuePair result = best(a, b, isMinim);
    assertEquals(0.0, result.getPoint(), 1e-6);
    assertEquals(10.0, result.getValue(), 1e-6);
  }

}