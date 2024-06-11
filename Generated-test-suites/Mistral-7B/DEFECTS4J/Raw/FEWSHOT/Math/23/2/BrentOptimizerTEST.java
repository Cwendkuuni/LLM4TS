package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertEquals(1e-6, optimizer.relativeThreshold, 0.0);
        assertEquals(1e-6, optimizer.absoluteThreshold, 0.0);
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        assertEquals(1e-6, optimizer.relativeThreshold, 0.0);
        assertEquals(1e-6, optimizer.absoluteThreshold, 0.0);
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new BrentOptimizer(1e-6, 0.1);
        assertEquals(1e-6, optimizer.relativeThreshold, 0.0);
        assertEquals(0.1, optimizer.absoluteThreshold, 0.0);
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new BrentOptimizer(0.1, 1e-6);
        assertEquals(0.1, optimizer.relativeThreshold, 0.0);
        assertEquals(1e-6, optimizer.absoluteThreshold, 0.0);
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new BrentOptimizer(0.1, 0.1, null);
        assertEquals(0.1, optimizer.relativeThreshold, 0.0);
        assertEquals(0.1, optimizer.absoluteThreshold, 0.0);
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        assertEquals(1e-6, optimizer.relativeThreshold, 0.0);
        assertEquals(1e-6, optimizer.absoluteThreshold, 0.0);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerAbsoluteThresholdNegative() {
        new BrentOptimizer(0, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        new BrentOptimizer(0.000001, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsoluteThresholdZero() {
        new BrentOptimizer(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerZeroConvergenceChecker() {
        new BrentOptimizer(1e-6, 1e-6, (ConvergenceChecker<UnivariatePointValuePair>) () -> {
            throw new IllegalArgumentException();
        });
    }

    @Test
    public void testBrentOptimizerDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2), 0, 2);
        assertEquals(0.0, result.getX(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);

        optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        result = optimizer.optimize(x -> Math.pow(x, 2), 0, 2);
        assertEquals(0.0, result.getX(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);

        optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        result = optimizer.optimize(x -> Math.sin(x), -10, 10);
        assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyCustomConvergenceChecker(2));
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2), 0, 2);
        assertEquals(0.0, result.getX(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
        assertTrue(optimizer.getIterations() > 10);

        optimizer = new BrentOptimizer(1e-6, 1e-6, new MyCustomConvergenceChecker(5));
        result = optimizer.optimize(x -> Math.pow(x, 2), 0, 2);
        assertEquals(0.0, result.getX(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
        assertTrue(optimizer.getIterations() > 15);
    }

    interface ConvergenceChecker<T> {
        boolean converged(int iterations, T previous, T current);
    }

    static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iterations, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return previous != null && current != null && previous.getValue() >= current.getValue();
        }
    }

    static class MyCustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        private final int maxIterations;

        public MyCustomConvergenceChecker(int maxIterations) {
            this.maxIterations = maxIterations;
        }

        @Override
        public boolean converged(int iterations, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return iterations >= maxIterations;
        }

        public int getIterations() {
            return maxIterations;
        }
    }
}