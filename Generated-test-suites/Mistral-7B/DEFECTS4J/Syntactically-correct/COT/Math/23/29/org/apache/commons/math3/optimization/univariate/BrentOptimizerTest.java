package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        // Initialize the test data.
        function = new MyFunction();
    }

    @Test
    public void testEmptyConstructor() {
        try {
            new BrentOptimizer();
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testRelativeAbsConstructorNullChecker() {
        try {
            new BrentOptimizer(0.1d, 0.0d, null);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testRelativeAbsConstructorInvalidRel() {
        try {
            new BrentOptimizer(-0.1d, 0.1d, null);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testRelativeAbsConstructorInvalidAbs() {
        try {
            new BrentOptimizer(0.1d, -0.1d, null);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testRelativeAbsConstructorValid() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, null);
        Assert.assertEquals(1.0d, optimizer.getRelativeThreshold(), 0.001d);
        Assert.assertEquals(0.01d, optimizer.getAbsoluteThreshold(), 0.001d);
    }

    @Test
    public void testRelativeAbsConstructorWithChecker() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, new MyConvergenceChecker());
        Assert.assertEquals(1.0d, optimizer.getRelativeThreshold(), 0.001d);
        Assert.assertEquals(0.01d, optimizer.getAbsoluteThreshold(), 0.001d);
    }

    @Test
    public void testRelativeAbsConstructorInvalidRel() {
        try {
            new BrentOptimizer(0.1d, 0.1d, null);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testMinimizeSimpleFunction() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, null);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5d, 1.5d, GoalType.MINIMIZE);
        Assert.assertEquals(0.75d, result.getPoint(), 0.001d);
        Assert.assertEquals(-0.24254953140235323d, result.getValue(), 1e-10);
    }

    @Test
    public void testMinimizeFunctionWithLocalMinimum() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, null);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5d, 1.5d, GoalType.MINIMIZE);
        Assert.assertEquals(1.0d, result.getPoint(), 0.001d);
        Assert.assertEquals(0.0d, result.getValue(), 1e-10);
    }

    @Test
    public void testMaximizeSimpleFunction() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5d, 1.5d);
        Assert.assertEquals(1.25d, result.getPoint(), 0.001d);
        Assert.assertEquals(1.0d, result.getValue(), 1e-10);
    }

    @Test
    public void testMaximizeFunctionWithLocalMaximum() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5d, 1.5d);
        Assert.assertEquals(0.5d, result.getPoint(), 0.001d);
        Assert.assertEquals(1.0d, result.getValue(), 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, null);
        optimizer.setGoalType(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        optimizer = new BrentOptimizer(1.0d, 0.01d, null);
        optimizer.optimize(null, 0.5d, 1.5d);
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 0.75, 2) + Math.sin(2 * Math.PI * x) - 0.25;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    }
}