package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.point.Point2D;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test function {@code f(x) = x^3 - 2*x^2 + x + 1}.
     */
    private UnivariateFunction testFunction;

    /**
     * Test constructor with default parameters.
     */
    @Before
    public void setUp() {
        testFunction = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x + 1;
            }
        };
    }

    /**
     * Test {@link PowellOptimizer} with default constructor and no custom
     * convergence checker.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, Double.POSITIVE_INFINITY);
        Assert.assertArrayEquals(new double[]{0.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Test {@link PowellOptimizer} constructor with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, Double.POSITIVE_INFINITY);
        Assert.assertArrayEquals(new double[]{0.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Test {@link PowellOptimizer} with negative absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(1e-6, -1e-6, null);
    }

    /**
     * Test {@link PowellOptimizer} with relative threshold smaller than 2ulps.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() {
        new PowellOptimizer(1e-9, 1e-6, null);
    }

    /**
     * Custom convergence checker that never passes.
     */
    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return false;
        }
    }
}