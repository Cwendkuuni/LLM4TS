import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testBrentOptimizer() {
    double rel = 0.01;
    double abs = 0.001;
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    assertNotNull(optimizer);
  }

  @Test(expected=NumberIsTooSmallException.class)
  public void testBrentOptimizerInvalidRel() {
    double rel = -0.01;
    double abs = 0.001;
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
  }

  @Test(expected=NotStrictlyPositiveException.class)
  public void testBrentOptimizerInvalidAbs() {
    double rel = 0.01;
    double abs = -0.001;
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
  }

  @Test
  public void testDoOptimize() {
    double rel = 0.01;
    double abs = 0.001;
    double min = 0;
    double max = 10;
    GoalType goal = GoalType.MINIMIZE;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;

    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    optimizer.setMin(min);
    optimizer.setMax(max);
    optimizer.setGoalType(goal);

    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(0, result.getPoint(), 0.001);
  }

  @Test
  public void testDoOptimizeMaximize() {
    double rel = 0.01;
    double abs = 0.001;
    double min = 0;
    double max = 10;
    GoalType goal = GoalType.MAXIMIZE;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;

    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    optimizer.setMin(min);
    optimizer.setMax(max);
    optimizer.setGoalType(goal);

    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(10, result.getPoint(), 0.001);
  }

  @Test
  public void testDoOptimizeMinEqualsMax() {
    double rel = 0.01;
    double abs = 0.001;
    double min = 3;
    double max = 3;
    GoalType goal = GoalType.MINIMIZE;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;

    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    optimizer.setMin(min);
    optimizer.setMax(max);
    optimizer.setGoalType(goal);

    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(3, result.getPoint(), 0.001);
  }

  @Test
  public void testDoOptimizeMaxEqualsMin() {
    double rel = 0.01;
    double abs = 0.001;
    double min = 5;
    double max = 5;
    GoalType goal = GoalType.MAXIMIZE;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;

    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    optimizer.setMin(min);
    optimizer.setMax(max);
    optimizer.setGoalType(goal);

    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(5, result.getPoint(), 0.001);
  }

  @Test
  public void testDoOptimizeMinGreaterThanMax() {
    double rel = 0.01;
    double abs = 0.001;
    double min = 10;
    double max = 5;
    GoalType goal = GoalType.MINIMIZE;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;

    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    optimizer.setMin(min);
    optimizer.setMax(max);
    optimizer.setGoalType(goal);

    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(5, result.getPoint(), 0.001);
  }

  @Test
  public void testDoOptimizeMaxGreaterThanMin() {
    double rel = 0.01;
    double abs = 0.001;
    double min = 5;
    double max = 10;
    GoalType goal = GoalType.MINIMIZE;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;

    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    optimizer.setMin(min);
    optimizer.setMax(max);
    optimizer.setGoalType(goal);

    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(0, result.getPoint(), 0.001);
  }

  @Test
  public void testBest() {
    double rel = 0.01;
    double abs = 0.001;

    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    UnivariatePointValuePair a = new UnivariatePointValuePair(0, 1);
    UnivariatePointValuePair b = new UnivariatePointValuePair(0, 2);
    UnivariatePointValuePair result = optimizer.best(a, b, true);

    assertNotNull(result);
    assertEquals(1, result.getValue(), 0.001);
  }
}