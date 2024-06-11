import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

    @Test
    public void testOptimize_withStartValue_and_GoalType_MINIMIZE() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test typical case
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 2);
        Assert.assertEquals(0.0, result, 1e-5);

        // Test edge case
        result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 0);
        Assert.assertEquals(0.0, result, 1e-5);

        // Test error scenario
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 20); // startValue is out of range
            Assert.fail("Expected FunctionEvaluationException was not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimize_withoutStartValue_and_GoalType_MINIMIZE() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test typical case
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        Assert.assertEquals(0.0, result, 1e-5);

        // Test edge case
        result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 0); // startValue is at the lower bound
        Assert.assertEquals(0.0, result, 1e-5);

        // Test error scenario
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 20); // startValue is out of range
            Assert.fail("Expected FunctionEvaluationException was not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimize_withoutStartValue_and_GoalType_MAXIMIZE() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test typical case
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return -x * x;
            }
        };
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10);
        Assert.assertEquals(0.0, result, 1e-5);

        // Test edge case
        result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10); // startValue is at the upper bound
        Assert.assertEquals(0.0, result, 1e-5);

        // Test error scenario
        try {
            result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, -20); // startValue is out of range
            Assert.fail("Expected FunctionEvaluationException was not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocalMin_withGoalType_MINIMIZE() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test typical case
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        double result = optimizer.localMin(true, function, GoalType.MINIMIZE, -5, 0, 5, 1e-6, 1e-8);
        Assert.assertEquals(0.0, result, 1e-5);

        // Test edge case
        result = optimizer.localMin(true, function, GoalType.MINIMIZE, 0, 0, 0, 1e-6, 1e-8); // lo == hi == mid
        Assert.assertEquals(0.0, result, 1e-5);

        // Test error scenario
        try {
            result = optimizer.localMin(true, function, GoalType.MINIMIZE, -10, 10, 20, -1e-6, 1e-8); // eps is not strictly positive
            Assert.fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocalMin_withGoalType_MAXIMIZE() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test typical case
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return -x * x;
            }
        };
        double result = optimizer.localMin(false, function, GoalType.MAXIMIZE, -5, 0, 5, 1e-6, 1e-8);
        Assert.assertEquals(0.0, result, 1e-5);

        // Test edge case
        result = optimizer.localMin(false, function, GoalType.MAXIMIZE, 0, 0, 0, 1e-6, 1e-8); // lo == hi == mid
        Assert.assertEquals(0.0, result, 1e-5);

        // Test error scenario
        try {
            result = optimizer.localMin(false, function, GoalType.MAXIMIZE, -10, 10, 20, 1e-6, -1e-8); // t is not strictly positive
            Assert.fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

}