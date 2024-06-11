package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test constructor with relative and absolute thresholds, and null convergence checker.
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNotNull(optimizer);

        // Test constructor with relative and absolute thresholds only.
        optimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.001, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        new BrentOptimizer(0.001, 0.001, null);
    }

    @Test
    public void testDoOptimize() {
        // Test optimization with a simple function.
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, null);
        Function function = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        UnivariateOptimizerResult result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.0, result.getPoint().getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeConvergenceChecker() {
        // Test optimization with a simple function and a custom convergence checker.
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 1e-6;
            }
        });
        Function function = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
        };
        UnivariateOptimizerResult result = optimizer.optimize(function, 0.0, 2.0);
        assertEquals(1.0, result.getPoint().getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeIterations() {
        // Test optimization with a simple function and a large number of iterations.
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, null);
        Function function = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariateOptimizerResult result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(0.0, result.getPoint().getValue(), 0.001);
        assertEquals(3.14159265359, result.getPoint().getX(), 0.001);
    }

    private interface Function {
        double value(double x);
    }
}