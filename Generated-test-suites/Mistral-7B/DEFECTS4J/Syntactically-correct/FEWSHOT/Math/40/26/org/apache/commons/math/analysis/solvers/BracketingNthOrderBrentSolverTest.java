package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Before;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        function = mock(UnivariateFunction.class);
    }

    @Test
    public void testConstructorWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        new BracketingNthOrderBrentSolver(1e-3, 1e-6, 5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeAndAbsoluteAndFunctionValueAccuracy() {
        new BracketingNthOrderBrentSolver(1e-3, 1e-6, 5e-7, 5);
    }

    @Test
    public void testGetMaximalOrder() {
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithDefaults() throws Exception {
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(-0.5);

        double result = solver.solve(100, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(-0.5, result, 1e-6);
    }

    @Test
    public void testSolveWithMinAndMax() throws Exception {
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(-0.5);

        double result = solver.solve(100, function, -1.0, 1.0);
        assertEquals(-0.5, result, 1e-6);
    }

    @Test
    public void testSolveWithMinAndMaxAndStartValue() throws Exception {
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(-0.5);

        double startValue = 1.5;
        double result = solver.solve(100, function, -1.0, 1.0, startValue, AllowedSolution.ANY_SIDE);
        assertEquals(-0.5, result, 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() {
        solver.solve(100, function, -1.0, 1.0, new AllowedSolution(-1));
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() throws Exception {
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(-0.5);

        double functionValueAccuracy = 1e-7;
        double result = solver.solve(100, function, -1.0, 1.0, AllowedSolution.ANY_SIDE, functionValueAccuracy);
        assertEquals(-0.5, result, functionValueAccuracy);
    }

    @Test
    public void testGuessXWithValidArguments() {
        double[] x = new double[10];
        double[] y = new double[10];
        double targetY = 0.5;

        for (int i = 0; i < x.length; i++) {
            x[i] = i;
            y[i] = Math.pow(i, 2);
        }

        double result = solver.guessX(targetY, x, y, 0, x.length);
        assertEquals(Math.sqrt(targetY), result, 1e-6);
    }

    @Test
    public void testGuessXWithNaN() {
        double[] x = new double[2];
        double[] y = new double[2];
        double targetY = 0.0;

        x[0] = 1.0;
        x[1] = 1.0;
        y[0] = 1.0;
        y[1] = 1.0;

        double result = solver.guessX(targetY, x, y, 0, x.length);
        assertTrue(Double.isNaN(result));
    }
}