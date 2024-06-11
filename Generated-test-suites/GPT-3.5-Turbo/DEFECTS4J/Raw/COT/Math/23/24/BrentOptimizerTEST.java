import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
  
  private BrentOptimizer optimizer;
  
  @Before
  public void setUp() {
    optimizer = new BrentOptimizer(0.0001, 0.0001);
  }
  
  @Test
  public void testDoOptimize() {
    // Test basic functionality
    double min = optimizer.optimize(new MyFunction(), org.apache.commons.math3.optimization.GoalType.MINIMIZE,
                                    0.0, 1.0);
    assertEquals(0.0, min, 0.0001);
    
    double max = optimizer.optimize(new MyFunction(), org.apache.commons.math3.optimization.GoalType.MAXIMIZE,
                                    0.0, 1.0);
    assertEquals(1.0, max, 0.0001);
  }
  
  @Test(expected = org.apache.commons.math3.exception.NumberIsTooSmallException.class)
  public void testNegativeRelativeThreshold() {
    optimizer = new BrentOptimizer(-0.1, 0.0001);
  }
  
  @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
  public void testNegativeAbsoluteThreshold() {
    optimizer = new BrentOptimizer(0.0001, -0.1);
  }
  
  @Test
  public void testZeroRelativeAndAbsoluteThreshold() {
    optimizer = new BrentOptimizer(0.0, 0.0);
    double min = optimizer.optimize(new MyFunction(), org.apache.commons.math3.optimization.GoalType.MINIMIZE,
                                    0.0, 1.0);
    assertEquals(0.0, min, 0.0001);
    
    double max = optimizer.optimize(new MyFunction(), org.apache.commons.math3.optimization.GoalType.MAXIMIZE,
                                    0.0, 1.0);
    assertEquals(1.0, max, 0.0001);
  }
  
  @Test
  public void testCustomConvergenceChecker() {
    // Test with a custom convergence checker
    optimizer = new BrentOptimizer(0.0001, 0.0001, new MyConvergenceChecker());
    
    double min = optimizer.optimize(new MyFunction(), org.apache.commons.math3.optimization.GoalType.MINIMIZE,
                                    0.0, 1.0);
    assertEquals(0.0, min, 0.0001);
  }
  
  private static class MyFunction implements org.apache.commons.math3.analysis.UnivariateFunction {
    public double value(double x) {
      return x;
    }
  }
  
  private static class MyConvergenceChecker implements org.apache.commons.math3.optimization.ConvergenceChecker<org.apache.commons.math3.optimization.UnivariatePointValuePair> {
    public boolean converged(int iteration, org.apache.commons.math3.optimization.UnivariatePointValuePair previous, org.apache.commons.math3.optimization.UnivariatePointValuePair current) {
      if (iteration > 0) {
        double diff = Math.abs(current.getValue() - previous.getValue());
        return diff < 0.0001;
      }
      return false;
    }
  }
}