package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverFactory;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(solver.solve(function, Double.MIN_VALUE, 0.0)).thenReturn(0.0);

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);
        when(factory.newDefaultSolver()).thenReturn(solver);

        UnivariateRealSolverUtils.setup(function);

        double root = utils.solve(function, Double.MIN_VALUE, 0.0);
        assertTrue(root == 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvalException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);

        UnivariateRealSolverUtils.setup(function);

        utils.solve(function, Double.MIN_VALUE, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));
        when(solver.solve(function, Double.MIN_VALUE, 0.0)).thenThrow(new ConvergenceException("Test exception"));

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);
        when(factory.newDefaultSolver()).thenReturn(solver);

        UnivariateRealSolverUtils.setup(function);

        utils.solve(function, Double.MIN_VALUE, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws Exception {
        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealFunction function = null;

        utils.solve(function, Double.MIN_VALUE, 0.0);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(solver.solve(function, Double.MIN_VALUE, 0.0)).thenReturn(0.0);
        when(solver.setAbsoluteAccuracy(0.0)).thenReturn(solver);

        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);
        when(factory.newDefaultSolver()).thenReturn(solver);

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverUtils.setup(function);

        double root = utils.solve(function, Double.MIN_VALUE, 0.0, 0.0);
        assertTrue(root == 0.0);
    }

    @Test
    public void testSolveWithIllegalArgumentForAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(solver.solve(function, Double.MIN_VALUE, 0.0)).thenReturn(0.0);
        when(solver.setAbsoluteAccuracy(-1.0)).thenThrow(new MathRuntimeException("Test exception"));

        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);
        when(factory.newDefaultSolver()).thenReturn(solver);

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverUtils.setup(function);

        try {
            utils.solve(function, Double.MIN_VALUE, 0.0, -1.0);
            fail("Expected MathRuntimeException to be thrown");
        } catch (MathRuntimeException e) {
            // Pass
        }
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(solver.solve(function, Double.MIN_VALUE, 0.0)).thenReturn(0.0);

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);
        when(factory.newDefaultSolver()).thenReturn(solver);

        double[] bracket = utils.bracket(function, 0.0, 1.0, 1.0, Integer.MAX_VALUE);
        assertArrayEquals(new double[]{0.0, 0.0}, bracket);

        when(function.value(Double.MIN_VALUE)).thenReturn(-1.0);
        bracket = utils.bracket(function, 0.0, 1.0, 1.0, Integer.MAX_VALUE);
        assertArrayEquals(new double[]{0.0, 0.0}, bracket);

        when(function.value(Double.MIN_VALUE)).thenReturn(-1.0);
        when(function.value(1.0)).thenReturn(1.0);
        bracket = utils.bracket(function, 0.0, 1.0, 1.0, Integer.MAX_VALUE);
        assertArrayEquals(new double[]{0.0, 1.0}, bracket);

        when(function.value(Double.MIN_VALUE)).thenReturn(-1.0);
        when(function.value(1.0)).thenReturn(-1.0);
        bracket = utils.bracket(function, 0.0, 1.0, 1.0, Integer.MAX_VALUE);
        assertArrayEquals(new double[]{Double.MIN_VALUE, 1.0}, bracket);

        when(function.value(Double.MIN_VALUE)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(1.0);
        try {
            bracket = utils.bracket(function, 0.0, 1.0, 1.0, 0);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            // Pass
        }
    }

    @Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(solver.solve(function, Double.MIN_VALUE, 0.0)).thenReturn(0.0);

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);
        when(factory.newDefaultSolver()).thenReturn(solver);

        double[] bracket = utils.bracket(function, 0.0, 1.0, 1.0, 1);
        assertFalse(bracket != null);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketWithFunctionEvalException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();

        try {
            utils.bracket(function, 0.0, 1.0, 1.0, Integer.MAX_VALUE);
            fail("Expected FunctionEvaluationException to be thrown");
        } catch (FunctionEvaluationException e) {
            // Pass
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
        when(function.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test exception"));
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(solver.solve(function, Double.MIN_VALUE, 0.0)).thenThrow(new ConvergenceException("Test exception"));

        UnivariateRealSolverFactory factory = mock(UnivariateRealSolverFactory.class);
        when(factory.newDefaultSolver()).thenReturn(solver);

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();
        UnivariateRealSolverUtils.setup(function);

        try {
            utils.bracket(function, 0.0, 1.0, 1.0, Integer.MAX_VALUE);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            // Pass
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws Exception {
        UnivariateRealFunction function = null;

        UnivariateRealSolverUtils utils = new UnivariateRealSolverUtils();

        try {
            utils.bracket(function, 0.0, 1.0, 1.0, Integer.MAX_VALUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}