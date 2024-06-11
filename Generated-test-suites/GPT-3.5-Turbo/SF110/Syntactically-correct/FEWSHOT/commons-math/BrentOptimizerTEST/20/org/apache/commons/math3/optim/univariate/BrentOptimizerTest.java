package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {
    private final double rel = 1e-6;
    private final double abs = 1e-12;

    @Test
    public void testConstructor_ThrowsNumberIsTooSmallExceptionWhenRelIsLessThanMinRelativeTolerance() {
        try {
            new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE - 1, abs);
        } catch (NumberIsTooSmallException e) {
            assertEquals(BrentOptimizer.MIN_RELATIVE_TOLERANCE - 1, e.getArgument(), 0.0);
            assertEquals(BrentOptimizer.MIN_RELATIVE_TOLERANCE, e.getMin(), 0.0);
            assertTrue(e.getBoundIsAllowed());
            assertEquals("org.apache.commons.math3.exception.NumberIsTooSmallException: 0.0 is less than the minimum", e.getMessage());
        }
    }

    @Test
    public void testConstructor_ThrowsNotStrictlyPositiveExceptionWhenAbsIsZero() {
        try {
            new BrentOptimizer(rel, 0);
        } catch (NotStrictlyPositiveException e) {
            assertEquals(0, e.getArgument(), 0.0);
            assertEquals("org.apache.commons.math3.exception.NotStrictlyPositiveException: 0.0", e.getMessage());
        }
    }

    @Test
    public void testDoOptimize_WhenStopConditionIsMet() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(0);
        optimizer.setStartValue(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(mock(ConvergenceChecker.class));

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNull(result);
    }

    @Test
    public void testDoOptimize_WhenStopConditionIsNotMet() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(0);
        optimizer.setStartValue(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(), any())).thenReturn(false, true);
        optimizer.setConvergenceChecker(convergenceChecker);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    @Test
    public void testBest_ReturnsAIfAIsNull() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        boolean isMinim = true;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testBest_ReturnsBIfBIsNull() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = null;
        boolean isMinim = true;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBest_ReturnsAIfAIsLessThanBAndIsMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        boolean isMinim = true;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBest_ReturnsBIfAIsGreaterThanBAndIsMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        boolean isMinim = true;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testBest_ReturnsAIfAIsGreaterThanBAndIsNotMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        boolean isMinim = false;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBest_ReturnsBIfAIsLessThanBAndIsNotMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        boolean isMinim = false;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testBest_ReturnsAIfAIsEqualToBAndIsMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        boolean isMinim = true;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBest_ReturnsAIfAIsEqualToBAndIsNotMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        boolean isMinim = false;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBest_ReturnsBIfAIsNullAndBIsNullAndIsMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        boolean isMinim = true;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertNull(result);
    }

    @Test
    public void testBest_ReturnsBIfAIsNullAndBIsNullAndIsNotMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        boolean isMinim = false;

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertNull(result);
    }

    @Test
    public void testDoOptimize_WhenBestHasNullValues() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(0);
        optimizer.setStartValue(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(), any())).thenReturn(false, true);
        optimizer.setConvergenceChecker(convergenceChecker);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNull(result);
    }

    @Test
    public void testDoOptimize_WhenBestHasNonNullValues() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(0);
        optimizer.setStartValue(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(), any())).thenReturn(false, true);
        optimizer.setConvergenceChecker(convergenceChecker);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    @Test
    public void testComputeObjectiveValue_ReturnsNegatedObjectiveValueIfGoalTypeIsNotMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        double x = 1.0;

        double result = optimizer.computeObjectiveValue(x);

        assertEquals(-1.0, result, Precision.EPSILON);
    }

    @Test
    public void testComputeObjectiveValue_ReturnsObjectiveValueIfGoalTypeIsMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double x = 1.0;

        double result = optimizer.computeObjectiveValue(x);

        assertEquals(1.0, result, Precision.EPSILON);
    }
}