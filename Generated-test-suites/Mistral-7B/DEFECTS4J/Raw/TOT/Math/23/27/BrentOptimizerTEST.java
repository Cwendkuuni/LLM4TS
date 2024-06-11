package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.test.AbstractTestCase;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends AbstractTestCase {

    /**
     * Test function for {@link BrentOptimizer}.
     */
    private static class TestFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) - 0.5 * x * x;
        }

        @Override
        public double derivative(double x) {
            return -2.0 * x - Math.cos(x);
        }
    }

    /**
     * Test optimization of {@link TestFunction} with default settings.
     */
    @Test
    public void testDefaultSettings() {
        final double[] expected = {-1.4142135625, -0.530637825};
        final double absTol = 1e-9;
        final double relTol = 1e-5;
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, null);
        final TestFunction function = new TestFunction();

        final UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);

        assertTrue(isClose(expected[0], result.getX(), absTol));
        assertTrue(isClose(expected[1], result.getValue(), absTol));
    }

    /**
     * Test optimization of {@link TestFunction} with custom settings.
     */
    @Test
    public void testCustomSettings() {
        final double[] expected = {-1.4142135625, -0.530637825};
        final double absTol = 1e-10;
        final double relTol = 1e-6;
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, null);
        final TestFunction function = new TestFunction();

        final UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);

        assertTrue(isClose(expected[0], result.getX(), absTol));
        assertTrue(isClose(expected[1], result.getValue(), absTol));
    }

    /**
     * Test optimization of {@link TestFunction} with a non-converging function.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergingFunction() {
        final BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        final TestFunction function = new TestFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        optimizer.optimize(function, -1.0, 1.0);
    }

    /**
     * Test optimization of {@link TestFunction} with negative absolute tolerance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAbsTolerance() {
        final BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6, null);
    }

    /**
     * Test optimization of {@link TestFunction} with relative tolerance less than machine precision.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeTolerance() {
        final BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-6, null);
    }

    /**
     * Test optimization of {@link TestFunction} with invalid range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRange() {
        final BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        final TestFunction function = new TestFunction();

        optimizer.optimize(function, Double.NEGATIVE_INFINITY, -1.0);
    }

    /**
     * Test if two doubles are close.
     *
     * @param d1 First double.
     * @param d2 Second double.
     * @param tolerance Tolerance.
     * @return {@code true} if the doubles are close, otherwise {@code false}.
     */
    private boolean isClose(double d1, double d2, double tolerance) {
        return Math.abs(d1 - d2) <= tolerance;
    }
}