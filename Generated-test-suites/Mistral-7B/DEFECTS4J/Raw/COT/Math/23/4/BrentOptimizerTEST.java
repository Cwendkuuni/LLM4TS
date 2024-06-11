package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.BrentOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test cases for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {
    // A simple quadratic function for testing.
    private static class QuadraticFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new QuadraticFunction();
        optimizer = new BrentOptimizer(1.0e-6, 0.0, null);
    }

    @Test
    public void testBrentOptimizer_Minimum() {
        double expected = 0.0;
        UnivariatePointValuePair result = optimizer.optimize(function, 2.0, GoalType.MINIMIZE);
        Assert.assertEquals(expected, result.getPoint(), 1e-9);
        Assert.assertEquals(0.0, result.getValue(), 1e-9);
    }

    @Test
    public void testBrentOptimizer_Maximum() {
        double expected = 4.0;
        optimizer = new BrentOptimizer(1.0e-6, 0.0, null);
        UnivariatePointValuePair result = optimizer.optimize(function, -2.0, GoalType.MAXIMIZE);
        Assert.assertEquals(expected, result.getPoint(), 1e-9);
        Assert.assertEquals(expected * expected, result.getValue(), 1e-9);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_SmallRelativeThreshold() {
        optimizer = new BrentOptimizer(1.0e-9, 0.0, null);
        optimizer.optimize(function, 0.0, GoalType.MINIMIZE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_SmallAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1.0e-6, -1.0e-9, null);
        optimizer.optimize(function, 0.0, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_NegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1.0e-6, -0.5, null);
        optimizer.optimize(function, 0.0, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_NegativeRelativeThreshold() {
        optimizer = new BrentOptimizer(-1.0e-6, 0.5, null);
        optimizer.optimize(function, 0.0, GoalType.MINIMIZE);
    }
}