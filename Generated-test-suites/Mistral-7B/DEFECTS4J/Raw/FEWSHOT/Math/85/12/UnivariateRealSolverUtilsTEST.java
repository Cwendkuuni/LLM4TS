package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        double x0 = 1.0;
        double x1 = 2.0;
        double solution = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertTrue(Math.abs(solution - 1.5) < 0.001);

        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        double x2 = 1.0;
        double x3 = 2.0;
        try {
            UnivariateRealSolverUtils.solve(function2, x2, x3);
            assertTrue(false);
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double x0 = 0.0;
        double x1 = Math.PI / 2.0;
        double absoluteAccuracy = 0.001;
        double solution = UnivariateRealSolverUtils.solve(function, x0, x1,
                absoluteAccuracy);
        assertTrue(Math.abs(solution - 0.5) < absoluteAccuracy);

        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        double x2 = 1.0;
        double x3 = 2.0;
        double absoluteAccuracy2 = 0.001;
        try {
            UnivariateRealSolverUtils.solve(function2, x2, x3, absoluteAccuracy2);
            assertTrue(false);
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealFunction function = null;
        double x0 = 1.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0;
            }
        };
        double x0 = Double.POSITIVE_INFINITY;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithInvalidFunction() throws FunctionEvaluationException {
        UnivariateRealFunction function = null;
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound,
                upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInitial() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double initial = Double.NEGATIVE_INFINITY;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound,
                upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidIntervalBounds()
            throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double initial = 0.0;
        double lowerBound = 2.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound,
                upperBound);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        double[] result = UnivariateRealSolverUtils.bracket(function,
                initial, lowerBound, upperBound);
        assertArrayEquals(new double[] { -1.4142135623730955, 1.4142135623730955 },
                result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithMaxIterations()
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        int maximumIterations = 0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound,
                upperBound, maximumIterations);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertTrue(Math.abs(result - expected) < 0.001);
    }
}