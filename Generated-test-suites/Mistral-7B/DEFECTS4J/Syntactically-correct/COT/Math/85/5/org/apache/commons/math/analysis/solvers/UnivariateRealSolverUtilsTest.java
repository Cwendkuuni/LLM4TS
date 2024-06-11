package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertDoubleEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BracketingBrentSolver;
import org.apache.commons.math3.analysis.solvers.LBracketer;
import org.apache.commons.math3.analysis.solvers.SimpleBracketingStrategy;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the UnivariateRealSolverUtils class.
 */
@RunWith(JUnit4.class)
public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;

        double solution = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertDoubleEquals("Solve should find square root of 4", 2.0, solution, 1e-6);

        // Test with invalid input
        UnivariateRealFunction invalidFunction = null;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(invalidFunction, x0, x1));

        x0 = 0.0;
        x1 = 2.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, x1, x0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x0));

        // Test with convergence exception
        UnivariateRealFunction convergenceFunction = new UnivariateRealFunction() {
            private int count = 0;

            @Override
            public double value(double x) {
                count++;
                if (count > 1000) {
                    throw new ConvergenceException("Reached maximum iterations");
                }
                return Math.sin(x) / x;
            }
        };

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(convergenceFunction, x0, x1));

        // Test with functionEvaluation exception
        UnivariateRealFunction functionWithException = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("FunctionEvaluationException");
            }
        };

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(functionWithException, x0, x1));
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2.71828;
            }
        };

        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-6;

        double solution = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        double e = Precision.absoluteError(2.71828, solution);
        assertDoubleEquals("Solve with absolute accuracy should find e", e, solution);

        // Test with invalid input
        double invalidAbsoluteAccuracy = -1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1, invalidAbsoluteAccuracy));
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        double[] bracketing = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals("Bracket should return valid bracketing", new double[]{initial - 1.0, initial + 1.0}, bracketing);

        // Test with invalid input
        UnivariateRealFunction invalidFunction = null;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(invalidFunction, initial, lowerBound, upperBound));

        double invalidInitial = Double.NaN;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, invalidInitial, lowerBound, upperBound));

        double invalidLowerBound = upperBound;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, invalidLowerBound, upperBound));

        double invalidUpperBound = lowerBound;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, invalidUpperBound));

        // Test with convergence exception
        UnivariateRealFunction convergenceFunction = new UnivariateRealFunction() {
            private int count = 0;

            @Override
            public double value(double x) {
                count++;
                if (count > 1000) {
                    throw new ConvergenceException("Reached maximum iterations");
                }
                return Math.sin(x) / x;
            }
        };

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(convergenceFunction, initial, lowerBound, upperBound));

        // Test with no bracketing exception
        UnivariateRealFunction noBracketingFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        double[] noBracketingBracketing = UnivariateRealSolverUtils.bracket(noBracketingFunction, initial, lowerBound, upperBound);
        assertThrows(NoBracketingException.class, () -> UnivariateRealSolverUtils.bracket(noBracketingFunction, initial, lowerBound, upperBound));

        // Test with functionEvaluation exception
        UnivariateRealFunction functionWithException = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("FunctionEvaluationException");
            }
        };

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(functionWithException, initial, lowerBound, upperBound));
    }

    @Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 5;

        double[] bracketing = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations);
        assertArrayEquals("Bracket with max iterations should find valid bracketing", new double[]{initial - 1.0, initial + 1.0}, bracketing);

        // Test with invalid input
        int invalidMaxIterations = -1;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, invalidMaxIterations));

        // Test with convergence exception
        UnivariateRealFunction convergenceFunction = new UnivariateRealFunction() {
            private int count = 0;

            @Override
            public double value(double x) {
                count++;
                if (count > 1000) {
                    throw new ConvergenceException("Reached maximum iterations");
                }
                return Math.sin(x) / x;
            }
        };

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(convergenceFunction, initial, lowerBound, upperBound, maxIterations));

        // Test with functionEvaluation exception
        UnivariateRealFunction functionWithException = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("FunctionEvaluationException");
            }
        };

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(functionWithException, initial, lowerBound, upperBound, maxIterations));
    }

    @Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;

        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertDoubleEquals("Midpoint should be the average of a and b", (a + b) / 2.0, midpoint);
    }

}