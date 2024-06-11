import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  private BrentOptimizer optimizer;
  
  @Test
  public void testDoOptimize_MinimizeFunction_Success() {
    optimizer = new BrentOptimizer(1e-15, 1e-15);
    
    optimizer.setMin(-10);
    optimizer.setStartValue(0);
    optimizer.setMax(10);
    optimizer.setGoalType(GoalType.MINIMIZE);
    
    UnivariatePointValuePair result = optimizer.doOptimize();
    
    assertEquals(-10, result.getPoint(), 0);
    assertEquals(0, result.getValue(), 0);
  }
  
  @Test
  public void testDoOptimize_MaximizeFunction_Success() {
    optimizer = new BrentOptimizer(1e-15, 1e-15);
    
    optimizer.setMin(-10);
    optimizer.setStartValue(0);
    optimizer.setMax(10);
    optimizer.setGoalType(GoalType.MAXIMIZE);
    
    UnivariatePointValuePair result = optimizer.doOptimize();
    
    assertEquals(10, result.getPoint(), 0);
    assertEquals(0, result.getValue(), 0);
  }
  
  @Test
  public void testDoOptimize_NoConvergence_Success() {
    optimizer = new BrentOptimizer(1e-15, 1e-15);
    
    optimizer.setMin(-10);
    optimizer.setStartValue(0);
    optimizer.setMax(10);
    optimizer.setGoalType(GoalType.MINIMIZE);
    
    UnivariatePointValuePair result = optimizer.doOptimize();
    
    assertNull(result);
  }
  
  @Test
  public void testDoOptimize_RelativeToleranceTooSmall_Success() {
    optimizer = new BrentOptimizer(1e-50, 1e-15);
    
    optimizer.setMin(-10);
    optimizer.setStartValue(0);
    optimizer.setMax(10);
    optimizer.setGoalType(GoalType.MINIMIZE);
    
    Exception exception = assertThrows(NumberIsTooSmallException.class, () -> {
      UnivariatePointValuePair result = optimizer.doOptimize();
    });
    
    assertEquals("The relative threshold must be no smaller than 4.9e-324", exception.getMessage());
  }
  
  @Test
  public void testDoOptimize_AbsoluteThresholdNegative_Success() {
    optimizer = new BrentOptimizer(1e-15, -1e-15);
    
    optimizer.setMin(-10);
    optimizer.setStartValue(0);
    optimizer.setMax(10);
    optimizer.setGoalType(GoalType.MINIMIZE);
    
    Exception exception = assertThrows(NotStrictlyPositiveException.class, () -> {
      UnivariatePointValuePair result = optimizer.doOptimize();
    });
    
    assertEquals("The absolute threshold must be positive", exception.getMessage());
  }
}