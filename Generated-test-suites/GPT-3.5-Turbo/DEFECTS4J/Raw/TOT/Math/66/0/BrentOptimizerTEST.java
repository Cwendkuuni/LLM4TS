import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private static final double DEFAULT_EPSILON = 1e-10;
    private static final double DEFAULT_TOLERANCE = 1e-14;
    private static final int DEFAULT_MAX_ITERATIONS = 100;

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_withStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double min = 0.5;
        double max = 3.5;
        double startValue = 2.0;
        double expected = 1.0;

        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

        assertEquals(expected, result, DEFAULT_EPSILON);
    }

    @Test
    public void testOptimize_withoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double min = 0.5;
        double max = 3.5;
        double expected = 1.0;

        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);

        assertEquals(expected, result, DEFAULT_EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_withZeroEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double min = 0.5;
        double max = 3.5;
        double startValue = 2.0;
        double epsilon = 0.0;

        optimizer.setRelativeAccuracy(epsilon);
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_withZeroTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double min = 0.5;
        double max = 3.5;
        double startValue = 2.0;
        double tolerance = 0.0;

        optimizer.setAbsoluteAccuracy(tolerance);
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimize_withNullFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        double min = 0.5;
        double max = 3.5;
        double startValue = 2.0;

        optimizer.optimize(null, GoalType.MINIMIZE, min, max, startValue);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimize_withNoOptimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        double min = 0.5;
        double max = 3.5;
        double startValue = 2.0;

        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_withMaxIterationsReached() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double min = 0.5;
        double max = 3.5;
        double startValue = 2.0;
        int maxIterations = 5;

        optimizer.setMaximalIterationCount(maxIterations);
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }

}