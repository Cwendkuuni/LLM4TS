package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with default solver and absolute accuracy.
     */
    @Test
    public void testSolveDefaultSolverAndAbsoluteAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);
        double x0 = 1.0, x1 = 2.0;

        // Check that the solver finds the root with default settings
        double root = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(Precision.round(1.5, 4), root, 1e-8);

        // Check that the solver throws an exception if the endpoints do not specify a valid interval
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x1, x0);
        });

        // Check that the solver throws an exception if the function is null
        assertThrows(MathRuntimeException.class, () -> {
            UnivariateRealSolverUtils.solve(null, x0, x1);
        });
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with default solver and specified absolute accuracy.
     */
    @Test
    public void testSolveDefaultSolverAndSpecifiedAbsoluteAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);
        double x0 = 1.0, x1 = 2.0, absoluteAccuracy = 1e-6;

        // Check that the solver finds the root with specified accuracy
        double root = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(Precision.round(1.5, 6), root, 1e-8);

        // Check that the solver throws an exception if the function is null
        assertThrows(MathRuntimeException.class, () -> {
            UnivariateRealSolverUtils.solve(null, x0, x1, absoluteAccuracy);
        });

        // Check that the solver throws an exception if the absoluteAccuracy is not valid for the default solver
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x0, x1, 1.0);
        });
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);
        double initial = 1.5, lowerBound = 0.0, upperBound = 3.0;
        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

        // Check that the bracketing function returns a valid interval
        double a = bracketingPoints[0];
        double b = bracketingPoints[1];
        assertEquals(Precision.round(1.5, 4), a, 1e-8);
        assertEquals(Precision.round(1.875, 4), b, 1e-8);
        assertEquals(Precision.round(1.5, 4), UnivariateRealSolverUtils.midpoint(a, b), 1e-8);

        // Check that the bracketing function throws an exceptional case for the given input
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, initial, lowerBound, 0);
        });

        // Check that the bracketing function throws an exceptional case for the given input
        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.bracket(null, initial, lowerBound, upperBound, 10);
        });

        // Check that the bracketing function throws an IllegalArgumentException for the given input
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, -1);
        });

        // Test the edge case of maximum iterations
        UnivariateRealFunction functionMaxIter = new PolynomialFunction(x -> x * x - 2);
        double initialMaxIter = Double.POSITIVE_INFINITY, lowerBoundMaxIter = Double.NEGATIVE_INFINITY,
                upperBoundMaxIter = Double.NEGATIVE_INFINITY;
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(functionMaxIter, initialMaxIter, lowerBoundMaxIter, upperBoundMaxIter, 0);
        });
    }
}