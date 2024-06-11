import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);

        double expectedValue = -3.0;
        double expectedPoint = -2.4662547406765835;

        Assert.assertEquals(expectedValue, result.getValue(), 1e-6);
        Assert.assertEquals(expectedPoint, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximize() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max);

        double expectedValue = 3.0;
        double expectedPoint = 2.4662547406765835;

        Assert.assertEquals(expectedValue, result.getValue(), 1e-6);
        Assert.assertEquals(expectedPoint, result.getPoint(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizerMaxIterations() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizerFunctionEvaluation() throws Exception {
        UnivariateRealFunction function = new MyFunctionThrowingException();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
        }
    }

    private static class MyFunctionThrowingException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (x == 1.0) {
                throw new FunctionEvaluationException("Evaluation exception!");
            }
            return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
        }
    }
}