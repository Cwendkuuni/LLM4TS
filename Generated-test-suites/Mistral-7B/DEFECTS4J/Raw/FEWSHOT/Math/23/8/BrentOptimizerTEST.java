package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithDefaults() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0);
        UnivariatePointValuePair result = optimizer.optimize(new MyFunction(), 1.0, 2.0);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(-2.332293551443743E-16, result.getValue(), 1e-16);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, new MyConvergenceChecker());
        UnivariatePointValuePair result = optimizer.optimize(new MyFunction(), 1.0, 2.0);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(-2.332293551443743E-16, result.getValue(), 1e-16);
    }

    @Test
    public void testBrentOptimizerWithMinAndMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        UnivariatePointValuePair result = optimizer.optimize(new MyFunction(), -5.0, 5.0);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(-2.332293551443743E-16, result.getValue(), 1e-16);
    }

    @Test
    public void testBrentOptimizerWithMinOnly() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, null);
        UnivariatePointValuePair result = optimizer.optimize(new MyFunction(), 1.0);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(-2.332293551443743E-16, result.getValue(), 1e-16);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNegativeAbsThreshold() {
        new BrentOptimizer(0.01, -0.01, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithRelativeThresholdLessThanMachineEps() {
        new BrentOptimizer(0.0, 0.0, null);
    }

    @Test
    public void testBrentOptimizerFailsToConverge() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        OptimizationResult result = optimizer.optimize(new MyFunctionWithNoMinima(), 1.0, 2.0);
        assertNull(result.getPoint());
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
            return (iteration > 100);
        }
    }

    private static class OptimizationResult {
        private double point;
        private double value;

        public OptimizationResult(double point, double value) {
            this.point = point;
            this.value = value;
        }

        public double getPoint() {
            return point;
        }

        public double getValue() {
            return value;
        }
    }
}