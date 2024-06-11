import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testMinimizeSimpleFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = 0.0;
        double startValue = 1.0;

        double result = new BrentOptimizer().optimize(function, goalType, min, max, startValue);
        assertEquals(0.0, result, 1e-8);
    }

    @Test
    public void testMinimizeFunctionWithMultipleLocalMinima() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) * Math.exp(-Math.abs(x));
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = 5.0;
        double startValue = 2.0;

        double result = new BrentOptimizer().optimize(function, goalType, min, max, startValue);
        assertEquals(-2.44948974278317, result, 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteAccuracy() throws Exception {
        new BrentOptimizer().setAbsoluteAccuracy(-1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeAccuracy() throws Exception {
        new BrentOptimizer().setRelativeAccuracy(-1e-8);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = 0.0;
        double startValue = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(0);

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = 0.0;
        double startValue = 0.0;

        new BrentOptimizer().optimize(function, goalType, min, max, startValue);
    }
}