import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);

    // Test input values
    double lo = 0;
    double mid = 5;
    double hi = 10;
    double minExpected = 5;
    double maxExpected = 10;
    double result;

    // Test when the goal type is MINIMIZE
    optimizer.setGoalType(GoalType.MINIMIZE);

    // Test when lo < hi
    optimizer.setMin(lo);
    optimizer.setStartValue(mid);
    optimizer.setMax(hi);
    result = optimizer.doOptimize().getPoint();
    assertEquals(minExpected, result, 0.0001);

    // Test when lo > hi
    optimizer.setMin(hi);
    optimizer.setStartValue(mid);
    optimizer.setMax(lo);
    result = optimizer.doOptimize().getPoint();
    assertEquals(minExpected, result, 0.0001);

    // Test when the goal type is MAXIMIZE
    optimizer.setGoalType(GoalType.MAXIMIZE);

    // Test when lo < hi
    optimizer.setMin(lo);
    optimizer.setStartValue(mid);
    optimizer.setMax(hi);
    result = optimizer.doOptimize().getPoint();
    assertEquals(maxExpected, result, 0.0001);

    // Test when lo > hi
    optimizer.setMin(hi);
    optimizer.setStartValue(mid);
    optimizer.setMax(lo);
    result = optimizer.doOptimize().getPoint();
    assertEquals(maxExpected, result, 0.0001);
  }

  @Test
  public void testBest() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);

    // Test input values
    UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
    boolean isMinim = true;
    UnivariatePointValuePair result;

    // Test when a is not null and b is null
    result = optimizer.best(a, null, isMinim);
    assertEquals(a, result);

    // Test when a is null and b is not null
    result = optimizer.best(null, b, isMinim);
    assertEquals(b, result);

    // Test when both a and b are null
    result = optimizer.best(null, null, isMinim);
    assertNull(result);

    // Test when isMinim is true and a < b
    result = optimizer.best(a, b, isMinim);
    assertEquals(a, result);

    // Test when isMinim is true and b < a
    result = optimizer.best(b, a, isMinim);
    assertEquals(a, result);

    // Test when isMinim is false and a < b
    isMinim = false;
    result = optimizer.best(a, b, isMinim);
    assertEquals(b, result);

    // Test when isMinim is false and b < a
    result = optimizer.best(b, a, isMinim);
    assertEquals(b, result);
  }
}