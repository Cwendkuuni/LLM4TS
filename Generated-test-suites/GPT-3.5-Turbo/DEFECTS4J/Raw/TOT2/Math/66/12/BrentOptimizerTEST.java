import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
        Assert.assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.0);
        Assert.assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.0);
    }

    // Test case for optimize method with startValue
    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; 
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.5);
        Assert.assertEquals(0.0, result, 1E-5);
    }

    // Test case for optimize method without startValue
    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; 
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 5.0);
        Assert.assertEquals(2.0, result, 1E-5);
    }

    // Test case for localMin method with edge cases
    @Test
    public void testLocalMinEdgeCases() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 5; 
            }
        };
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, -5.0, 0.0, 5.0, 1E-10, 1E-10);
        Assert.assertEquals(0.0, result, 1E-5);
    }

    // Test case for localMin method with error scenario
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinErrorScenario() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; 
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, -5.0, 0.0, 5.0, -1E-10, 1E-10);
    }
}