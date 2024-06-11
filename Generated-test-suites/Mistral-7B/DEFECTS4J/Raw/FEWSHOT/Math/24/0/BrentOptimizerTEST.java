package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    /**
     * Test constructor with relative and absolute threshold and ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizerWithRelativeAndAbsoluteThresholdAndChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5, null);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test constructor with relative and absolute threshold.
     */
    @Test
    public void testBrentOptimizerWithRelativeAndAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testBrentOptimizerDefaultConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(null);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with GoalType.MINIMIZE.
     */
    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with GoalType.MAXIMIZE.
     */
    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with wrong GoalType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWrongGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.setGoalType(GoalType.SADDLE_POINT);
        optimizer.optimize(function, 0.0, 1.0);
    }

    /**
     * Test optimization with a null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.optimize(null, 0.0, 1.0);
    }

    /**
     * Test optimization in the interval [0, 1].
     */
    @Test
    public void testBrentOptimizerInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization in the interval [-1, 0].
     */
    @Test
    public void testBrentOptimizerNegativeInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, -1.0, 0.0);
        assertEquals(-0.5, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization in the interval [1, 2].
     */
    @Test
    public void testBrentOptimizerBigInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, 2.0);
        assertEquals(1.5, result.getPoint(), 1e-6);
        assertEquals(-0.75, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with an incorrect interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerIncorrectInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.optimize(function, 1.0, 0.0);
    }

    /**
     * Test optimization with a converged point.
     */
    @Test
    public void testBrentOptimizerConvergedPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.setConvergenceChecker((iter, previous, current) -> iter >= 10);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a non-converged point.
     */
    @Test
    public void testBrentOptimizerNonConvergedPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.setConvergenceChecker((iter, previous, current) -> false);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertNull(result);
    }

    /**
     * Test optimization with a null convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(function, 0.0, 1.0);
    }

    /**
     * Function used for optimization testing.
     */
    private double function(double x) {
        return Math.pow(x - 0.5, 2);
    }
}