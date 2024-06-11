package org.apache.commons.math.analysis.solvers; // adjust the package name if necessary

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using a simple polynomial function.
     * @throws Exception
     */
    @Test
    public void testSolveSimplePolynomial() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0});
        double expectedResult = 1.0;
        double x0 = 0.0;
        double x1 = 2.0;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using a simple polynomial function with absolute accuracy.
     * @throws Exception
     */
    @Test
    public void testSolveSimplePolynomialWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0});
        double expectedResult = 1.0;
        double x0 = 0.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-9;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * using a simple polynomial function.
     * @throws Exception
     */
    @Test
    public void testBracketSimplePolynomial() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(new double[]{0.0, 2.0}, result, 1e-8);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * using a simple polynomial function with a specified number of iterations.
     * @throws Exception
     */
    @Test
    public void testBracketSimplePolynomialWithMaxIterations() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 1;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        assertArrayEquals(new double[]{initial, initial}, result, 1e-8);
        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * using a function with roots close to the endpoints.
     * @throws Exception
     */
    @Test
    public void testBracketFunctionWithRootsCloseToEndpoints() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{3.0, -2.0, 3.0});
        double initial = 1.0;
        double lowerBound = 1.1;
        double upperBound = 2.0;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, 1000);
        assertArrayEquals(new double[]{1.5, 1.75}, result, 1e-8);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a function with an invalid interval.
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, -1.0, 1.0});
        double x0 = 2.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with a function and a very small absolute accuracy.
     * @throws Exception
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveVerySmallAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0});
        double x0 = 0.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-15;
        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with a function and a very large number of iterations.
     * @throws Exception
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0});
        double x0 = 0.0;
        double x1 = 2.0;
        int maximumIterations = Integer.MAX_VALUE;
        UnivariateRealSolverUtils.solve(function, x0, x1, maximumIterations);
    }
}