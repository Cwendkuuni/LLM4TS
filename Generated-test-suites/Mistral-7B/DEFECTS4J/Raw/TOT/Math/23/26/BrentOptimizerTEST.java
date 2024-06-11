package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        OptimizableFunction function = new SineFunction();
        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1.5), GoalType.MINIMIZE);
        assertEquals(Math.PI, solution.getPoint(), 1e-7);
        assertEquals(0.0, solution.getValue(), 1e-7);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        new BrentOptimizer(-0.1, 0.1, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        new BrentOptimizer(0.0, 0.001, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001, null);
        optimizer.optimize(null, new InitialGuess(1.5), GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001, null);
        optimizer.optimize(new SineFunction(), null, GoalType.MINIMIZE);
    }

    @Test
    public void testInvalidGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001, null);
        try {
            optimizer.optimize(new SineFunction(), new InitialGuess(1.5), GoalType.MAXIMIZE);
            fail("Should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001, null);
        OptimizableFunction function = new CosineFunction();
        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1.5), GoalType.MAXIMIZE);
        assertEquals(2 * Math.PI, solution.getPoint(), 1e-7);
        assertEquals(1.0, solution.getValue(), 1e-7);
    }

    @FunctionalInterface
    private interface OptimizableFunction {
        double value(double x);
    }

    private static class SineFunction implements OptimizableFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    }

    private static class CosineFunction implements OptimizableFunction {
        @Override
        public double value(double x) {
            return Math.cos(x);
        }
    }
}