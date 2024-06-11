package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testConstructorWithRelAndAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), EPSILON);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbs() {
        new BrentOptimizer(0.01, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelTooSmall() {
        new BrentOptimizer(0.0, 1.0);
    }

    @Test
    public void testConstructorWithRelAndNullChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0, null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), EPSILON);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), EPSILON);
    }

    @Test
    public void testConstructorWithRelAbsAndChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0, new MaxEval(100));
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), EPSILON);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), EPSILON);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeMin() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariatePointValuePair result = optimizer.optimize(new SinFunction(), -5.0, 5.0);
        assertEquals(-Math.PI / 2.0, result.getX(), EPSILON);
        assertEquals(-Math.sin(-Math.PI / 2.0), result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeMinWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0, new MaxEval(5));
        UnivariatePointValuePair result = optimizer.optimize(new SinFunction(), -5.0, 5.0);
        assertEquals(-Math.PI / 2.0, result.getX(), EPSILON);
        assertEquals(-Math.sin(-Math.PI / 2.0), result.getValue(), EPSILON);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testOptimizeMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(new SinFunction(), -5.0, 5.0);
        assertEquals(Math.PI / 2.0, result.getX(), EPSILON);
        assertEquals(Math.sin(Math.PI / 2.0), result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeMaxWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0, new MaxEval(5));
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(new SinFunction(), -5.0, 5.0);
        assertEquals(Math.PI / 2.0, result.getX(), EPSILON);
        assertEquals(Math.sin(Math.PI / 2.0), result.getValue(), EPSILON);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testOptimizeWithClosePoints() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair result1 = optimizer.optimize(new AbsFunction(), -10.0, 10.0);
        UnivariatePointValuePair result2 = optimizer.optimize(new AbsFunction(), -10.0, 10.0);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(0.0, 0.0);
        assertArrayEquals(new Object[] { expected.getX(), expected.getValue() },
                new Object[] { result1.getX(), result1.getValue() },
                "Expected and result points are not equal: " + result1);
        assertArrayEquals(new Object[] { expected.getX(), expected.getValue() },
                new Object[] { result2.getX(), result2.getValue() },
                "Expected and result points are not equal: " + result2);
        assertEquals(expected, best(result1, result2, false));
    }

    @Test
    public void testOptimizeWithClosePointsMin() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result1 = optimizer.optimize(new AbsFunction(), -10.0, 10.0);
        UnivariatePointValuePair result2 = optimizer.optimize(new AbsFunction(), -10.0, 10.0);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(0.0, 0.0);
        assertArrayEquals(new Object[] { expected.getX(), expected.getValue() },
                new Object[] { result1.getX(), result1.getValue() },
                "Expected and result points are not equal: " + result1);
        assertArrayEquals(new Object[] { expected.getX(), expected.getValue() },
                new Object[] { result2.getX(), result2.getValue() },
                "Expected and result points are not equal: " + result2);
        assertEquals(expected, best(result1, result2, true));
    }

    @Test
    public void testOptimizeWithClosePointsMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result1 = optimizer.optimize(new AbsFunction(), -10.0, 10.0);
        UnivariatePointValuePair result2 = optimizer.optimize(new AbsFunction(), -10.0, 10.0);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(0.0, 0.0);
        assertArrayEquals(new Object[] { expected.getX(), expected.getValue() },
                new Object[] { result1.getX(), result1.getValue() },
                "Expected and result points are not equal: " + result1);
        assertArrayEquals(new Object[] { expected.getX(), expected.getValue() },
                new Object[] { result2.getX(), result2.getValue() },
                "Expected and result points are not equal: " + result2);
        assertEquals(expected, best(result1, result2, false));
    }

    @Test
    public void testOptimizeWithDifferentGoals() {
        BrentOptimizer optimizerMin = new BrentOptimizer(0.001, 0.001);
        optimizerMin.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair resultMin = optimizerMin.optimize(new AbsFunction(), -10.0, 10.0);

        BrentOptimizer optimizerMax = new BrentOptimizer(0.001, 0.001);
        optimizerMax.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair resultMax = optimizerMax.optimize(new AbsFunction(), -10.0, 10.0);

        UnivariatePointValuePair expectedMin = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair expectedMax = new UnivariatePointValuePair(0.0, 0.0);

        assertArrayEquals(new Object[] { expectedMin.getX(), expectedMin.getValue() },
                new Object[] { resultMin.getX(), resultMin.getValue() },
                "Expected and result points are not equal: " + resultMin);
        assertArrayEquals(new Object[] { expectedMax.getX(), expectedMax.getValue() },
                new Object[] { resultMax.getX(), resultMax.getValue() },
                "Expected and result points are not equal: " + resultMax);
        assertFalse(expectedMin.equals(expectedMax));
        assertFalse(resultMin.equals(resultMax));
    }

    @Test
    public void testOptimizeWithNoPoints() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(new ConstantFunction(0.0), -10.0, 10.0);
        assertNull(result);
    }

    private static UnivariatePointValuePair best(UnivariatePointValuePair a,
            UnivariatePointValuePair b, boolean isMinim) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        if (isMinim) {
            return a.getValue() < b.getValue() ? a : b;
        } else {
            return a.getValue() > b.getValue() ? a : b;
        }
    }

    private static class SinFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class AbsFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.abs(x);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class ConstantFunction implements UnivariateFunction {

        private final double value;

        public ConstantFunction(double value) {
            this.value = value;
        }

        @Override
        public double value(double x) {
            return value;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}