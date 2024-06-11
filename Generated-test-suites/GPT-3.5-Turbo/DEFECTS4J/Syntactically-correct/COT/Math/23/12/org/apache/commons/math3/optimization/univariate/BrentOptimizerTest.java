import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testBrentOptimizer1() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
    // Test implementation here
  }

  @Test
  public void testBrentOptimizer2() {
    BrentOptimizer optimizer = new BrentOptimizer(0.2, 0.02);
    // Test implementation here
  }

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.3, 0.03);
    // Test implementation here
  }

  @Test
  public void testBrentOptimizerEdgeCase1() {
    BrentOptimizer optimizer = new BrentOptimizer(0, 0);
    // Test implementation here
  }

  @Test
  public void testBrentOptimizerEdgeCase2() {
    BrentOptimizer optimizer = new BrentOptimizer(-0.5, -0.05);
    // Test implementation here
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testBrentOptimizerException1() {
    BrentOptimizer optimizer = new BrentOptimizer(1, 0);
    // Test implementation here
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testBrentOptimizerException2() {
    BrentOptimizer optimizer = new BrentOptimizer(0.5, -0.05);
    // Test implementation here
  }

}