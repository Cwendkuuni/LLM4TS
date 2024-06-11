package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test(expected = MathRuntimeException.class)
    public void testBracketFunctionNull() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = null;
        double initial = 0;
        double lowerBound = -1;
        double upperBound = 1;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = MathRuntimeException.class)
    public void testBracketInvalidParameters() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0;
        double lowerBound = 2;
        double upperBound = 1;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = MathRuntimeException.class)
    public void testBracketOutOfRange() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0;
        double lowerBound = Double.NEGATIVE_INFINITY;
        double upperBound = Double.POSITIVE_INFINITY;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0;
        double lowerBound = -1;
        double upperBound = 1;
        double[] expected = {-1, 1};
        when(function.value(-1)).thenReturn(-1.0);
        when(function.value(1)).thenReturn(1.0);
        assertArrayEquals(expected, UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound), 0.0001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testBracketMaxIterationsExceeded() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0;
        double lowerBound = -1;
        double upperBound = 1;
        int maximumIterations = 5;
        when(function.value(-1)).thenReturn(-1.0);
        when(function.value(1)).thenReturn(1.0);
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test(expected = MathRuntimeException.class)
    public void testMidpoint() {
        double a = 0;
        double b = 2;
        assertEquals(1.0, UnivariateRealSolverUtils.midpoint(a, b), 0.0001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testSetupFunctionNull() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
}