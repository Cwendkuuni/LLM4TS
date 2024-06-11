package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.NoBracketException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    /**
     * Test the {@code BrentOptimizer} with a simple test function:
     * {@code f(x) = x^2}.
     */
    @Test
    public void testBrentOptimizerSimpleFunction() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] x = {1.1, 2.0, 3.0, 4.0, 5.0};
        double[] expected = {2.0, 2.0, 2.0, 2.0, 2.0};
        for (int i = 0; i < x.length; ++i) {
            PointValuePair result = optimizer.optimize(x[i], null, null, null, GoalType.MINIMIZE);
            assertNotNull(result);
            assertEquals(expected[i], result.getPoint(), 1e-9);
        }
    }

    /**
     * Test the {@code BrentOptimizer} with a test function that has
     * multiple local minimums: {@code f(x) = (x - 1)^3}.
     */
    @Test
    public void testBrentOptimizerFunctionMultipleMinima() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] x = {-1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5};
        double[] expected = {-1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 1.0};
        for (int i = 0; i < x.length; ++i) {
            PointValuePair result = optimizer.optimize(x[i], null, null, null, GoalType.MINIMIZE);
            assertNotNull(result);
            assertEquals(expected[i], result.getPoint(), 1e-9);
        }
    }

    /**
     * Test the {@code BrentOptimizer} with a test function that
     * has no local minimum in the given interval.
     */
    @Test(expected = NoBracketException.class)
    public void testBrentOptimizerFunctionNoMinima() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] x = {1.0, 2.0, 3.0};
        for (double start : x) {
            optimizer.optimize(start, null, null, null, GoalType.MINIMIZE);
        }
    }

    /**
     * Test the {@code BrentOptimizer} with a test function that
     * has a local maximum in the given interval.
     */
    @Test
    public void testBrentOptimizerFunctionMaxima() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] x = {-1.0, 0.0, 1.0};
        double[] expected = {-1.0, 0.0, 1.0};
        for (int i = 0; i < x.length; ++i) {
            PointValuePair result = optimizer.optimize(x[i], null, null, null, GoalType.MAXIMIZE);
            assertNotNull(result);
            assertEquals(expected[i], result.getPoint(), 1e-9);
        }
    }

    /**
     * Test the {@code BrentOptimizer} with a test function
     * that has a singularity in the given interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerFunctionSingularity() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(0.0, null, null, null, GoalType.MINIMIZE);
    }

    /**
     * Test the {@code BrentOptimizer} with an invalid relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        UnivariateOptimizer optimizer = new BrentOptimizer(0.0, 1e-6);
    }

    /**
     * Test the {@code BrentOptimizer} with an invalid absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
    }
}