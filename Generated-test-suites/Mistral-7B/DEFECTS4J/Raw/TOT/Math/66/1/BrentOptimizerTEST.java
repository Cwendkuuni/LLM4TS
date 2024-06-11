package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testSimpleMinimization() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new FunctionSquare();
        double min = 0.0;
        double startValue = 1.0;
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, startValue);

        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionExp();
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);

        optimizer.optimize(function, goalType, min, startValue, new SimpleBounds(min, max));
    }

    @Test
    public void testOutOfBounds() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionExp();
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.optimize(function, goalType, min, startValue, new SimpleBounds(min, max));
            fail("Expected IllegalArgumentException for out-of-bounds input.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testInvalidStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionExp();
        double min = 0.0;
        double max = 10.0;
        double startValue = Double.POSITIVE_INFINITY;
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.optimize(function, goalType, min, startValue, new SimpleBounds(min, max));
            fail("Expected IllegalArgumentException for invalid start value.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testInvalidFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionNotDefined();
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.optimize(function, goalType, min, startValue, new SimpleBounds(min, max));
            fail("Expected FunctionEvaluationException for invalid function.");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    private static class FunctionSquare implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x;
        }
    }

    private static class FunctionExp implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.exp(x);
        }
    }

    private static class FunctionNotDefined implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new UnsupportedOperationException();
        }
    }
}