package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PsolverRequiredException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BracketedBrentSolver;
import org.apache.commons.math3.solver.LBFGSolver;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.UnivariateSolver;
import org.junit.Test;

/**
 * Test class for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double expected = 1.0;

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1);
            assertEquals(expected, result, 1e-8);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        // Test with a function that has no real roots
        function = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        x0 = 1.0;
        x1 = 2.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1));

        // Test with a function that throws an exception during evaluation
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new NumberIsTooSmallException("value is too small");
            }
        };
        x0 = 1.0;
        x1 = 2.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1));

        // Test with a function that requires a specific solver
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x > 0.0) {
                    return Math.log(x);
                } else {
                    throw new PsolverRequiredException("Function requires Powell solver");
                }
            }
        };
        x0 = 1.0;
        x1 = 2.0;
        UnivariateSolver solver = new PowellSolver();
        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, solver);
            assertEquals(Math.E, result, 1e-8);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-8;
        double expected = 1.0;

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
            assertEquals(expected, result, absoluteAccuracy);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        // Test with a function that has no real roots
        function = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        x0 = 1.0;
        x1 = 2.0;
        absoluteAccuracy = 1e-6;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy));

        // Test with a function that throws an exception during evaluation
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new NumberIsTooSmallException("value is too small");
            }
        };
        x0 = 1.0;
        x1 = 2.0;
        absoluteAccuracy = 1e-8;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy));

        // Test with a function that requires a specific solver
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x > 0.0) {
                    return Math.log(x);
                } else {
                    throw new PsolverRequiredException("Function requires Powell solver");
                }
            }
        };
        x0 = 1.0;
        x1 = 2.0;
        absoluteAccuracy = 1e-6;
        UnivariateSolver solver = new PowellSolver();
        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy, solver);
            assertEquals(Math.E, result, absoluteAccuracy);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] expected = {0.0, 1.0};

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            assertEquals(expected[0], result[0], 1e-8);
            assertEquals(expected[1], result[1], 1e-8);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        // Test with a function that has no real roots
        function = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound));

        // Test with a function that throws an exception during evaluation
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new NumberIsTooSmallException("value is too small");
            }
        };
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound));

        // Test with a function that requires a specific solver
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x > 0.0) {
                    return Math.log(x);
                } else {
                    throw new PsolverRequiredException("Function requires Powell solver");
                }
            }
        };
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;
        UnivariateSolver solver = new PowellSolver();
        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, Integer.MAX_VALUE);
            assertEquals(expected[0], result[0], 1e-8);
            assertEquals(expected[1], result[1], 1e-8);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 10;
        double[] expected = {0.0, 1.0};

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations);
            assertEquals(expected[0], result[0], 1e-8);
            assertEquals(expected[1], result[1], 1e-8);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        // Test with a function that has no real roots
        function = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;
        maxIterations = 5;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations));

        // Test with a function that throws an exception during evaluation
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new NumberIsTooSmallException("value is too small");
            }
        };
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;
        maxIterations = 10;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations));

        // Test with a function that requires a specific solver
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x > 0.0) {
                    return Math.log(x);
                } else {
                    throw new PsolverRequiredException("Function requires Powell solver");
                }
            }
        };
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;
        maxIterations = 10;
        UnivariateSolver solver = new PowellSolver();
        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations);
            assertEquals(expected[0], result[0], 1e-8);
            assertEquals(expected[1], result[1], 1e-8);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}.
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;

        assertEquals(expected, UnivariateRealSolverUtils.midpoint(a, b), 1e-8);
    }
}