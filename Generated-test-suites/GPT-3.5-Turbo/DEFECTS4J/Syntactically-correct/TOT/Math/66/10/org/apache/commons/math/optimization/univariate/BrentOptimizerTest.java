import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    // Test optimize method when startValue is not provided
    @Test
    public void testOptimizeWithStartValueNotProvided() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        Assert.assertEquals(0.0, result, 0.001);
    }

    // Test optimize method with startValue provided
    @Test
    public void testOptimizeWithStartValueProvided() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10, 5);
        Assert.assertEquals(3.1415, result, 0.001);
    }

    // Test optimize method with invalid range
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidRange() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        optimizer.optimize(function, GoalType.MINIMIZE, 10, 0);
    }

    // Test optimize method with function evaluation exception
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
    }

    // Test doOptimize method
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    // Test localMin method with basic input
    @Test
    public void testLocalMinWithBasicInput() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        double result = optimizer.localMin(true, function, GoalType.MINIMIZE, 0, 5, 10, 0.1, 0.01);
        Assert.assertEquals(1.5707, result, 0.001);
    }

    // Test localMin method with invalid interval
    @Test(expected = IllegalArgumentException.class)
    public void testLocalMinWithInvalidInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        optimizer.localMin(true, function, GoalType.MINIMIZE, 10, 5, 0, 0.1, 0.01);
    }

    // Test localMin method with negative epsilon
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeEpsilon() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        optimizer.localMin(true, function, GoalType.MINIMIZE, 0, 5, 10, -0.1, 0.01);
    }

    // Test localMin method with negative tolerance
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        optimizer.localMin(true, function, GoalType.MINIMIZE, 0, 5, 10, 0.1, -0.01);
    }

    // Test localMin method with function evaluation exception
    @Test(expected = FunctionEvaluationException.class)
    public void testLocalMinWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Sin();
        optimizer.localMin(true, function, GoalType.MINIMIZE, -10, 0, 10, 0.1, 0.01);
    }

}