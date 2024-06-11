package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double x0 = 0.0;
        double x1 = 1.57; // Pi

        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertTrue(Math.abs(Math.PI - result) < 0.001);

        result = UnivariateRealSolverUtils.solve(function, x0, x1, 0.001);
        assertTrue(Math.abs(Math.PI - result) < 0.0001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws FunctionEvaluationException {
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
    public void testSolveIllegalArgumentException() {
        UnivariateRealFunction function = null;

        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        double x0 = 1.0;
        double x1 = 0.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.57; // Pi

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

        assertArrayEquals(new double[]{lowerBound, initial}, result);
    }

    @Test
    public void testBracketConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                double value = Math.sin(x) - 0.5;
                if (value == 0.0) {
                    throw new ConvergenceException("Root found");
                }
                return value;
            }
        };

        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 0.0;

        assertNotNull(UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Error evaluating function");
            }
        };

        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 0.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 0.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(1.5, result, 0.0);
    }
}