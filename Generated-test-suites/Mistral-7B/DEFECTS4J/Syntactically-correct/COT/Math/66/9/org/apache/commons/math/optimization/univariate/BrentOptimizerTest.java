import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializer.ValueSelector;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbstractUnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(5);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        optimizer.setAbsoluteAccuracy(0.001);
        optimizer.setRelativeAccuracy(0.0001);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -3, 3, 0);
        assertEquals(-1.5, result.getPoint(), 0.001);
        assertEquals(-2.25, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbstractUnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(5);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        optimizer.setAbsoluteAccuracy(0.001);
        optimizer.setRelativeAccuracy(0.0001);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -3, 3, 0);
        assertEquals(1, result.getPoint(), 0.001);
        assertEquals(1, result.getValue(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbstractUnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5, -0.5, -0.001, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbstractUnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0, 0.5, -0.001, 0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbstractUnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, 1, 2, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
        // Note: This test may not work with some function evaluators,
        // as some may not propagate exceptions correctly.
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbstractUnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x > 0) {
                    throw new FunctionEvaluationException("Function evaluation failed.");
                }
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, -1, 1, 0);
    }
}