package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test suite for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)} with default solver.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testSolveWithDefaultSolver() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0);
        double x0 = 1.0;
        double x1 = 0.0;
        double expectedResult = -0.5;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        assertEquals(expectedResult, result, Precision.EPSILON);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)} with default solver.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testSolveWithDefaultSolverAndAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(3.0, -2.0, 1.0, 2.0);
        double x0 = 2.0;
        double x1 = 0.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = -1.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);

        assertEquals(expectedResult, result, Precision.EPSILON);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)} with maximum iterations.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketWithMaxIterations() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)} with max iterations.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketWithMaxIterationsAndCustom() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 10;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)} with valid arguments.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testBracketWithValidArguments() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);

        assertEquals(lowerBound, result[0], Precision.EPSILON);
        assertEquals(upperBound, result[1], Precision.EPSILON);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)} with null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetupWithNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)} with null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 0.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)} with null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunctionAndAccuracy() {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 0.0;
        double absoluteAccuracy = 0.001;

        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)} with null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() {
        UnivariateRealFunction f = null;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)} with null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunctionAndIterations() {
        UnivariateRealFunction f = null;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 10;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#midpoint(double, double)} with two negative numbers.
     */
    @Test
    public void testMidpointWithTwoNegativeNumbers() {
        double a = -1.0;
        double b = -2.0;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(-1.5, result, Precision.EPSILON);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#midpoint(double, double)} with two positive numbers.
     */
    @Test
    public void testMidpointWithTwoPositiveNumbers() {
        double a = 1.0;
        double b = 2.0;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(1.5, result, Precision.EPSILON);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#midpoint(double, double)} with one negative number and one positive number.
     */
    @Test
    public void testMidpointWithOneNegativeAndOnePositiveNumber() {
        double a = -1.0;
        double b = 2.0;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(-0.5, result, Precision.EPSILON);
    }
}