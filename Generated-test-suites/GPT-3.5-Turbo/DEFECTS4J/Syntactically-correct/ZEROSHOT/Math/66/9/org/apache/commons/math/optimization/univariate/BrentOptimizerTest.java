import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
  
  private BrentOptimizer optimizer;
  
  @Before
  public void setUp() {
    optimizer = new BrentOptimizer();
  }
  
  @Test
  public void testOptimize_withStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x;
      }
    };
    double min = -10.0;
    double max = 10.0;
    double startValue = 5.0;
    GoalType goalType = GoalType.MINIMIZE;

    double result = optimizer.optimize(f, goalType, min, max, startValue);

    assertEquals(0.0, result, 1E-10);
  }
  
  @Test
  public void testOptimize_withoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return -x * x;
      }
    };
    double min = -10.0;
    double max = 10.0;
    GoalType goalType = GoalType.MAXIMIZE;

    double result = optimizer.optimize(f, goalType, min, max);

    assertEquals(0.0, result, 1E-10);
  }
  
  @Test(expected = MaxIterationsExceededException.class)
  public void testOptimize_withMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return Math.sin(x);
      }
    };
    double min = -10.0;
    double max = 10.0;
    double startValue = 0.0;
    GoalType goalType = GoalType.MINIMIZE;

    optimizer.optimize(f, goalType, min, max, startValue);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimize_withInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return Math.sin(x);
      }
    };
    double min = -10.0;
    double max = 10.0;
    double startValue = 0.0;
    GoalType goalType = GoalType.MINIMIZE;
    double eps = 0.0;

    optimizer.setRelativeAccuracy(eps);
    optimizer.optimize(f, goalType, min, max, startValue);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimize_withInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return Math.sin(x);
      }
    };
    double min = -10.0;
    double max = 10.0;
    double startValue = 0.0;
    GoalType goalType = GoalType.MINIMIZE;
    double t = 0.0;

    optimizer.setAbsoluteAccuracy(t);
    optimizer.optimize(f, goalType, min, max, startValue);
  }
  
  @Test
  public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x * x + 2 * x * x + x + 1;
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    double lo = -10.0;
    double mid = 0.0;
    double hi = 10.0;
    double eps = 1E-10;
    double t = 1E-14;

    double result = optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);

    assertEquals(-1.0, result, 1E-10);
  }
}