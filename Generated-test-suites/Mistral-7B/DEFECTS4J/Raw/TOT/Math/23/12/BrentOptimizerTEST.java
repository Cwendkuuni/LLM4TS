package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.test.EqualsTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    private UnivariateFunction testFunction;
    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new MyFunction();
        optimizer = new BrentOptimizer(1e-6, 1e-3);
    }

    @Test
    public void testSimpleMinimization() {
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointsValuePair result = optimizer.optimize(testFunction, bounds);
        Assert.assertEquals(0.5, result.getPoint(), 1e-6);
        Assert.assertEquals(-0.25, result.getValue(), 1e-6);
    }

    @Test
    public void testSimpleMaximization() {
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        optimizer = new BrentOptimizer(1e-6, 1e-3, GoalType.MAXIMIZE);
        PointsValuePair result = optimizer.optimize(testFunction, bounds);
        Assert.assertEquals(-0.5, result.getPoint(), 1e-6);
        Assert.assertEquals(0.25, result.getValue(), 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.optimize(testFunction, bounds);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        optimizer = new BrentOptimizer(1e-12, 1e-12, 10);
        optimizer.optimize(testFunction, bounds);
    }

    @Test
    public void testNotStrictlyPositiveAbsoluteThreshold() {
        optimizer = new BrentOptimizer(-1.0, 1e-6);
        try {
            optimizer.optimize(testFunction, new SimpleBounds(-10, 10));
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Absolute threshold must be positive", e.getMessage());
        }
    }

    @Test
    public void testNotStrictlyPositiveRelativeThreshold() {
        optimizer = new BrentOptimizer(1e-6, -1.0);
        try {
            optimizer.optimize(testFunction, new SimpleBounds(-10, 10));
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Relative threshold must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testAroundMinimumInitializer() {
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        optimizer = new BrentOptimizer(1e-6, 1e-3);
        AroundMinimumInitializer initializer = new AroundMinimumInitializer(0.5);
        PointsValuePair result = optimizer.optimize(testFunction, bounds, initializer);
        EqualsTester tester = new EqualsTester();
        tester.addEqualityGroup(result.getPoint(), 0.5, 1e-6);
        tester.addEqualityGroup(result.getValue(), -0.25, 1e-6);
        tester.testEquals();
    }

    @Test
    public void testUserDefinedConvergenceChecker() {
        ConvergenceChecker<PointsValuePair> checker = new MyConvergenceChecker();
        optimizer = new BrentOptimizer(1e-6, 1e-3, checker);
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        optimizer.optimize(testFunction, bounds);
    }

    /**
     * Test function for {@link BrentOptimizer}.
     */
    private static class MyFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) / (1 + x * x);
        }

        @Override
        public double derivative(double x) {
            double sinX = Math.sin(x);
            double cosX = Math.cos(x);
            return (cosX - x * sinX) / (1 + x * x);
        }
    }

    /**
     * Test convergence checker for {@link BrentOptimizer}.
     */
    private static class MyConvergenceChecker
            implements ConvergenceChecker<PointsValuePair> {

        @Override
        public boolean converged(int iteration,
                                PointsValuePair previous,
                                PointsValuePair current) {
            return false;
        }
    }
}