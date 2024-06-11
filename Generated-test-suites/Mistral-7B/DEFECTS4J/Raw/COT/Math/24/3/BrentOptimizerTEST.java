package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = mock(UnivariateFunction.class);
        optimizer = new BrentOptimizer(0.1, 0.001, null);
    }

    @Test
    public void testEmptyConstructor() {
        try {
            new BrentOptimizer();
            fail("Expected NotStrictlyPositiveException or NumberIsTooSmallException");
        } catch (Exception e) {
            if (e instanceof NotStrictlyPositiveException) {
                verifyZeroInteractions(function);
            } else if (e instanceof NumberIsTooSmallException) {
                verifyZeroInteractions(function);
            } else {
                fail("Unexpected exception: " + e.getClass().getName());
            }
        }
    }

    @Test
    public void testRelativeAndAbsoluteThresholdsConstructor() {
        optimizer = new BrentOptimizer(0.1, 0.001);
        verify(function, times(0)).value(anyDouble());
    }

    @Test
    public void testOptimizeMinimum() {
        when(function.value(anyDouble())).thenAnswer(invocation -> Math.pow(invocation.getArgument(0), 2));

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 0.001);
        verify(function, times(5)).value(anyDouble());
    }

    @Test
    public void testOptimizeMaximum() {
        when(function.value(anyDouble())).thenAnswer(invocation -> -Math.pow(invocation.getArgument(0), 2));

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, GoalType.MAXIMIZE);
        assertEquals(1.0, result.getPoint(), 0.001);
        verify(function, times(5)).value(anyDouble());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(0.1, -0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdSmallerThanTwoUlp() {
        optimizer = new BrentOptimizer(0.0, 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        optimizer = new BrentOptimizer(0.1, 0.001, null);
        optimizer.setConvergenceChecker(null);
    }
}