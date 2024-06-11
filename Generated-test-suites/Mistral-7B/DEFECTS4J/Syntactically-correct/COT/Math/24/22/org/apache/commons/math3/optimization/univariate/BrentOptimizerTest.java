package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test BrentOptimizer class.
 */
public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerBasic() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = 0.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-14);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        new BrentOptimizer(1e-6, 1e-8, null);
    }

    @Test
    public void testBrentOptimizerCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, new CustomConvergenceChecker());
        UnivariateFunction f = (x) -> Math.pow(x, 2);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = 0.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-14);
    }

    @Test
    public void testBrentOptimizerGoalTypeMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = 0.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-14);
    }

    @Test
    public void testBrentOptimizerGoalTypeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = 25.0;
        Assert.assertEquals(expected, result.getValue(), 1e-14);
        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @Test
    public void testBrentOptimizerCustomStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2) - 3;
        optimizer.setStartValue(2.0);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = -2.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-14);
    }

    @Test
    public void testBrentOptimizerCustomMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2) - 3;
        optimizer.setMin(-5.0);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = -2.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-14);
    }

    @Test
    public void testBrentOptimizerCustomMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2) - 3;
        optimizer.setMax(5.0);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = -2.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-14);
    }

    @Test
    public void testBrentOptimizerCustomFunctionMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2) + 3;
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = 2.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected * expected, result.getValue(), 1e-14);
    }

    @Test
    public void testBrentOptimizerCustomFunctionMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = (x) -> Math.pow(x, 2) + 3;
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        double expected = -2.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(-expected * expected, result.getValue(), 1e-14);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            if (iteration > 100) {
                return true;
            }
            return false;
        }
    }
}