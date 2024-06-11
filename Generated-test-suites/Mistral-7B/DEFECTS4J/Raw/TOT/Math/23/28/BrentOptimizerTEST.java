package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testBrentOptimizer() {
        final double function = MathConstants.E;
        final double relativeThreshold = 1e-6;
        final double absoluteThreshold = 1e-6;
        final double x1 = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        PointValuePair result = optimizer.optimize(x -> x * x - function, new SimpleBounds(x1 - 2, x1 + 2));

        assertEquals(x1, result.getPoint(), 1e-6);
        assertEquals(function, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testBrentOptimizerWithCustomChecker() {
        final double function = MathConstants.E;
        final double relativeThreshold = 1e-6;
        final double absoluteThreshold = 1e-6;
        final double x1 = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, x -> true);
        PointValuePair result = optimizer.optimize(x -> x * x - function, new SimpleBounds(x1 - 2, x1 + 2));

        assertEquals(x1, result.getPoint(), 1e-6);
        assertEquals(function, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsThreshold() {
        final double relativeThreshold = 1e-6;
        final double absoluteThreshold = -1e-6;

        new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithRelativeThresholdTooSmall() {
        final double relativeThreshold = 1e-8;
        final double absoluteThreshold = 1e-6;

        new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, SimpleBounds)}.
     */
    @Test
    public void testBrentOptimizerWithFunctionThrowsException() {
        final double relativeThreshold = 1e-6;
        final double absoluteThreshold = 1e-6;
        final double x1 = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold) {
            @Override
            protected UnivariatePointValuePair doOptimize() {
                throw new RuntimeException("Test exception");
            }
        };

        try {
            PointValuePair result = optimizer.optimize(x -> {
                throw new RuntimeException("Function exception");
            }, new SimpleBounds(x1 - 2, x1 + 2));
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, SimpleBounds)}.
     */
    @Test
    public void testBrentOptimizerWithBoundariesTooClose() {
        final double function = MathConstants.E;
        final double relativeThreshold = 1e-6;
        final double absoluteThreshold = 1e-6;
        final double x1 = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        PointValuePair result = optimizer.optimize(x -> x * x - function, new SimpleBounds(x1, x1));

        assertNull(result);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, SimpleBounds)}.
     */
    @Test
    public void testBrentOptimizerWithBoundsNotDefined() {
        final double function = MathConstants.E;
        final double relativeThreshold = 1e-6;
        final double absoluteThreshold = 1e-6;
        final double x1 = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        PointValuePair result = optimizer.optimize(x -> x * x - function, null);

        assertNull(result);
    }
}