
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
  
  private BrentOptimizer optimizer;
  
  @Before
  public void setUp() {
    optimizer = new BrentOptimizer(1e-8, 1e-8);
  }
  
  // Test for Successful Optimization
  
  @Test
  public void testSuccessfulOptimization() {
    optimizer.setGoalType(GoalType.MINIMIZE);
    optimizer.setUnivariateObjectiveFunction(new UnivariateObjectiveFunction() {
      public double value(double x) {
        return x * x;
      }
    });
    
    double min = -10.0;
    double max = 10.0;
    
    UnivariatePointValuePair result = optimizer.optimize(min, max);
    
    assertEquals(0.0, result.getPoint(), 1e-8);
    assertEquals(0.0, result.getValue(), 1e-8);
  }
  
  // Test for Error Scenario: Starting point outside of range
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testStartingPointOutOfRange() {
    optimizer.setGoalType(GoalType.MAXIMIZE);
    optimizer.setUnivariateObjectiveFunction(new UnivariateObjectiveFunction() {
      public double value(double x) {
        return FastMath.sin(x);
      }
    });
    
    double min = -1.0;
    double max = 1.0;
    double start = -2.0; // outside the range
    
    optimizer.optimize(min, max, start);
  }
  
  // Test for Error Scenario: Relative threshold is too small
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testRelativeThresholdTooSmall() {
    double rel = 1e-10; // smaller than the minimum allowed value
    double abs = 1e-8;
    optimizer = new BrentOptimizer(rel, abs);
    
    optimizer.setGoalType(GoalType.MINIMIZE);
    optimizer.setUnivariateObjectiveFunction(new UnivariateObjectiveFunction() {
      public double value(double x) {
        return x * x;
      }
    });
    
    double min = -10.0;
    double max = 10.0;
    
    optimizer.optimize(min, max);
  }
  
  // Test for Error Scenario: Absolute threshold is negative
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testNegativeAbsoluteThreshold() {
    double rel = 1e-8;
    double abs = -1e-8; // negative value
    optimizer = new BrentOptimizer(rel, abs);
    
    optimizer.setGoalType(GoalType.MINIMIZE);
    optimizer.setUnivariateObjectiveFunction(new UnivariateObjectiveFunction() {
      public double value(double x) {
        return x * x;
      }
    });
    
    double min = -10.0;
    double max = 10.0;
    
    optimizer.optimize(min, max);
  }
}

