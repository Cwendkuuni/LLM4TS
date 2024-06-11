package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() {
        UnivariateRealFunction function = new TestFunction();
        try {
            double solution = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
            assertEquals(0.5, solution, 1e-8);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction function = new TestFunction();
        try {
            double solution = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, 1e-6);
            assertEquals(0.5, solution, 1e-7);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveNullFunction() {
        UnivariateRealFunction function = null;
        try {
            UnivariateRealSolverUtils.solve(function, 0.0, 0.0);
        } catch (ConvergenceException e) {
            throw new RuntimeException("Unexpected ConvergenceException", e);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveInvalidInterval() {
        UnivariateRealFunction function = new TestFunction();
        try {
            UnivariateRealSolverUtils.solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } catch (ConvergenceException e) {
            throw new RuntimeException("Unexpected ConvergenceException", e);
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = new TestFunction();
        double[] brackets;
        try {
            brackets = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
            assertArrayEquals(new double[]{0.0, 0.5}, brackets, 1e-8);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testBracketMaxIterations() {
        UnivariateRealFunction function = new TestFunction();
        try {
            double[] brackets = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 1);
            assertNull(brackets);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionNull() {
        UnivariateRealFunction function = null;
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0, 100);
        } catch (ConvergenceException e) {
            throw new RuntimeException("Unexpected ConvergenceException", e);
        }
    }

    @Test
    public void testBracketInvalidInterval() {
        UnivariateRealFunction function = new TestFunction();
        try {
            UnivariateRealSolverUtils.bracket(function, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 100);
            assertFalse(true);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // expected
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergence() {
        UnivariateRealFunction function = new TestFunction();
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.5, 0.5, 10000);
        } catch (FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected FunctionEvaluationException", e);
        }
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 1.0;
        }
    }

}