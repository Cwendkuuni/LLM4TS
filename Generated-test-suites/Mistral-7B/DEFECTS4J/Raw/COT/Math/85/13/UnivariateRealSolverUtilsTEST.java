package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.exception.ZeroSolverException;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Tests for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a simple function and no exception should be thrown.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 2.0});
        double x0 = 1.0;
        double x1 = 3.0;
        double expectedResult = 1.0;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(expectedResult, result, 1e-9);
        } catch(FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with a simple function and no exception should be thrown.
     */
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 2.0});
        double x0 = 1.0;
        double x1 = 3.0;
        double expectedResult = 1.0;
        double absoluteAccuracy = 1e-10;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(expectedResult, result, absoluteAccuracy);
        } catch(FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a function that has no roots in the interval, and
     * ConvergenceException should be thrown.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveNoRoots() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        double x0 = 1.0;
        double x1 = 2.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with a simple function and no exception should be thrown.
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 2.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;

        double[] expectedResult = new double[]{1.5, 2.5};

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertArrayEquals(expectedResult, new double[]{result[0], result[1]}, 0.0);
        } catch(FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with a simple function and no exception should be thrown.
     */
    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 2.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        int maxIterations = 10;

        double[] expectedResult = new double[]{1.5, 2.5};

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
            assertArrayEquals(expectedResult, new double[]{result[0], result[1]}, 0.0);
        } catch(FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with a function that has no roots in the interval, and
     * ConvergenceException should be thrown.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketNoRoots() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 10;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     * with two values that are not equal,
     * and the midpoint should be calculated correctly.
     */
    @Test
    public void testMidpoint() {
        double a = 2.0;
        double b = 4.0;
        double expectedMidpoint = 3.0;

        double midpointResult = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expectedMidpoint, midpointResult, 1e-9);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}
     * with a null function,
     * and IllegalArgumentException should be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double, int)}
     * with a simple function, initial point not in the interval,
     * and ConvergenceException should be thrown.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveInitialPointOutOfInterval() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 2.0});
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maxIterations = 10;

        UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, maxIterations);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double, int)}
     * with a simple function, lower bound greater than upper bound,
     * and ConvergenceException should be thrown.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveLowerBoundGreaterThanUpperBound() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 2.0});
        double initial = 1.0;
        double lowerBound = 3.0;
        double upperBound = 2.0;
        int maxIterations = 10;

        UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, maxIterations);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with a simple function, lower bound greater than upper bound,
     * and ConvergenceException should be thrown.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketLowerBoundGreaterThanUpperBound() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 2.0});
        double initial = 1.0;
        double lowerBound = 3.0;
        double upperBound = 2.0;
        int maxIterations = 10;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double, int)}
     * with a function that can't be solved with the specified maximum iterations,
     * and ConvergenceException should be thrown.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0, 1.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 0;

        try {
            UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, maxIterations);
        } catch (NoBracketingException | PrecisionExceededException e) {
            // these exceptions should not be thrown in this test case
            throw new RuntimeException("Unexpected exception.", e);
        }

        UnivariateRealSolver solver = new BrentSolver();
        double[] brackets = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations * 2);

        UnivariateRealFunction bracketedFunction = new PolynomialFunction(new double[]{0.0, 1.0, -2.0, 1.0, 1.0 + Math.pow(10, maxIterations)});
        double root = solver.solve(bracketedFunction, brackets[0], brackets[1]);

        UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, maxIterations * 2 + 1);
    }
}