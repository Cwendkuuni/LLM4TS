import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.PointValueMap;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10, 10, 2.0);
        assertEquals(result.getValue(), -Math.PI, 0.001);
        assertEquals(result.getPoint(), -Math.PI, 0.001);
    }

    @Test
    public void testBrentOptimizerMaximization() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -10, 10, 2.0);
        assertEquals(result.getValue(), Math.PI, 0.001);
        assertEquals(result.getPoint(), Math.PI, 0.001);
    }

    @Test
    public void testBrentOptimizerMinimumOutsideInterval() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI - x) * Math.cos(Math.PI - x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, -10, 10, 5));
    }

    @Test
    public void testBrentOptimizerMaximumOutsideInterval() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI + x) * Math.cos(Math.PI + x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(testFunction, GoalType.MAXIMIZE, -10, 10, 5));
    }

    @Test
    public void testBrentOptimizerWithInitialGuess() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10, 10, 0, new InitialGuess() {
            @Override
            public double getInitialGuess() {
                return 0;
            }
        });
        assertEquals(result.getValue(), -Math.PI, 0.001);
        assertEquals(result.getPoint(), -Math.PI, 0.001);
    }

    @Test
    public void testBrentOptimizerWithInvalidAccuracy() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        double invalidAccuracy = 0;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(invalidAccuracy));
    }

    @Test
    public void testBrentOptimizerWithNegativeAccuracy() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        double negativeAccuracy = -0.01;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(negativeAccuracy));
    }

    @Test
    public void testBrentOptimizerWithInvalidIterationsCount() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        int invalidIterationsCount = Integer.MIN_VALUE;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.setMaxIterations(invalidIterationsCount));
    }

    @Test
    public void testBrentOptimizerWithNegativeIterationsCount() throws FunctionEvaluationException, MaxIterationsExceededException, NotStrictlyPositiveException {
        int negativeIterationsCount = Integer.MIN_VALUE;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.setMaxIterations(negativeIterationsCount));
    }
}