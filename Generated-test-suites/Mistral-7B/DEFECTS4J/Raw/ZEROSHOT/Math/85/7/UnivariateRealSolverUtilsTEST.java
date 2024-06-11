package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Tests for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test solver with a simple quadratic function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolverQuadraticFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 1;
        double x0 = -1.0;
        double x1 = 1.0;
        double expected = 0.0;

        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Test solver with a function that has no roots.
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testSolverNoRootFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(Math.E, -f);
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    /**
     * Test solver with a function that has infinite roots.
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testSolverInfiniteRootFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.log(Math.abs(f) + 1.0);
        double x0 = -2.0;
        double x1 = 2.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    /**
     * Test bracketing with a simple quadratic function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketingQuadraticFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 1;
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        double[] expected = {-0.70710678118655, 0.70710678118655};

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(expected, result, 1e-12);
    }

    /**
     * Test bracketing with a function that has no roots.
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketingNoRootFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(Math.E, -f);
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    /**
     * Test bracketing with a function that has infinite roots.
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketingInfiniteRootFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.log(Math.abs(f) + 1.0);
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    /**
     * Test bracketing with a function that has one root.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketingOneRootFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1.0;
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        double[] expected = {-1.0, 1.0};

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(expected, result, 1e-12);
    }

    /**
     * Test bracketing with a function that requires a large number of iterations.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketingLargeIterationsFunction() throws Exception {
        UnivariateRealFunction function = f -> Math.sin(Math.PI * f) / f;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 1000;
        double[] expected = {0.68354557495683, 0.7122471230132};

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations);
        assertArrayEquals(expected, result, 1e-6);
    }
}