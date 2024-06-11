import org.junit.Test;

public class PowellOptimizerTest {

  // Test getStartPoint() method
  @Test
  public void testGetStartPoint() {
    // create an instance of PowellOptimizer
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);

    // get the start point
    double[] startPoint = optimizer.getStartPoint();

    // assert the start point is not null
    assertNotNull(startPoint);
  }

  // Test getGoalType() method
  @Test
  public void testGetGoalType() {
    // create an instance of PowellOptimizer
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);

    // get the goal type
    GoalType goalType = optimizer.getGoalType();

    // assert the goal type is not null
    assertNotNull(goalType);
  }

  // Test getConvergenceChecker() method
  @Test
  public void testGetConvergenceChecker() {
    // create an instance of PowellOptimizer
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);

    // get the convergence checker
    ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();

    // assert the convergence checker is not null
    assertNotNull(convergenceChecker);
  }

  // Test computeObjectiveValue() method with non-null input
  @Test
  public void testComputeObjectiveValueNonNull() {
    // create an instance of PowellOptimizer
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);

    // create a test input
    double[] input = new double[]{1.0, 2.0, 3.0};

    // compute the objective value
    double objectiveValue = optimizer.computeObjectiveValue(input);

    // assert the objective value is a finite number
    assertTrue(Double.isFinite(objectiveValue));
  }

  // Test computeObjectiveValue() method with null input
  @Test(expected = NullPointerException.class)
  public void testComputeObjectiveValueNull() {
    // create an instance of PowellOptimizer
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);

    // compute the objective value with null input
    optimizer.computeObjectiveValue(null);
  }

  // Test doOptimize() method
  @Test
  public void testDoOptimize() {
    // create an instance of PowellOptimizer
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);

    // optimize
    PointValuePair result = optimizer.doOptimize();

    // assert the result is not null
    assertNotNull(result);
  }
}