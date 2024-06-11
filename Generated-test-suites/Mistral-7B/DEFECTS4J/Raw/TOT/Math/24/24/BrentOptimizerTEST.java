package org.apache.commons.math3.optimization.univariate;

import junit.framework.TestCase;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends TestCase {

    /**
     * Test for {@code BrentOptimizer#BrentOptimizer(double, double)}
     * constructor without additional {@code ConvergenceChecker}.
     */
    public void testBrentOptimizerSimple() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new MyFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
        assertEquals(3.0, result.getPoint(), 1e-5);
    }

    /**
     * Test for {@code BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * constructor with custom {@code ConvergenceChecker}.
     */
    public void testBrentOptimizerWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new ConvergenceChecker<UnivariatePointValuePair>() {
                @Override
                public boolean converged(int iteration,
                                         UnivariatePointValuePair prev,
                                         UnivariatePointValuePair current) {
                    double prevValue = prev.getValue();
                    double currentValue = current.getValue();
                    return prevValue > currentValue && Math.abs(prevValue - currentValue) < 1e-7;
                }
            };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, checker);
        UnivariateFunction function = new MyFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
        assertEquals(3.0, result.getPoint(), 1e-5);
    }

    /**
     * Test error case for {@code BrentOptimizer#BrentOptimizer(double, double)}
     * constructor with negative absolute threshold.
     */
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        try {
            new BrentOptimizer(1e-5, -1e-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test error case for {@code BrentOptimizer#BrentOptimizer(double, double)}
     * constructor with too small relative threshold.
     */
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        try {
            new BrentOptimizer(1e-8, 1e-5);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test error case for {@code BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * constructor with null {@code ConvergenceChecker}.
     */
    public void testBrentOptimizerNullChecker() {
        try {
            new BrentOptimizer(1e-5, 1e-5, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test function for Brent Optimizer.
     */
    private static class MyFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 6 * x + 9;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 6;
        }
    }
}