package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        // Test constructors with valid arguments.
        BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.001);
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.001, null);

        // Test constructor with an invalid absolute threshold.
        try {
            new BrentOptimizer(-0.001, 0.01);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected.
        }

        // Test constructor with a relative threshold smaller than the minimum.
        try {
            new BrentOptimizer(0.0, 0.001);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected.
        }
    }

    @Test
    public void testBrentOptimizerBasicFunctionality() {
        // Test minimization of a simple function.
        UnivariateFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, 2.0);

        Assert.assertNotNull(result);
        assertEquals(1.5, result.getPoint(), 0.0001);
        assertEquals(-2.25, result.getValue(), 0.0001);

        // Test maximization of a simple function.
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize(function, 1.0, 2.0);

        Assert.assertNotNull(result);
        assertEquals(2.5, result.getPoint(), 0.0001);
        assertEquals(2.25, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        // Test convergence checking with a null checker.
        UnivariateFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        optimizer.setConvergenceChecker(null);
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, 2.0);

        assertNull(result);

        // Test convergence checking with an empty checker.
        ConvergenceChecker<UnivariatePointValuePair> emptyChecker = () -> false;
        optimizer.setConvergenceChecker(emptyChecker);
        result = optimizer.optimize(function, 1.0, 2.0);

        Assert.assertNotNull(result);
        assertEquals(1.5, result.getPoint(), 0.0001);
        assertEquals(-2.25, result.getValue(), 0.0001);

        // Test convergence checking with a custom checker.
        ConvergenceChecker<UnivariatePointValuePair> customChecker = (iter, a, b) -> {
            if (iter > 10) {
                return true;
            }
            return a.getValue() < b.getValue();
        };
        optimizer.setConvergenceChecker(customChecker);
        result = optimizer.optimize(function, 1.0, 2.0);

        Assert.assertNotNull(result);
        assertEquals(1.5, result.getPoint(), 0.0001);
        assertEquals(-2.25, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerEdgeCases() {
        UnivariateFunction function = new EdgeCaseFunction();

        // Test minimization with the interval limits swapped.
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.optimize(function, 2.0, 1.0);

        Assert.assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);

        // Test minimization with the interval limits equal.
        optimizer.setConvergenceChecker(null);
        result = optimizer.optimize(function, 1.0, 1.0);

        assertNull(result);

        // Test minimization with a function that does not change sign.
        optimizer.setConvergenceChecker(new AlwaysFalseChecker());
        result = optimizer.optimize(function, 1.0, 2.0);

        assertNull(result);

        // Test minimization with a function that converges very slowly.
        optimizer.setConvergenceChecker(new SlowChecker());
        result = optimizer.optimize(function, 1.0, 2.0);

        Assert.assertNotNull(result);
        assertEquals(1.5, result.getPoint(), 0.0001);
        assertEquals(-2.25, result.getValue(), 0.0001);
    }

    private static class SimpleFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2;
        }
    }

    private static class EdgeCaseFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x > 0) {
                return Math.pow(x, 2);
            } else {
                return -Math.pow(-x, 2);
            }
        }
    }

    private static class AlwaysFalseChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair a, UnivariatePointValuePair b) {
            return false;
        }
    }

    private static class SlowChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair a, UnivariatePointValuePair b) {
            if (iter > 100) {
                return true;
            }
            return false;
        }
    }
}