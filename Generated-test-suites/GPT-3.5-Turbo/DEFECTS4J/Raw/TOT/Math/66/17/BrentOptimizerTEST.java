import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

  @Test
  public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * (x - 1);
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0;
    double max = 2;
    double startValue = 1;

    BrentOptimizer optimizer = new BrentOptimizer();
    double result = optimizer.optimize(f, goalType, min, max, startValue);

    assertEquals(0.5, result, 0);
  }

  @Test
  public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * (x - 1);
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0;
    double max = 2;

    BrentOptimizer optimizer = new BrentOptimizer();
    double result = optimizer.optimize(f, goalType, min, max);

    assertEquals(0.5, result, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOptimizeInvalidGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * (x - 1);
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0;
    double max = 2;

    BrentOptimizer optimizer = new BrentOptimizer();
    optimizer.optimize(f, GoalType.MAXIMIZE, min, max);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimizeInvalidTolerance1() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * (x - 1);
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0;
    double max = 2;

    BrentOptimizer optimizer = new BrentOptimizer();
    optimizer.setRelativeAccuracy(0);
    optimizer.optimize(f, goalType, min, max);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimizeInvalidTolerance2() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * (x - 1);
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0;
    double max = 2;

    BrentOptimizer optimizer = new BrentOptimizer();
    optimizer.setAbsoluteAccuracy(0);
    optimizer.optimize(f, goalType, min, max);
  }

  @Test(expected = MaxIterationsExceededException.class)
  public void testOptimizeMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return x * (x - 1);
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    double min = 0;
    double max = 2;

    BrentOptimizer optimizer = new BrentOptimizer();
    optimizer.setMaximalIterationCount(3);
    optimizer.optimize(f, goalType, min, max);
  }

}