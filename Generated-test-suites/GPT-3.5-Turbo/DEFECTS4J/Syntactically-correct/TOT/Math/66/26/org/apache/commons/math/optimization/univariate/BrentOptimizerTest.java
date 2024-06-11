import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

  @Test
  public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x;
      }
    };

    BrentOptimizer optimizer = new BrentOptimizer();
    double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 5);
    Assert.assertEquals(-10.0, result, 0.00001);

    result = optimizer.optimize(f, GoalType.MAXIMIZE, 0, 20, 15);
    Assert.assertEquals(20.0, result, 0.00001);
  }

  @Test
  public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return Math.sqrt(x);
      }
    };

    BrentOptimizer optimizer = new BrentOptimizer();
    double result = optimizer.optimize(f, GoalType.MINIMIZE, 1, 100);
    Assert.assertEquals(1.0, result, 0.00001);

    result = optimizer.optimize(f, GoalType.MAXIMIZE, 1, 100);
    Assert.assertEquals(100.0, result, 0.00001);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimizeInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x;
      }
    };

    BrentOptimizer optimizer = new BrentOptimizer();
    double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 5);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimizeInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return 2 * x;
      }
    };

    BrentOptimizer optimizer = new BrentOptimizer();
    double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 5);
  }

  @Test(expected = MaxIterationsExceededException.class)
  public void testOptimizeMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x * x;
      }
    };

    BrentOptimizer optimizer = new BrentOptimizer();
    optimizer.setMaximalIterationCount(100);
    double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
  }
}