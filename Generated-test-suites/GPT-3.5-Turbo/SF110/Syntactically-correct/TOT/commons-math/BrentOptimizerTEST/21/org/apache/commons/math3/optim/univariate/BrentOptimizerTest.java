package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-6;

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-10, 1e-10);
    }

    // Test constructor with valid parameters
    @Test
    public void testConstructorValidParameters() {
        assertEquals(1e-10, optimizer.getRelativeThreshold(), EPSILON);
        assertEquals(1e-10, optimizer.getAbsoluteThreshold(), EPSILON);
    }

    // Test constructor with invalid parameters (rel < MIN_RELATIVE_TOLERANCE)
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        optimizer = new BrentOptimizer(1e-15, 1e-10);
    }

    // Test constructor with invalid parameters (abs <= 0)
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        optimizer = new BrentOptimizer(1e-10, -1e-10);
    }

    // Test optimize method
    @Test
    public void testOptimize() {
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x - 4 * x + 3);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        PointValuePair result = optimizer.optimize(100, objective, goalType, min, max);
        double expectedX = 2.0;
        double expectedY = -1.0;
        assertEquals(expectedX, result.getPoint(), EPSILON);
        assertEquals(expectedY, result.getValue(), EPSILON);
    }

    // Test optimize method with invalid range (min > max)
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidRange() {
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 10;
        double max = 0;
        optimizer.optimize(100, objective, goalType, min, max);
    }

    // Test optimize method with invalid goal type
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidGoalType() {
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        GoalType goalType = null;
        double min = 0;
        double max = 10;
        optimizer.optimize(100, objective, goalType, min, max);
    }

}