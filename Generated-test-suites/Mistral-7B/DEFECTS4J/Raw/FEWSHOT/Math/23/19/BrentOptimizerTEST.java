package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        FunctionMinimizer functionMinimizer = new FunctionMinimizer() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }
        };

        UnivariateOptimizerResult result = optimizer.optimize(functionMinimizer, -3.0, 3.0);
        Assert.assertEquals(-1.0, result.getPoint().getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithCustomChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, new MyConvergenceChecker());
        FunctionMinimizer functionMinimizer = new FunctionMinimizer() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }
        };

        UnivariateOptimizerResult result = optimizer.optimize(functionMinimizer, -3.0, 3.0);
        Assert.assertEquals(-1.0, result.getPoint().getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);
        FunctionMinimizer functionMinimizer = new FunctionMinimizer() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }
        };

        UnivariateOptimizerResult result = optimizer.optimize(functionMinimizer, 0.5, 3.0);
        Assert.assertEquals(-1.0, result.getPoint().getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithOutOfRangeInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);
        FunctionMinimizer functionMinimizer = new FunctionMinimizer() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }
        };

        try {
            optimizer.optimize(functionMinimizer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizationWithNegativeAbsoluteThreshold() {
        try {
            new BrentOptimizer(1e-5, -1e-6, null);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizationWithTooSmallRelativeThreshold() {
        try {
            new BrentOptimizer(-1e-7, 1e-6, null);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    interface FunctionMinimizer {
        double value(double x);
    }

    static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return previous != null && current != null && Math.abs(previous.getValue() - current.getValue()) < 1e-6;
        }
    }
}