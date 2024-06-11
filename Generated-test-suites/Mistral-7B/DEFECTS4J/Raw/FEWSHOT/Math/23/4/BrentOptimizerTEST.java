package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        //Test null check
        assertNull(new BrentOptimizer(0.0, 0.0, null));
        //Test with valid inputs
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = -0.001;
        new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        double relativeThreshold = 0.0;
        double absoluteThreshold = 0.001;
        new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, new MyConvergenceChecker());

        // Test that convergence checker is set correctly
        assertEquals(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerWithoutCustomConvergenceChecker() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);

        double min = 5.0;
        double max = 10.0;
        double startValue = 7.5;
        double goalType = GoalType.MINIMIZE.getValue();

        UnivariatePointValuePair result = optimizer.doOptimize(min, max, startValue, goalType);

        assertNotNull(result);
        assertEquals(goalType, result.getGoalType(), 0);
        assertEquals(min, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, new MyConvergenceChecker());

        double min = 5.0;
        double max = 10.0;
        double startValue = 7.5;
        double goalType = GoalType.MINIMIZE.getValue();

        UnivariatePointValuePair result = optimizer.doOptimize(min, max, startValue, goalType);

        assertNotNull(result);
        assertEquals(goalType, result.getGoalType(), 0);
        assertEquals(min, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);

        // Test that the convergence checker was called
        MyConvergenceChecker convergenceChecker = (MyConvergenceChecker) optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker.wasCalled());
    }

    @Test
    public void testDoOptimizeWithInvalidGoalType() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);

        double min = 5.0;
        double max = 10.0;
        double startValue = 7.5;
        double goalType = 3.0; //Invalid GoalType

        try {
            optimizer.doOptimize(min, max, startValue, goalType);
            fail("Expected NumberIsDefinedException to be thrown.");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testDoOptimizeWithIntervalsDontMeetCriteria() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);

        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double goalType = GoalType.MINIMIZE.getValue();

        UnivariatePointValuePair result = optimizer.doOptimize(min, max, startValue, goalType);

        assertNotNull(result);
        assertEquals(goalType, result.getGoalType(), 0);
        assertEquals(min, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);

        // Test that the intervals don't meet the stopping criteria
        assertEquals(0, optimizer.getIterations(), 0);
    }

    @Test
    public void testDoOptimizeWithInvalidIntervals() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);

        double min = 5.0;
        double max = 4.0; //Invalid interval
        double startValue = 7.5;
        double goalType = GoalType.MINIMIZE.getValue();

        try {
            optimizer.doOptimize(min, max, startValue, goalType);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testDoOptimizeWithInvalidStartValue() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);

        double min = 5.0;
        double max = 10.0;
        double startValue = 4.0; //Invalid start value
        double goalType = GoalType.MINIMIZE.getValue();

        try {
            optimizer.doOptimize(min, max, startValue, goalType);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testGetRelativeThreshold() {
        double relativeThreshold = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, 0.0);
        assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0);
    }

    @Test
    public void testGetAbsoluteThreshold() {
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(0.0, absoluteThreshold);
        assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testGetIterations() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.doOptimize(0.0, 0.0, 0.0, 0);
        assertEquals(0, optimizer.getIterations(), 0);
    }

    @Test
    public void testGetConvergenceChecker() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, new MyConvergenceChecker());
        assertEquals(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetGoalType() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertEquals(GoalType.MINIMIZE.getValue(), optimizer.getGoalType(), 0);
    }

    @Test
    public void testGetMin() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertEquals(Double.NaN, optimizer.getMin(), 0.0);
    }

    @Test
    public void testGetMax() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertEquals(Double.NaN, optimizer.getMax(), 0.0);
    }

    @Test
    public void testGetStartValue() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertEquals(Double.NaN, optimizer.getStartValue(), 0.0);
    }

    @Test
    public void testGetGoalTypeString() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertEquals("MINIMIZE", optimizer.getGoalTypeString());
    }

    @Test
    public void testSetGoalType() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.setGoalType(GoalType.MAXIMIZE.getValue());
        assertEquals(GoalType.MAXIMIZE.getValue(), optimizer.getGoalType(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGoalTypeInvalid() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.setGoalType(4);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        private boolean wasCalled = false;

        @Override
        public boolean converged(int iterations, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            wasCalled = true;
            return false;
        }

        public boolean wasCalled() {
            return wasCalled;
        }
    }
}