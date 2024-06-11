import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {
  
  private static final double EPSILON = 1e-15;
    
  @Test
  public void testOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON, new SimpleValueChecker());
    
    // Test case 1
    UnivariatePointValuePair result1 = optimizer.optimize(10, x -> x * x, GoalType.MINIMIZE);
    assertEquals(0, result1.getPoint(), EPSILON);
    assertEquals(0, result1.getValue(), EPSILON);
    
    // Test case 2
    UnivariatePointValuePair result2 = optimizer.optimize(10, x -> -(x * x), GoalType.MINIMIZE);
    assertEquals(0, result2.getPoint(), EPSILON);
    assertEquals(0, result2.getValue(), EPSILON);
    
    // Test case 3
    UnivariatePointValuePair result3 = optimizer.optimize(-10, x -> x * x, GoalType.MINIMIZE);
    assertEquals(0, result3.getPoint(), EPSILON);
    assertEquals(0, result3.getValue(), EPSILON);
    
    // Test case 4
    UnivariatePointValuePair result4 = optimizer.optimize(-10, x -> -(x * x), GoalType.MINIMIZE);
    assertEquals(0, result4.getPoint(), EPSILON);
    assertEquals(0, result4.getValue(), EPSILON);
  }
}