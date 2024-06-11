package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Spy;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Setup
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariateObjectiveFunction objectiveFunction = mock(UnivariateObjectiveFunction.class);
        when(objectiveFunction.value(0)).thenReturn(-1000.0);
        when(objectiveFunction.value(5)).thenReturn(0.0);
        when(objectiveFunction.value(10)).thenReturn(-1000.0);
        optimizer.setObjectiveFunction(objectiveFunction);

        // Test
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Verify
        assertNotNull(result);
        assertEquals(5.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
        verify(objectiveFunction, times(3)).value(anyDouble());
    }

    @Test
    public void testDoOptimize_noObjectiveFunction() {
        // Setup
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Test
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Verify
        assertNull(result);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_relTooSmall() {
        new BrentOptimizer(1e-8, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absNegative() {
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test
    public void testConstructor_absZero() {
        // Setup
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0);

        // Verify
        assertNotNull(optimizer);
    }
}