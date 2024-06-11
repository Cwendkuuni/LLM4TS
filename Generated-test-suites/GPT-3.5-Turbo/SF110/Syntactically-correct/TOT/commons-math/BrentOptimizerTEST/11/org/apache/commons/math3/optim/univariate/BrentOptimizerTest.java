import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testConstructorWithValidParameters() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
    assertNotNull(optimizer);
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithInvalidRelativeTolerance() {
    BrentOptimizer optimizer = new BrentOptimizer(-0.1, 0.01);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithInvalidAbsoluteThreshold() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, -0.01);
  }

  @Test
  public void testDoOptimizeWithMinimizeGoalType() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
    optimizer.setMin(0.0);
    optimizer.setMax(10.0);
    optimizer.setStartValue(5.0);
    optimizer.setGoalType(GoalType.MINIMIZE);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(0.0, result.getPoint(), 0.01);
    assertEquals(0.0, result.getValue(), 0.01);
  }

  @Test
  public void testDoOptimizeWithMaximizeGoalType() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
    optimizer.setMin(0.0);
    optimizer.setMax(10.0);
    optimizer.setStartValue(5.0);
    optimizer.setGoalType(GoalType.MAXIMIZE);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(10.0, result.getPoint(), 0.01);
    assertEquals(0.0, result.getValue(), 0.01);
  }

  @Test
  public void testDoOptimizeWithMaxIterations() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
    optimizer.setMin(0.0);
    optimizer.setMax(10.0);
    optimizer.setStartValue(5.0);
    optimizer.setMaxIterations(1);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertFalse(result.getPoint() == 0.0);
    assertFalse(result.getValue() == 0.0);
  }

  @Test
  public void testDoOptimizeWithCustomConvergenceChecker() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
    optimizer.setMin(0.0);
    optimizer.setMax(10.0);
    optimizer.setStartValue(5.0);
    optimizer.setConvergenceChecker(new DummyConvergenceChecker());
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertFalse(result.getPoint() == 0.0);
    assertFalse(result.getValue() == 0.0);
  }

  private class DummyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
      return iteration >= 10;
    }
  }
}