package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default accuracy and maximal order.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified accuracy and maximal order.
     */
    @Test
    public void testConstructorWithSpecifiedAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(3, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified relative accuracy, absolute accuracy, and maximal order.
     */
    @Test
    public void testConstructorWithSpecifiedRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 4);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 1e-6);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified relative accuracy, absolute accuracy, function value accuracy, and maximal order.
     */
    @Test
    public void testConstructorWithSpecifiedRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 5);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 1e-6);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-7, solver.getFunctionValueAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test getMaximalOrder method.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test solve method with a polynomial function, default minimal accuracy and minimal maximal order.
     */
    @Test
    public void testSolveWithPolynomialFunctionDefaultParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        double result = solver.solve(100, f, -10, 10);
        assertEquals(-2.0, result, 1e-6);
    }

    /**
     * Test solve method with a polynomial function, specified minimal accuracy and minimal maximal order.
     */
    @Test
    public void testSolveWithPolynomialFunctionSpecifiedParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 4);
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        double result = solver.solve(100, f, -10, 10);
        assertEquals(-2.0, result, 1e-9);
    }

    /**
     * Test solve method with a polynomial function, specified minimal accuracy, minimal maximal order, and start value.
     */
    @Test
    public void testSolveWithPolynomialFunctionSpecifiedParametersStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 4);
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        double result = solver.solve(100, f, -10, 10, 0);
        assertEquals(-2.0, result, 1e-9);
    }

    /**
     * Test solve method with a polynomial function, minimal accuracy, maximal order, and maximum number of evaluations,
     * which should lead to NoBracketingException.
     */
    @Test
    public void testSolveWithPolynomialFunctionMinimalParametersMaxEval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        assertThrows(NoBracketingException.class, () -> solver.solve(1, f, -10, 10));
    }

    /**
     * Test solve method with a polynomial function, minimal accuracy, maximal order, and maximum number of evaluations,
     * which should lead to MaxCountExceededException.
     */
    @Test
    public void testSolveWithPolynomialFunctionMinimalParametersMaxEvalMaxCountExceeded() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        assertThrows(MaxCountExceededException.class, () -> solver.solve(Integer.MAX_VALUE, f, -10, 10));
    }

    /**
     * Test solve method with a polynomial function, minimal accuracy, maximal order, and start value, which should lead to NoBracketingException.
     */
    @Test
    public void testSolveWithPolynomialFunctionMinimalParametersStartValueMaxEvalMaxCountExceeded() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, -10, 10, 0));
    }

    /**
     * Test solve method with a polynomial function, specified minimal accuracy, minimal maximal order, and start value,
     * which should lead to NoBracketingException.
     */
    @Test
    public void testSolveWithPolynomialFunctionSpecifiedParametersStartValueMaxEvalMaxCountExceeded() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 4);
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, -10, 10, 0));
    }

    /**
     * Test solve method with a polynomial function, minimal accuracy, maximal order, and start value, which should lead to NoBracketingException.
     */
    @Test
    public void testSolveWithPolynomialFunctionMinimalParametersStartValueMaxEval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, -10, 10, 1));
    }

    /**
     * Test solve method with a polynomial function, minimal accuracy, maximal order, and start value, which should lead to MaxCountExceededException.
     */
    @Test
    public void testSolveWithPolynomialFunctionMinimalParametersStartValueMaxEvalMaxCountExceeded() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        assertThrows(MaxCountExceededException.class, () -> solver.solve(Integer.MAX_VALUE, f, -10, 10, 1));
    }

    /**
     * Test solve method with a polynomial function, minimal accuracy, minimal maximal order, and start value,
     * which should lead to NumberIsTooSmallException.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithPolynomialFunctionMinimalParametersStartValueNumberIsTooSmallException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        solver.solve(100, f, -10, 10, 0.5);
    }

    /**
     * Test solve method with a polynomial function, specified minimal accuracy, minimal maximal order, and start value,
     * which should lead to NumberIsTooSmallException.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithPolynomialFunctionSpecifiedParametersStartValueNumberIsTooSmallException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 4);
        UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4, -11, 6, -2}, new int[] {4, 2, 0, 2, 1});
        solver.solve(100, f, -10, 10, 0.5);
    }

}