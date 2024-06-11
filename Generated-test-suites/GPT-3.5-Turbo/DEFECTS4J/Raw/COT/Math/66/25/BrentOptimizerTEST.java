
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4 * x + 3; // Quadratic function with roots at x = 1 and x = 3
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x * x - 10; // Cubic function with one real root at x = 10^(1/3)
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
    }
}