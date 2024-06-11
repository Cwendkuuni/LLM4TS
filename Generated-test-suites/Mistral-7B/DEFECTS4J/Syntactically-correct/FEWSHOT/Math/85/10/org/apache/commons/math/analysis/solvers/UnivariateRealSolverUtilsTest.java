package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        double[] bracketingValues = new double[]{-1.0, 1.0};
        double expectedResult = 0.0;
        double result = UnivariateRealSolverUtils.solve(function, bracketingValues[0], bracketingValues[1]);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double[] bracketingValues = new double[]{-1.0, 1.0};
        UnivariateRealSolverUtils.solve(function, bracketingValues[0], bracketingValues[1]);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        double[] bracketingValues = new double[]{-1.0, 1.0};
        UnivariateRealSolverUtils.solve(function, bracketingValues[0], bracketingValues[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = null;
        double[] bracketingValues = new double[]{-1.0, 1.0};
        UnivariateRealSolverUtils.solve(function, bracketingValues[0], bracketingValues[1]);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double[] bracketingValues = new double[]{-1.0, 1.0};
        double absoluteAccuracy = 0.001;
        double expectedResult = -2.0;
        double result = UnivariateRealSolverUtils.solve(function, bracketingValues[0], bracketingValues[1], absoluteAccuracy);
        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        fail("Expected ConvergenceException");
    }

    @Test
    public void testBracketSuccess() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        double expectedA = -1.5;
        double expectedB = 1.5;
        assertArrayEquals(new double[]{expectedA, expectedB}, bracketingValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException2() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 0.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException3() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 0.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException4() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        int maximumIterations = 0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expectedResult = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expectedResult, result, 0.001);
    }
}