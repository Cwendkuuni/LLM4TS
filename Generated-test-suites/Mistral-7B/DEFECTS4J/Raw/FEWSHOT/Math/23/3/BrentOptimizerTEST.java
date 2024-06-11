package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.001, null);
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.001);
        BrentOptimizer optimizer3 = new BrentOptimizer(0.001, 0.01, null);
        BrentOptimizer optimizer4 = new BrentOptimizer(0.001, 0.01);

        assertEquals(GoalType.MINIMIZE, optimizer1.getGoalType());
        assertEquals(0.01, optimizer1.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer1.getAbsoluteThreshold(), 0.0001);

        assertEquals(GoalType.MINIMIZE, optimizer2.getGoalType());
        assertEquals(0.01, optimizer2.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer2.getAbsoluteThreshold(), 0.0001);

        assertEquals(GoalType.MAXIMIZE, optimizer3.getGoalType());
        assertEquals(0.001, optimizer3.getRelativeThreshold(), 0.001);
        assertEquals(0.01, optimizer3.getAbsoluteThreshold(), 0.001);

        assertEquals(GoalType.MAXIMIZE, optimizer4.getGoalType());
        assertEquals(0.001, optimizer4.getRelativeThreshold(), 0.001);
        assertEquals(0.01, optimizer4.getAbsoluteThreshold(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        new BrentOptimizer(-0.01, 0.01, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerZeroAbsoluteThreshold() {
        new BrentOptimizer(0.01, 0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.01, null);
    }

    @Test
    public void testBrentOptimizerDefaultValues() {
        BrentOptimizer optimizer = new BrentOptimizer(null, null, null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1.4e-8, optimizer.getRelativeThreshold(), 1e-8);
        assertEquals(1.4e-8, optimizer.getAbsoluteThreshold(), 1e-8);
    }

    @Test
    public void testBrentOptimizerNoConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(
                (x) -> Math.pow(x, 2),
                0, 2, null);

        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, (iter, a, b) -> {
            if (iter > 50) {
                return true;
            }
            return false;
        });

        UnivariatePointValuePair result = optimizer.optimize(
                (x) -> Math.pow(x, 2),
                0, 2, null);

        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);

        assertEquals(5, optimizer.getIterations());
    }

    @Test
    public void testBrentOptimizerFunctionMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        UnivariatePointValuePair result = optimizer.optimize(
                (x) -> Math.pow(x, 2) - 4 * x + 4,
                0, 2, null);

        assertEquals(1.5, result.getX(), 1e-6);
        assertEquals(-3.5, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerFunctionMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        UnivariatePointValuePair result = optimizer.optimize(
                (x) -> Math.pow(x, 2) - 4 * x + 4,
                2, 0, null);

        assertEquals(-1.5, result.getX(), 1e-6);
        assertEquals(3.5, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerFunctionNoSolution() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        UnivariatePointValuePair result = optimizer.optimize(
                (x) -> Math.sin(x) * Math.sin(x),
                0, 1, null);

        assertNull(result);
    }

    @Test
    public void testBrentOptimizerFunctionTwovalued() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        UnivariatePointValuePair result = optimizer.optimize(
                (x) -> Math.pow(x, 2) - Math.pow(Math.sin(x), 2),
                0, 1, null);

        assertEquals(0.675655530137514, result.getX(), 1e-6);
        assertEquals(-0.36846375852051, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerFunctionInflectionPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        UnivariatePointValuePair result = optimizer.optimize(
                (x) -> Math.pow(x, 3) - 6 * x * x + 9 * x - 2,
                0, 3, null);

        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}