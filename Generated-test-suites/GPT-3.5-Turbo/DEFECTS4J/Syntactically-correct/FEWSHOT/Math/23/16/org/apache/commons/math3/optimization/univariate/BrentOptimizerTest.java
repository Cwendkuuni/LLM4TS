package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    private final double ABS_TOLERANCE = 1e-6;
    private final double REL_TOLERANCE = 1e-6;

    @Test
    public void testDoOptimize_Minimize() {
        // Setup
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(0.5)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double expectedOptimum = 0.0;

        // Execute
        BrentOptimizer optimizer = new BrentOptimizer(REL_TOLERANCE, ABS_TOLERANCE);
        UnivariatePointValuePair result = optimizer.optimize(new MaxEval(100),
                                                             new UnivariateObjectiveFunction(function),
                                                             GoalType.MINIMIZE,
                                                             new SearchInterval(min, max),
                                                             new InitialGuess(startValue));

        // Verify
        assertEquals(expectedOptimum, result.getPoint(), ABS_TOLERANCE);
    }

    @Test
    public void testDoOptimize_Maximize() {
        // Setup
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(0.0)).thenReturn(1.0);
        when(function.value(0.5)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(-1.0);
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double expectedOptimum = 1.0;

        // Execute
        BrentOptimizer optimizer = new BrentOptimizer(REL_TOLERANCE, ABS_TOLERANCE);
        UnivariatePointValuePair result = optimizer.optimize(new MaxEval(100),
                                                             new UnivariateObjectiveFunction(function),
                                                             GoalType.MAXIMIZE,
                                                             new SearchInterval(min, max),
                                                             new InitialGuess(startValue));

        // Verify
        assertEquals(expectedOptimum, result.getPoint(), ABS_TOLERANCE);
    }
}