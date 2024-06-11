package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(optimizer.getMin());
        assertNotNull(optimizer.getMax());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(1e-5, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        new BrentOptimizer(1e-8, 1e-6);
    }

    @Test
    public void testDoOptimize_FunctionValueAtEndPoints() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        double functionAtMin = optimizer.computeObjectiveValue(optimizer.getMin());
        double functionAtMax = optimizer.computeObjectiveValue(optimizer.getMax());
        assertTrue(functionAtMin > functionAtMax);
    }

    @Test
    public void testDoOptimize_FunctionValueAtInitialPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        double functionAtInitialPoint = optimizer.computeObjectiveValue(optimizer.getStartValue());
        assertFalse(optimizer.isOptimized());
        assertNotEquals(optimizer.getBestPoint().getValue(), functionAtInitialPoint);
    }

    @Test
    public void testDoOptimize_FunctionValueAtMidPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        double functionAtMidPoint = optimizer.computeObjectiveValue(optimizer.getStartValue() + (optimizer.getMax() - optimizer.getMin()) / 2);
        assertFalse(optimizer.isOptimized());
        assertNotEquals(optimizer.getBestPoint().getValue(), functionAtMidPoint);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        optimizer.optimize();
        assertTrue(optimizer.isOptimized());
        assertNotNull(optimizer.getBestPoint());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize_FunctionValueAtBestPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        optimizer.optimize();
        double functionAtBestPoint = optimizer.computeObjectiveValue(optimizer.getBestPoint().getPoint());
        assertEquals(optimizer.getBestPoint().getValue(), functionAtBestPoint);
    }

    @Test
    public void testDoOptimize_FunctionValueAtPreviousBestPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        optimizer.optimize();
        UnivariatePointValuePair previousBestPoint = optimizer.getPreviousBestPoint();
        double functionAtPreviousBestPoint = 0;
        if (previousBestPoint != null) {
            functionAtPreviousBestPoint = optimizer.computeObjectiveValue(previousBestPoint.getPoint());
        }
        assertNotEquals(optimizer.getBestPoint().getValue(), functionAtPreviousBestPoint);
    }

    @Test
    public void testDoOptimize_ConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize();
        verify(convergenceChecker).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));
    }
}