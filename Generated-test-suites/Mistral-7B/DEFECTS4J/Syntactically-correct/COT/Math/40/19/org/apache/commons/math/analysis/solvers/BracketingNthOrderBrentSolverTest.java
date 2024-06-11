package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Test;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default values.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified accuracy and maximal order.
     */
    @Test
    public void testConstructorWithSpecifiedValues() {
        double absoluteAccuracy = 1e-8;
        int maximalOrder = 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test constructor with relative accuracy, absolute accuracy, function value accuracy, and maximal order.
     */
    @Test
    public void testConstructorWithAllValues() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-10;
        int maximalOrder = 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test throwing exception when maximal order is lower than 2.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1.0, 1);
    }

    /**
     * Test solving a simple polynomial function.
     */
    @Test
    public void testSolveSimplePolynomial() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 2);
        double min = -2.5;
        double max = 2.5;
        double root = solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 0.0001);
    }

    /**
     * Test solving a polynomial function with specified start value.
     */
    @Test
    public void testSolveSimplePolynomialWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 2);
        double min = -2.5;
        double max = 2.5;
        double startValue = 0.5;
        double root = solver.solve(100, f, min, max, startValue, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 0.0001);
    }

    /**
     * Test solving a non-bracketing interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNonBracketingInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 2);
        double min = 3.0;
        double max = 2.5;
        solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solving a non-bracketing interval with specified start value.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNonBracketingIntervalWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 2);
        double min = 3.0;
        double max = 2.5;
        double startValue = 1.0;
        solver.solve(100, f, min, max, startValue, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solving a quadratic function with specified allowed solution.
     */
    @Test
    public void testSolveQuadraticFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) + 2);
        double min = -2.5;
        double max = 2.5;
        double root = solver.solve(100, f, min, max, AllowedSolution.ABOVE_SIDE);
        assertTrue(root > 0);
    }

    /**
     * Test solving a quadratic function with specified start value and allowed solution.
     */
    @Test
    public void testSolveQuadraticFunctionWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) + 2);
        double min = -2.5;
        double max = 2.5;
        double startValue = 1.5;
        double root = solver.solve(100, f, min, max, startValue, AllowedSolution.ABOVE_SIDE);
        assertTrue(root > 0);
    }

    /**
     * Test solving a constant function.
     */
    @Test
    public void testSolveConstantFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> 4);
        double min = -2.5;
        double max = 2.5;
        double root = solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE);
        assertNull(root);
    }

    /**
     * Test solving a constant function with specified start value.
     */
    @Test
    public void testSolveConstantFunctionWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(x -> 4);
        double min = -2.5;
        double max = 2.5;
        double startValue = 1.0;
        double root = solver.solve(100, f, min, max, startValue, AllowedSolution.ANY_SIDE);
        assertNull(root);
    }
}