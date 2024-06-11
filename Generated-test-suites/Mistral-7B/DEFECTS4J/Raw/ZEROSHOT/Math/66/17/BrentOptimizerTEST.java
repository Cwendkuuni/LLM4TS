import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_minimize() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(result, -1.7320508075688772, 0.0001);
    }

    @Test
    public void testBrentOptimizer_maximize() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(result, 1.7320508075688772, 0.0001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizer_functionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception.");
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizer_maxIterationsExceededException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_notStrictlyPositiveException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(0.0);
        optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }
}