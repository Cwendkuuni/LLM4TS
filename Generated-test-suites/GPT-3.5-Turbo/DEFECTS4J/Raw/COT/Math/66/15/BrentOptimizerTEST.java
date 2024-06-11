
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double actual = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.5);
        double expected = 0.0;
        Assert.assertEquals(expected, actual, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double actual = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        double expected = 0.0;
        Assert.assertEquals(expected, actual, 1e-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double actual = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        // Test will fail if max iterations exceed and exception is not thrown
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double actual = optimizer.localMin(true, f, GoalType.MINIMIZE, -1.0, 0.0, 1.0, 0.0, 1e-10);
        // Test will fail if NotStrictlyPositiveException is not thrown
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double actual = optimizer.localMin(true, f, GoalType.MINIMIZE, -1.0, 0.0, 1.0, 1e-10, 0.0);
        // Test will fail if NotStrictlyPositiveException is not thrown
    }
}
