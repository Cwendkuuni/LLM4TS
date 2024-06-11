package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketingBrentMethod;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;
        double expectedSolution = 0.25;

        UnivariateRealSolver solver = new BrentSolver(new BracketingBrentMethod());
        double actualSolution = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expectedSolution, actualSolution, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;

        // Configure the mock function to throw a ConvergenceException
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.solve(any(UnivariateRealFunction.class), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("Test ConvergenceException"));
        UnivariateRealSolverUtils.setup(function);
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;

        // Configure the mock function to throw a FunctionEvaluationException
        when(function.value(anyDouble())).thenThrow(new FunctionEvaluationException("Test FunctionEvaluationException"));
        UnivariateRealSolverUtils.setup(function);
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.001;
        double expectedSolution = 0.25;

        UnivariateRealSolver solver = new BrentSolver(new BracketingBrentMethod());
        double actualSolution = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(expectedSolution, actualSolution, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() throws FunctionEvaluationException {
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(null, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double x0 = 1.0;
        double x1 = 0.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        double[] expectedBracketingPoints = {-0.1, 0.2};

        UnivariateRealSolver solver = new BrentSolver(new BracketingBrentMethod());
        double[] actualBracketingPoints = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(expectedBracketingPoints, actualBracketingPoints, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        // Configure the mock function to throw a ConvergenceException
        when(function.value(anyDouble())).thenThrow(new ConvergenceException("Test ConvergenceException"));
        UnivariateRealSolverUtils.setup(function);
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        // Configure the mock function to throw a FunctionEvaluationException
        when(function.value(anyDouble())).thenThrow(new FunctionEvaluationException("Test FunctionEvaluationException"));
        UnivariateRealSolverUtils.setup(function);
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() throws FunctionEvaluationException {
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        UnivariateRealSolverUtils.bracket(null, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketMaxIterationsExceededException() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        int maximumIterations = 1;
        double[] expectedBracketingPoints = {-0.1, 0.2};

        when(function.value(eq(initial))).thenReturn(0.0);
        when(function.value(eq(lowerBound))).thenReturn(Math.E);
        when(function.value(gt(initial))).thenReturn(Math.E);

        UnivariateRealSolverUtils.setup(function);
        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        verify(function).value(eq(initial));
        verify(function).value(eq(lowerBound));
        verify(function).value(gt(initial));
    }
}