import org.junit.Test;
import org.junit.Assert;

public class BrentOptimizerTest {
  
  @Test
  public void testOptimize() throws Exception {
    // Test basic functionality
    BrentOptimizer optimizer = new BrentOptimizer();
    UnivariateRealFunction function = new TestFunction();
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 5.0;
    
    double result = optimizer.optimize(function, goalType, min, max, startValue);
    
    Assert.assertEquals(0.0, result, 1e-10);
  }
  
  @Test
  public void testOptimizeWithDefaultStartValue() throws Exception {
    // Test using default startValue
    BrentOptimizer optimizer = new BrentOptimizer();
    UnivariateRealFunction function = new TestFunction();
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0.0;
    double max = 10.0;
    
    double result = optimizer.optimize(function, goalType, min, max);
    
    Assert.assertEquals(0.0, result, 1e-10);
  }

  @Test
  public void testOptimizeMaximize() throws Exception {
    // Test maximizing the function
    BrentOptimizer optimizer = new BrentOptimizer();
    UnivariateRealFunction function = new TestFunction();
    GoalType goalType = GoalType.MAXIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 5.0;
    
    double result = optimizer.optimize(function, goalType, min, max, startValue);
    
    Assert.assertEquals(10.0, result, 1e-10);
  }

  @Test(expected = MaxIterationsExceededException.class)
  public void testMaxIterationsExceeded() throws Exception {
    // Test exceeding the maximum iteration count
    BrentOptimizer optimizer = new BrentOptimizer();
    UnivariateRealFunction function = new TestFunction();
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 5.0;
    
    optimizer.setMaximalIterationCount(10);
    
    optimizer.optimize(function, goalType, min, max, startValue);
  }
  
  @Test(expected = FunctionEvaluationException.class)
  public void testFunctionEvaluationException() throws Exception {
    // Test throwing a FunctionEvaluationException
    BrentOptimizer optimizer = new BrentOptimizer();
    UnivariateRealFunction function = new TestFunctionThrowsException();
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 5.0;
    
    optimizer.optimize(function, goalType, min, max, startValue);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testNotStrictlyPositiveException1() throws Exception {
    // Test throwing a NotStrictlyPositiveException for epsilon
    BrentOptimizer optimizer = new BrentOptimizer();
    UnivariateRealFunction function = new TestFunction();
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 5.0;
    
    optimizer.setRelativeAccuracy(0.0);
    
    optimizer.optimize(function, goalType, min, max, startValue);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testNotStrictlyPositiveException2() throws Exception {
    // Test throwing a NotStrictlyPositiveException for tol
    BrentOptimizer optimizer = new BrentOptimizer();
    UnivariateRealFunction function = new TestFunction();
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 5.0;
    
    optimizer.setAbsoluteAccuracy(0.0);
    
    optimizer.optimize(function, goalType, min, max, startValue);
  }
}