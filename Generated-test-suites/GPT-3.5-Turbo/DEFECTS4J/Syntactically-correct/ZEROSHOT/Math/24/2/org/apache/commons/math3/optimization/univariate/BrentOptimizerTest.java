import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BrentOptimizerTest {

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
    optimizer.setMin(0);
    optimizer.setMax(10);
    optimizer.setStartValue(5);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertEquals(0, result.getX(), 0.0001);
  }

  @Test
  public void testBest() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
    UnivariatePointValuePair a = new UnivariatePointValuePair(0, 1);
    UnivariatePointValuePair b = new UnivariatePointValuePair(1, 2);
    UnivariatePointValuePair best = optimizer.best(a, b, true);
    assertEquals(a, best);
  }
}