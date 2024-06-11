package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test Brent Optimizer.
 */
public class BrentOptimizerTest {
    /**
     * Test for the constructor with positive value for absolute threshold.
     */
    @Test
    public void testBrentOptimizerPositiveAbsThreshold() {
        UnivariateFunction func = new MyUnivariateFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateOptimizer result = optimizer.optimize(func, new SimpleBounds(-10.0, 10.0), null);
        assertNotNull(result);
        PointValuePair expected = new PointValuePair(0.0, 0.0);
        assertEquals(expected, result);
    }

    /**
     * Test for the constructor with non-positive value for absolute threshold.
     */
    @Test
    public void testBrentOptimizerNegativeAbsThreshold() {
        UnivariateFunction func = new MyUnivariateFunction();
        assertThrows(NotStrictlyPositiveException.class, () -> {
            new BrentOptimizer(0.01, -0.001, null);
        });
    }

    /**
     * Test for the constructor with a relative threshold smaller than machine epsilon.
     */
    @Test
    public void testBrentOptimizerSmallRelativeThreshold() {
        UnivariateFunction func = new MyUnivariateFunction();
        assertThrows(NumberIsTooSmallException.class, () -> {
            new BrentOptimizer(0.0, 0.00001, null);
        });
    }

    /**
     * Test for the minimize method with a UnivariateFunction that has one minimum.
     */
    @Test
    public void testBrentOptimizerMinimumExists() {
        UnivariateFunction func = new MyUnivariateFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateOptimizer result = optimizer.optimize(func, new SimpleBounds(-10.0, 10.0), null);
        assertNotNull(result);
        PointValuePair expected = new PointValuePair(0.0, 0.0);
        assertEquals(expected, result);
    }

    /**
     * Test for the minimize method with a UnivariateFunction that has no minimum or maximum.
     */
    @Test
    public void testBrentOptimizerNoMinimumOrMaximum() {
        UnivariateFunction func = new MyNoMinimumOrMaximumFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateOptimizer result = optimizer.optimize(func, new SimpleBounds(-10.0, 10.0), null);
        assertNull(result);
    }

    /**
     * Test for the minimize method with a UnivariateFunction that has multiple minima.
     */
    @Test
    public void testBrentOptimizerMultipleMinima() {
        UnivariateFunction func = new MyMultipleMinimaFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateOptimizer result = optimizer.optimize(func, new SimpleBounds(-10.0, 10.0), null);
        PointValuePair expected = new PointValuePair(-2.0, 1.5);
        assertEquals(expected, result);
    }

    /**
     * Test for the minimize method with goal type set to MAXIMIZE.
     */
    @Test
    public void testBrentOptimizerMaximize() {
        UnivariateFunction func = new MyUnivariateFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariateOptimizer result = optimizer.optimize(func, new SimpleBounds(-10.0, 10.0), null);
        assertNotNull(result);
        PointValuePair expected = new PointValuePair(5.0, 25.0);
        assertEquals(expected, result);
    }

    /**
     * Custom UnivariateFunction for testing.
     */
    private static class MyUnivariateFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 4 * x + 4;
        }
    }

    /**
     * Custom UnivariateFunction for testing with no minimum or maximum.
     */
    private static class MyNoMinimumOrMaximumFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    }

    /**
     * Custom UnivariateFunction for testing with multiple minima.
     */
    private static class MyMultipleMinimaFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            double result = Math.exp(-x) * (Math.cos(x) - 1);
            return result * result;
        }
    }
}