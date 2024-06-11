package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for BracketingNthOrderBrentSolver
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor BracketingNthOrderBrentSolver()
     */
    @Test
    public void testConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor BracketingNthOrderBrentSolver(double, int)
     */
    @Test
    public void testConstructorRelativeAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 4);
        assertEquals(1e-7, solver.getRelativeAccuracy(), 0.0);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Test constructor BracketingNthOrderBrentSolver(double, double, int)
     */
    @Test
    public void testConstructorRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 1e-8, 4);
        assertEquals(1e-7, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Test constructor BracketingNthOrderBrentSolver(double, double, double, int)
     */
    @Test
    public void testConstructorRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder()
            throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 1e-8, 1e-9, 4);
        assertEquals(1e-7, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-9, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Test method getMaximalOrder()
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method solve(int, UnivariateFunction, double, double)
     */
    @Test
    public void testSolveMinMax() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 0);
        double result = new BracketingNthOrderBrentSolver().solve(100, f, -2, 2);
        assertEquals(0.0, result, 1e-6);
    }

    /**
     * Test method solve(int, UnivariateFunction, double, double, double)
     */
    @Test
    public void testSolveMinStartValue() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 0);
        double result = new BracketingNthOrderBrentSolver().solve(100, f, -2, 2, 1);
        assertEquals(0.0, result, 1e-6);
    }

    /**
     * Test method solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution)
     */
    @Test
    public void testSolveMinStartValueAllowedSolution() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 0);
        double result = new BracketingNthOrderBrentSolver().solve(100, f, -2, 2, 1, AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, result, 1e-6);
    }

    /**
     * Test method solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) with non bracketing interval
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNonBracketingInterval() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 0);
        new BracketingNthOrderBrentSolver().solve(100, f, 2, 1);
    }

    /**
     * Test method solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) with function value accuracy less than the absolute
     * accuracy
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionValueAccuracyLessThanAbsoluteAccuracy()
            throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 0);
        new BracketingNthOrderBrentSolver().solve(100, f, -2, 2, 1e-7, 1e-8);
    }

    /**
     * Test method solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) with negative relative accuracy
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveNegativeRelativeAccuracy() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 0);
        new BracketingNthOrderBrentSolver().solve(100, f, -2, 2, 1, -1e-7);
    }

    /**
     * Test method solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) with maximal order less than 2
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveMaximalOrderLessThanTwo() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 0);
        new BracketingNthOrderBrentSolver().solve(100, f, -2, 2, 1, 1);
    }

}