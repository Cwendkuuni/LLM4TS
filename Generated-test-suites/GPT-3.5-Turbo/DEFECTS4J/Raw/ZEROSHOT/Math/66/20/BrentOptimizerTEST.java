import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer = new BrentOptimizer();

    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        double min = -1.0;
        double max = 1.0;
        double expectedOptimum = 0.0;
        double actualOptimum = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        Assert.assertEquals(expectedOptimum, actualOptimum, 1e-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        double min = -1.0;
        double max = 1.0;
        double eps = 0.0;
        optimizer.localMin(true, f, GoalType.MINIMIZE, min, 0.0, max, eps, 1e-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        double min = -1.0;
        double max = 1.0;
        double t = 0.0;
        optimizer.localMin(true, f, GoalType.MINIMIZE, min, 0.0, max, 1e-10, t);
    }
}