package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.PI;
            }
        };

        double expectedResult = 1.23456;
        double x0 = 0.0;
        double x1 = 2.0;

        double result = UnivariateRealSolverUtils.solve(function, x0, x1);

        assertEquals(expectedResult, result, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveThrowsConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error evaluating function");
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveThrowsIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = null;

        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.PI;
            }
        };

        double expectedResult = 1.23456;
        double x0 = 0.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.0001;

        double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);

        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.PI;
            }
        };

        double x0 = 0.0;
        double x1 = 2.0;
        double absoluteAccuracy = -0.0001;

        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

        assertTrue(bracketingValues[0] < bracketingValues[1]);
        assertTrue(function.value(bracketingValues[0]) * function.value(bracketingValues[1]) < 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketThrowsConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error evaluating function");
            }
        };

        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketThrowsIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.PI;
            }
        };

        double initial = 1.0;
        double lowerBound = 2.0;
        double upperBound = 0.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.PI;
            }
        };

        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 5;

        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);

        assertTrue(bracketingValues[0] < bracketingValues[1]);
        assertTrue(function.value(bracketingValues[0]) * function.value(bracketingValues[1]) <= 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketThrowsConvergenceExceptionWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.PI;
            }
        };

        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
    }
}