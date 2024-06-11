import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

  private BrentOptimizer optimizer;

  @Before
  public void setUp() {
    optimizer = new BrentOptimizer(0.001, 0.001);
  }

  @Test
  public void testBrentOptimizerConstructor() {
    try {
      BrentOptimizer optimizer = new BrentOptimizer(-0.001, 0.001);
      fail("Expected NumberIsTooSmallException");
    } catch (NumberIsTooSmallException e) {
      // Expected Exception
    }
    try {
      BrentOptimizer optimizer = new BrentOptimizer(0.001, -0.001);
      fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
      // Expected Exception
    }
  }

  @Test
  public void testDoOptimize() {
    // Test for a case where the minimum is in the middle of the range
    double min = optimizer.optimize(100, x -> (x - 5) * (x - 5)).getPoint();
    assertEquals(5.0, min, 0.001);

    // Test for a case where the minimum is at the lower end of the range
    min = optimizer.optimize(100, x -> x * x).getPoint();
    assertEquals(0.0, min, 0.001);

    // Test for a case where the minimum is at the upper end of the range
    min = optimizer.optimize(100, x -> (x - 10) * (x - 10)).getPoint();
    assertEquals(10.0, min, 0.001);

    // Test for a case where the goal type is MAXIMIZE instead of MINIMIZE
    optimizer.setGoalType(GoalType.MAXIMIZE);
    min = optimizer.optimize(100, x -> -x * x).getPoint();
    assertEquals(0.0, min, 0.001);
  }

  @Test
  public void testBest() {
    UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 1.0);
    UnivariatePointValuePair result = optimizer.best(a, b, true);
    assertEquals(b, result);

    a = new UnivariatePointValuePair(0.0, 1.0);
    b = new UnivariatePointValuePair(0.0, 0.0);
    result = optimizer.best(a, b, true);
    assertEquals(a, result);

    a = null;
    result = optimizer.best(a, b, true);
    assertEquals(b, result);

    b = null;
    result = optimizer.best(a, b, true);
    assertNull(result);
  }

}