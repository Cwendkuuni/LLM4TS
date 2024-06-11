package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingBrentSolver;
import org.apache.commons.math.analysis.solvers.LaguerreSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolver solver = new BrentSolver();

        when(function.value(x0)).thenReturn(2.0);
        when(function.value(x1)).thenReturn(-1.0);

        double expectedResult = 0.75;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1);

        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;

        when(function.value(x0)).thenReturn(Double.NaN);
        when(function.value(x1)).thenReturn(Double.NaN);

        UnivariateRealSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(1.0e-10);

        UnivariateRealSolverUtils.solve(function, x0, x1, 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;

        when(function.value(x0)).thenThrow(new FunctionEvaluationException(""));

        UnivariateRealSolver solver = new BrentSolver();

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;

        when(function.value(x0)).thenReturn(1.5);
        when(function.value(x1)).thenReturn(0.5);

        UnivariateRealSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(absoluteAccuracy);

        double expectedResult = 1.125;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);

        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolver solver = new BrentSolver();

        UnivariateRealSolverUtils.solve(null, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveLowerBoundGreaterThanUpperBound() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 2.0;
        double x1 = 1.0;

        UnivariateRealSolver solver = new BrentSolver();

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        UnivariateRealSolver solver = new BracketingBrentSolver();

        when(function.value(initial)).thenReturn(0.0);

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        when(function.value(initial)).thenThrow(new FunctionEvaluationException(""));

        UnivariateRealSolver solver = new BracketingBrentSolver();

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 1.0;
        double lowerBound = 2.0;
        double upperBound = 0.0;

        UnivariateRealSolver solver = new BracketingBrentSolver();

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketSuccess() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 100;

        UnivariateRealSolver solver = new BracketingBrentSolver();

        when(function.value(initial)).thenReturn(1.0);

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);

        double expectedResultA = 0.5;
        double expectedResultB = 1.5;

        assertArrayEquals(new double[]{expectedResultA, expectedResultB}, result);
        assertTrue(solver.isBracketing(expectedResultA, expectedResultB));
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 1;

        UnivariateRealSolver solver = new BracketingBrentSolver();

        when(function.value(initial)).thenReturn(0.0);

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;

        double expectedResult = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(expectedResult, result, 0.001);
    }
}