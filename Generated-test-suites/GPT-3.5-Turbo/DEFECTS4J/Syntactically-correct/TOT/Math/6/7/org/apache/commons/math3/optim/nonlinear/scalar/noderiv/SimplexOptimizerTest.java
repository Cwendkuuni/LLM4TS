import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {
  
  private SimplexOptimizer optimizer;
  
  @Before
  public void setUp() {
    optimizer = new SimplexOptimizer();
  }
  
  @Test
  public void testOptimizeInternal_WithMaxEval_ReturnOptimalValue() {
    MultivariateFunction f = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] * point[0] + point[1] * point[1]; // x^2 + y^2
      }
    };
    int maxEval = 100;
    GoalType goalType = GoalType.MINIMIZE;
    
    PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);
    
    assertNotNull(result);
    assertEquals(0.0, result.getPoint()[0], 0.01);
    assertEquals(0.0, result.getPoint()[1], 0.01);
    assertEquals(0.0, result.getValue(), 0.01);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testOptimizeInternal_WithoutSimplex_ThrowNullArgumentException() {
    MultivariateFunction f = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] * point[0] + point[1] * point[1]; // x^2 + y^2
      }
    };
    int maxEval = 100;
    GoalType goalType = GoalType.MINIMIZE;
    
    optimizer.optimizeInternal(maxEval, f, goalType);
  }
  
  @Test
  public void testDoOptimize_WithConvergingSimplex_ReturnOptimalValue() {
    MultivariateFunction f = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] * point[0] + point[1] * point[1]; // x^2 + y^2
      }
    };
    int maxEval = 100;
    GoalType goalType = GoalType.MINIMIZE;
    optimizer.setSimplex(new Simplex());
    
    PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);
    
    assertNotNull(result);
    assertEquals(0.0, result.getPoint()[0], 0.01);
    assertEquals(0.0, result.getPoint()[1], 0.01);
    assertEquals(0.0, result.getValue(), 0.01);
  }
  
}