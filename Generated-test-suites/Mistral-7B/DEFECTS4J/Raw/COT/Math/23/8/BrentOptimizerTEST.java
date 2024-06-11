package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_BasicFunctionality() {
        UnivariateFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        PointValuePair result = optimizer.optimize(function, 1.0);
        Assert.assertEquals(0.0, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizer_CustomGoalType() {
        UnivariateFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(function, -1.0);
        Assert.assertEquals(-2.0, result.getPoint(), 0.001);
        Assert.assertEquals(4.0, result.getValue(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_NegativeAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.001, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_RelativeThresholdTooSmall() {
        new BrentOptimizer(0.0, 0.001, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_NullFunction() {
        new BrentOptimizer(0.01, 0.001, null).optimize(null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_NullInitialGuess() {
        UnivariateFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        new BrentOptimizer(0.01, 0.001, null).optimize(function, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_ConvergenceCheckerNull() {
        UnivariateFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        new BrentOptimizer(0.01, 0.001, null).optimize(function, 1.0, null);
    }

    @Test
    public void testBrentOptimizer_CustomConvergenceChecker() {
        UnivariateFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new CustomConvergenceChecker());
        PointValuePair result = optimizer.optimize(function, 1.0);
        Assert.assertEquals(0.0, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            if (iteration > 10) {
                return true;
            }
            return false;
        }
    }
}