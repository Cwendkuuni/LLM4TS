package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeRelativeThreshold() {
        new BrentOptimizer(-1.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithTooSmallAbsoluteThreshold() {
        new BrentOptimizer(1.0, 0.0);
    }

    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair expected = new UnivariatePointValuePair(0.0, 0.0);
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setConvergenceChecker(mock(ConvergenceChecker.class));
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.setGoalType(goalType);
        assertEquals(expected, optimizer.doOptimize());

        optimizer = new BrentOptimizer(1.0, 1.0, mock(ConvergenceChecker.class));
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(goalType);
        assertEquals(expected, optimizer.doOptimize());

        optimizer = new BrentOptimizer(1.0, 1.0, mock(ConvergenceChecker.class));
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        assertNotNull(optimizer.doOptimize());

        optimizer = new BrentOptimizer(1.0, 1.0, null);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        assertNotNull(optimizer.doOptimize());

        optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        assertNotNull(optimizer.doOptimize());

        optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        assertNotNull(optimizer.doOptimize());
        
        optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        assertNotNull(optimizer.doOptimize());
        
        optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setMin(-1.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        assertNotNull(optimizer.doOptimize());
    }
}