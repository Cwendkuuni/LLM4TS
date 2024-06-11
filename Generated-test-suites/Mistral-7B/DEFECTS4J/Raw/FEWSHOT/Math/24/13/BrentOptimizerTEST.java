package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, (arg1, arg2, arg3) -> {
        });

        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 3) - 2 * x, -5.0, 5.0);

        double expectedX = -1.5;
        double expectedValue = Math.pow(-1.5, 3) - 2 * (-1.5);
        Assert.assertEquals(expectedX, result.getPoint(), 0.0001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, (arg1, arg2, arg3) -> {
        });

        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 3), 0.0, 10.0);

        double expectedX = 2.0;
        double expectedValue = Math.pow(2.0, 3);
        Assert.assertEquals(expectedX, result.getPoint(), 0.0001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, (arg1, arg2, arg3) -> {
        });

        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 3), 0.0, 10.0, GoalType.MAXIMIZE);

        double expectedX = 2.0;
        double expectedValue = Math.pow(2.0, 3);
        Assert.assertEquals(expectedX, result.getPoint(), 0.0001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, new MyConvergenceChecker());

        UnivariatePointValuePair result = optimizer.optimize(x -> Math.sin(x), 0.0, 4.0);

        double expectedX = 0.3562;
        double expectedValue = Math.sin(0.3562);
        Assert.assertEquals(expectedX, result.getPoint(), 0.0001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.01, (arg1, arg2, arg3) -> {
        });
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithRelativeThresholdTooSmall() {
        BrentOptimizer optimizer = new BrentOptimizer(0.00001, 0.01, (arg1, arg2, arg3) -> {
        });
    }

    @Test
    public void testBrentOptimizerWithEmptyFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, (arg1, arg2, arg3) -> {
        });

        UnivariatePointValuePair result = optimizer.optimize(() -> 0.0, 0.0, 0.0);

        assertNull(result);
    }

    @Test
    public void testBrentOptimizerWithMinimumAndIntervalOutOfOrder() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, (arg1, arg2, arg3) -> {
        });

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimize(x -> Math.pow(x, 3) - 2 * x, 10.0, -5.0);
        });
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return current.getValue() < 0.0001;
        }
    }
}