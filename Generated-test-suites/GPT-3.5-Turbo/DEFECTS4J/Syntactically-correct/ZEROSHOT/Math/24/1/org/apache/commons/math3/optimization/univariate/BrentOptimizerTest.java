import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrentOptimizerTest {

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(0, result.getPoint(), 0.0001);
    assertEquals(0, result.getValue(), 0.0001);
  }

  @Test
  public void testBest() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
    UnivariatePointValuePair a = new UnivariatePointValuePair(0, 5);
    UnivariatePointValuePair b = new UnivariatePointValuePair(1, 2);
    boolean isMinim = false;
    UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
    assertNotNull(result);
    assertEquals(1, result.getPoint(), 0.0001);
    assertEquals(2, result.getValue(), 0.0001);
  }
}