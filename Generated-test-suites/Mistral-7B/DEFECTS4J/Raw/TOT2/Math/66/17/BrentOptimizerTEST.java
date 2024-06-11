package org.apache.commons.math.optimization.univariate.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBasicFunctionality() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new FunctionSquared(-1);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, SimpleGoalType.MINIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(-2.44948974278317, result.getPoint(), 1e-7);
        Assert.assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new FunctionSquared(-1);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new FunctionWithException();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(10000);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    @Test
    public void testEdgeCases() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new FunctionAtMinimum();
        UnivariateOptimizer optimizer = new BrentOptimizer();

        // Test minimum at boundary
        PointValuePair result1 = optimizer.optimize(function, SimpleGoalType.MINIMIZE, -10.0, 10.0, -10.0);
        Assert.assertEquals(-10.0, result1.getPoint(), 1e-7);
        Assert.assertEquals(0.0, result1.getValue(), 1e-7);

        // Test minimum at interior point
        PointValuePair result2 = optimizer.optimize(function, SimpleGoalType.MINIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(-5.0, result2.getPoint(), 1e-7);
        Assert.assertEquals(25.0, result2.getValue(), 1e-7);
    }

    @Test
    public void testNegativeFunction() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new FunctionSquaredNegative();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, SimpleGoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(2.44948974278317, result.getPoint(), 1e-7);
        Assert.assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test
    public void testBounds() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new FunctionWithBounds();
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, SimpleGoalType.MINIMIZE, bounds);
        Assert.assertEquals(-0.5, result.getPoint(), 1e-7);
        Assert.assertEquals(0.25, result.getValue(), 1e-7);
    }

    private static class FunctionSquared extends UnivariateRealFunction {
        private final double a;

        FunctionSquared(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return x * x - a;
        }
    }

    private static class FunctionSquaredNegative extends UnivariateRealFunction {
        @Override
        public double value(double x) {
            return -(x * x);
        }
    }

    private static class FunctionAtMinimum extends UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.abs(x) - 5, 2);
        }
    }

    private static class FunctionWithException extends UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (x == 0) {
                throw new FunctionEvaluationException("Function evaluation failed.");
            }
            return Math.pow(x, 2);
        }
    }

    private static class FunctionWithBounds extends UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x < -3 || x > 3) {
                throw new IllegalArgumentException("x should be in [-3, 3]");
            }
            return Math.pow(x, 2) - 0.5 * x;
        }
    }
}