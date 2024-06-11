import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {
  
  private BrentOptimizer optimizer;
  
  @Before
  public void setUp() {
    optimizer = new BrentOptimizer();
  }
  
  @Test
  public void testOptimize_withMaxGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
    // Test with a simple quadratic function
    UnivariateRealFunction f = new QuadraticFunction(1, -6, 9);
    double min = optimizer.optimize(f, GoalType.MAXIMIZE, 0, 5, 0);
    assertEquals(0, min, 0.001);
  }
  
  @Test
  public void testOptimize_withMinGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
    // Test with a simple quadratic function
    UnivariateRealFunction f = new QuadraticFunction(1, -6, 9);
    double min = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 0);
    assertEquals(3, min, 0.001);
  }
  
  @Test
  public void testOptimize_withNanValue() {
    // Test with a function that returns NaN
    UnivariateRealFunction f = new NanFunction();
    try {
      optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 0);
      fail("Expected FunctionEvaluationException");
    } catch (FunctionEvaluationException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testOptimize_withInfiniteValue() {
    // Test with a function that returns infinite value
    UnivariateRealFunction f = new InfiniteFunction();
    try {
      optimizer.optimize(f, GoalType.MAXIMIZE, 0, 5, 0);
      fail("Expected FunctionEvaluationException");
    } catch (FunctionEvaluationException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testOptimize_withNotStrictlyPositiveEpsValue() {
    // Test with negative eps value
    UnivariateRealFunction f = new QuadraticFunction(1, -6, 9);
    try {
      optimizer.optmize(f, GoalType.MINIMIZE, 0, 5, 0, -0.001);
      fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testOptimize_withNotStrictlyPositiveTValue() {
    // Test with negative t value
    UnivariateRealFunction f = new QuadraticFunction(1, -6, 9);
    try {
      optimizer.optmize(f, GoalType.MINIMIZE, 0, 5, 0, 0.001, -0.001);
      fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
      // Expected exception
    }
  }
  
  private static class QuadraticFunction implements UnivariateRealFunction {
    private double a;
    private double b;
    private double c;
    
    public QuadraticFunction(double a, double b, double c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }
    
    public double value(double x) {
      return a * x * x + b * x + c;
    }
  }
  
  private static class NanFunction implements UnivariateRealFunction {
    public double value(double x) {
      return Double.NaN;
    }
  }
  
  private static class InfiniteFunction implements UnivariateRealFunction {
    public double value(double x) {
      return Double.POSITIVE_INFINITY;
    }
  }

}