package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * JUnit tests for the BisectionSolver class.
 */
public class BisectionSolverTest {

    /**
     * Test constructor with valid UnivariateRealFunction.
     */
    @Test
    public void testConstructorWithValidFunction() {
        UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        double expected = -0.5;
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(expected, Precision.round(result, 5));
    }

    /**
     * Test solve method with valid interval and initial guess.
     */
    @Test
    public void testSolveWithValidIntervalAndInitialGuess() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        double expected = -0.5;
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(expected, Precision.round(result, 5));
    }

    /**
     * Test constructor with null UnivariateRealFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFunction() {
        new BisectionSolver(null);
    }

    /**
     * Test constructor with UnivariateRealFunction that throws exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testConstructorWithFunctionThatThrowsException() {
        UnivariateRealFunction f = () -> { throw new UnsupportedOperationException(); };
        new BisectionSolver(f);
    }

    /**
     * Test solve method with invalid interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Test solve method with initial guess outside of interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInitialGuessOutsideOfInterval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.5, 1.0);
    }

    /**
     * Test solve method with max iterations exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(0);
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solve method with very small absolute accuracy.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithVerySmallAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(0.0);
        solver.solve(f, -1.0, 1.0);
    }
}