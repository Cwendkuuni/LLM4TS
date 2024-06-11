package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testConstructor() {
        double rel = 0.1;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ZeroChecker<>();
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        assertEquals(rel, optimizer.getRelativeThreshold(), EPSILON);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), EPSILON);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        double rel = 1e-15;
        double abs = 0.01;
        new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        double rel = 0.1;
        double abs = 0;
        new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, new ZeroChecker<>());
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0, result.getPoint(), EPSILON);
        assertEquals(0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimizeWithInvalidInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        // Try an interval with a lower bound greater than the upper bound
        optimizer.setMin(10);
        optimizer.setMax(0);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        try {
            optimizer.doOptimize();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid interval"));
        }
    }

    private static class ZeroChecker<T extends UnivariatePointValuePair> implements ConvergenceChecker<T> {

        @Override
        public boolean converged(int iteration, T previous, T current) {
            return Math.abs(current.getValue()) < EPSILON;
        }
    }
}