import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {

  @Test
  public void testOptimizeInternal() {
    //create instance of SimplexOptimizer
    SimplexOptimizer optimizer = new SimplexOptimizer();

    //create test data
    double[] startPoint = new double[]{1.0, 2.0, 3.0};
    MultivariateFunction function = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] + point[1] + point[2];
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    OptimizationData[] optData = new OptimizationData[]{new AbstractSimplex()};

    //call the optimizeInternal method
    PointValuePair result = optimizer.optimizeInternal(100, function, goalType, optData);

    //verify the result
    assertEquals(1.0, result.getPoint()[0], 0.0001);
    assertEquals(2.0, result.getPoint()[1], 0.0001);
    assertEquals(3.0, result.getPoint()[2], 0.0001);
    assertEquals(6.0, result.getValue(), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOptimizeInternalWithNullSimplex() {
    //create instance of SimplexOptimizer
    SimplexOptimizer optimizer = new SimplexOptimizer();

    //create test data with null simplex
    double[] startPoint = new double[]{1.0, 2.0, 3.0};
    MultivariateFunction function = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] + point[1] + point[2];
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    OptimizationData[] optData = new OptimizationData[]{null};

    //call the optimizeInternal method with null simplex
    PointValuePair result = optimizer.optimizeInternal(100, function, goalType, optData);
  }
}