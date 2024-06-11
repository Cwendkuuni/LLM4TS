package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;

import org.junit.Test;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default accuracy and maximal order.
     */
    @Test
    public void testDefaultConstructor() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified accuracy and maximal order.
     */
    @Test
    public void testConstructor() {
        final double absoluteAccuracy = 1e-8;
        final int maximalOrder = 7;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test constructor with relative and absolute accuracy and maximal order.
     */
    @Test
    public void testConstructor2() {
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-9;
        final int maximalOrder = 7;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test constructor with relative, absolute accuracy and function value accuracy and maximal order.
     */
    @Test
    public void testConstructor3() {
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-9;
        final double functionValueAccuracy = 1e-10;
        final int maximalOrder = 7;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}
     * with default accuracy and maximal order.
     */
    @Test
    public void testSolveDefault() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 2 * x * x + 3);
        final double min = -2;
        final double max = 2;
        final double expected = -1.0;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double result = solver.solve(f, min, max);
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}
     * with specified accuracy and maximal order.
     */
    @Test
    public void testSolve() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 2 * x * x + 3);
        final double min = -2;
        final double max = 2;
        final double expected = -1.0;
        final int maxEval = 100;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        final double result = solver.solve(maxEval, f, min, max);
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with default accuracy and maximal order.
     */
    @Test
    public void testSolveDefault2() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 2 * x * x + 3);
        final double min = -2;
        final double max = 2;
        final double startValue = 0;
        final double expected = -1.0;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double result = solver.solve(f, min, max, startValue);
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with specified accuracy and maximal order.
     */
    @Test
    public void testSolve2() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 2 * x * x + 3);
        final double min = -2;
        final double max = 2;
        final double startValue = 0;
        final double expected = -1.0;
        final int maxEval = 100;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        final double result = solver.solve(maxEval, f, min, max, startValue);
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with specified function value accuracy.
     */
    @Test
    public void testSolve3() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 2 * x * x + 3);
        final double min = -2;
        final double max = 2;
        final double startValue = 0;
        final double absoluteAccuracy = 1e-12;
        final double expected = -1.0;
        final int maxEval = 100;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-9, 1e-12, 7);
        final double result = solver.solve(maxEval, f, min, max, startValue);
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x - 1);
        final double min = 0;
        final double max = 0;
        final double startValue = 1;
        final int maxEval = 100;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        solver.solve(maxEval, f, min, max, startValue);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with no bracketing and function value accuracy.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing2() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x - 1);
        final double min = 0;
        final double max = 0;
        final double startValue = 0;
        final double absoluteAccuracy = 1e-12;
        final int maxEval = 100;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-9, 1e-12, 7);
        solver.solve(maxEval, f, min, max, startValue);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with maxEval exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        final UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 2 * x * x + 3);
        final double min = -2;
        final double max = 2;
        final double startValue = 0;
        final int maxEval = 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        solver.solve(maxEval, f, min, max, startValue);
    }

}