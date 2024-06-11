package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    /** The solver to test. */
    private BaseSecantSolver solver;

    /** The method to use. */
    private Method method;

    /** The polynomial function to test against. */
    private UnivariateFunction function;

    /** The minimum and maximum bounds. */
    private double min;
    private double max;

    /**
     * Constructor.
     *
     * @param method The method to use.
     * @param function The polynomial function to test against.
     * @param min The minimum bound.
     * @param max The maximum bound.
     */
    public BaseSecantSolverTest(Method method, UnivariateFunction function, double min, double max) {
        this.method = method;
        this.function = function;
        this.min = min;
        this.max = max;
    }

    /**
     * Test data.
     */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Method.REGULA_FALSI, new PolynomialFunction(x -> Math.pow(x, 2) - 4), -3.0, 2.0},
                {Method.ILLINOIS, new PolynomialFunction(x -> Math.pow(x, 2) - 4), -3.0, 2.0},
                {Method.PEGASUS, new PolynomialFunction(x -> Math.pow(x, 2) - 4), -3.0, 2.0},
                {Method.REGULA_FALSI, new PolynomialFunction(x -> Math.sin(x) / x), -10.0, 10.0},
                {Method.ILLINOIS, new PolynomialFunction(x -> Math.sin(x) / x), -10.0, 10.0},
                {Method.PEGASUS, new PolynomialFunction(x -> Math.sin(x) / x), -10.0, 10.0},
                {Method.REGULA_FALSI, new PolynomialFunction(x -> Math.exp(x) - 1), -10.0, 10.0},
                {Method.ILLINOIS, new PolynomialFunction(x -> Math.exp(x) - 1), -10.0, 10.0},
                {Method.PEGASUS, new PolynomialFunction(x -> Math.exp(x) - 1), -10.0, 10.0},
                {Method.REGULA_FALSI, new PolynomialFunction(x -> Math.log(Math.abs(x) + 1) - 1), -2.0, 2.0},
                {Method.ILLINOIS, new PolynomialFunction(x -> Math.log(Math.abs(x) + 1) - 1), -2.0, 2.0},
                {Method.PEGASUS, new PolynomialFunction(x -> Math.log(Math.abs(x) + 1) - 1), -2.0, 2.0},
                {Method.REGULA_FALSI, new PolynomialFunction(x -> Math.sqrt(x) - 2), -5.0, 5.0},
                {Method.ILLINOIS, new PolynomialFunction(x -> Math.sqrt(x) - 2), -5.0, 5.0},
                {Method.PEGASUS, new PolynomialFunction(x -> Math.sqrt(x) - 2), -5.0, 5.0},
                {Method.REGULA_FALSI, new PolynomialFunction(x -> x * Math.sin(Math.PI / x) / x), -10.0, 10.0},
                {Method.ILLINOIS, new PolynomialFunction(x -> x * Math.sin(Math.PI / x) / x), -10.0, 10.0},
                {Method.PEGASUS, new PolynomialFunction(x -> x * Math.sin(Math.PI / x) / x), -10.0, 10.0}
        });
    }

    /**
     * Basic test case for the {@link #solve(int, UnivariateRealFunction, double, double)} method.
     */
    @Test
    public void testSolve() throws MaxEvalException {
        double root = solver.solve(function, min, max);
        double expected = function.value(0.0);
        assertEquals("Failed to find root.", expected, Precision.round(root, 10));
    }

    /**
     * Test for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testConstructorWithAbsoluteAccuracyAndMethod() {
        // Nothing to test here as the constructor only initializes the superclass.
    }

    /**
     * Test for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndMethod() {
        // Nothing to test here as the constructor only initializes the superclass.
    }

    /**
     * Test for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyFunctionValueAccuracyAndMethod() {
        // Nothing to test here as the constructor only initializes the superclass.
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithOnlyFunction() throws MaxEvalException {
        solver.solve(function);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() throws MaxEvalException {
        solver.solve(function, min, max, AllowedSolution.INVALID_SOLUTION);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with startValue out of bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithStartValueOutOfBounds() throws MaxEvalException {
        solver.solve(function, min, max, min - 1.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with startValue within bounds but not bracketing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithStartValueNotBracketing() throws MaxEvalException {
        solver.solve(function, min, max, min, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with startValue within bracketing but solution is not found.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithStartValueWithinBracketingButNoSolution() throws MaxEvalException {
        solver.setMaxEval(100);
        solver.solve(function, min, max, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with inverted intervals.
     */
    @Test
    public void testSolveWithInvertedIntervals() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                double x0 = getMax();
                double x1 = getMin();
                double f0 = computeObjectiveValue(x0);
                double f1 = computeObjectiveValue(x1);

                if (f0 * f1 <= 0) {
                    // Switch the bounds and invert the flag for inverted intervals.
                    double temp = x0;
                    x0 = x1;
                    x1 = temp;
                    boolean tempFlag = isAllowedLeft();
                    setAllowedLeft(!tempFlag);
                }

                return super.doSolve();
            }
        };

        solver.solve(function, min, max);

        double expected = function.value(0.0);
        assertEquals("Failed to find root.", expected, Precision.round(solver.getRoot(), 10));
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with the function having a vertical asymptote.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSolveWithFunctionHavingVerticalAsymptote() throws MaxEvalException {
        UnivariateFunction function = new PolynomialFunction(x -> 1 / (x - 1.0));
        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(function, -10.0, 10.0);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with the function having a horizontal asymptote.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithFunctionHavingHorizontalAsymptote() throws MaxEvalException {
        UnivariateFunction function = new PolynomialFunction(x -> x / x);
        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(function, -10.0, 10.0);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with the function having a double root.
     */
    @Test
    public void testSolveWithFunctionHavingDoubleRoot() throws MaxEvalException {
        UnivariateFunction function = new PolynomialFunction(x -> (x - 2) * (x - 3));
        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(function, -5.0, 5.0);
        double expected = 2.0;
        assertEquals("Failed to find root.", expected, Precision.round(root, 10));
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with the function having a local minimum.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithFunctionHavingLocalMinimum() throws MaxEvalException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x - 1.0, 2));
        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(function, -5.0, 5.0);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with the function having a local maximum.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithFunctionHavingLocalMaximum() throws MaxEvalException {
        UnivariateFunction function = new PolynomialFunction(x -> -Math.pow(x - 1.0, 2));
        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(function, -5.0, 5.0);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with the function having a local extreme at the root.
     */
    @Test
    public void testSolveWithFunctionHavingLocalExtremeAtRoot() throws MaxEvalException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x - 2.0, 2) - 0.01);
        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(function, -5.0, 5.0);
        double expected = 2.0;
        assertEquals("Failed to find root.", expected, Precision.round(root, 10));
    }
}