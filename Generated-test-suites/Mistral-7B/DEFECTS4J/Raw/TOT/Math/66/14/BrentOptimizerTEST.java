package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testSimpleMinimum() throws Exception {
        UnivariateRealFunction f = new Function("x^2", x -> x * x);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(-2.5, result.getPoint(), 1e-8);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testSimpleMaximum() throws Exception {
        UnivariateRealFunction f = new Function("x^2", x -> x * x);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(2.5, result.getPoint(), 1e-8);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testFunctionWithNumericalZero() throws Exception {
        UnivariateRealFunction f = new Function("x^3 - 100x", x -> x * x * x - 100 * x);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(-2.4142135625, result.getPoint(), 1e-5);
        Assert.assertEquals(107.99999999999999, result.getValue(), 1e-5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction f = new Function("x^2", x -> x * x);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(0); // fewer evaluations than necessary
        optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new Function("1/x", x -> 1.0 / x);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 2.0, 1.5);
    }

    private static class Function implements UnivariateRealFunction {
        private String name;
        private Function(String name, UnivariateRealFunction func) {
            this.name = name;
            this.function = func;
        }

        private UnivariateRealFunction function;

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return function.value(x);
        }

        @Override
        public double derivative(double x) throws FunctionEvaluationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Test
    public void testBounds() throws Exception {
        UnivariateRealFunction f = new Function("x^2", x -> x * x);
        SimpleBounds bounds = new SimpleBounds(1.0, 3.0);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, bounds);
        Assert.assertEquals(2.0, result.getPoint(), 1e-8);
        Assert.assertEquals(3.0 * 3.0 - 16.0, result.getValue(), 1e-8);
    }
}