package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RuntimeConfiguration;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @org.junit.Test
    public void testBaseSecantSolverConstructor() {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @org.junit.Test
    public void testBaseSecantSolverConstructorRelative() {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-4, 1.0E-6, Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @org.junit.Test
    public void testBaseSecantSolverConstructorAbsoluteFunctionValue() {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, 1.0E-6, 1.0E-6, Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        double result = solver.solve(10, f, 1.0, 2.0, 1.5);
        assertEquals(1.3390573153321255, result, 1.0E-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @org.junit.Test
    public void testSolveWithAllowedSolution() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        double result = solver.solve(10, f, 1.0, 2.0, 1.5, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.3390573153321255, result, 1.0E-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() throws Exception {
        UnivariateRealFunction f = x -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        solver.solve(10, f, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolveNegativeMaxEval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        solver.solve(-10, f, 1.0, 2.0, 1.5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalExceeded() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        solver.solve(1, f, 1.0, 2.0, 1.5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test
    public void testSolveMaxEvalExceeded_throwException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS) {
            @Override
            protected double doSolve() throws MaxEvalExceededException {
                throw new MaxEvalExceededException();
            }
        };
        try {
            solver.solve(1, f, 1.0, 2.0, 1.5);
        } catch (NoBracketingException e) {
            // expected exception
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @org.junit.Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        try {
            solver.verifyBracketing(1.0, 1.0);
            assert False;
        } catch (NoBracketingException expected) {
            // expected exception
        }

        try {
            solver.verifyBracketing(1.0, 2.0);
            assertTrue(true);
        } catch (Exception e) {
            assert False;
        }

        try {
            solver.verifyBracketing(2.0, 1.0);
            assertTrue(true);
        } catch (Exception e) {
            assert False;
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#getAllowedSolution()}.
     */
    @org.junit.Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        BaseSecantSolver solver2 = new BaseSecantSolver(1.0E-6, Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver2.getAllowedSolution());

        BaseSecantSolver solver3 = new BaseSecantSolver(1.0E-6, Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver3.getAllowedSolution());
    }

    /**
     * Test method for {@link BaseSecantSolver#setAllowedSolution(AllowedSolution)}.
     */
    @org.junit.Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        BaseSecantSolver solver2 = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        solver2.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver2.getAllowedSolution());
    }

    /**
     * Test method for {@link BaseSecantSolver#getMethod()}.
     */
    @org.junit.Test
    public void testGetMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#setMethod(Method)}.
     */
    @org.junit.Test
    public void testSetMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        solver.setMethod(Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#getAbsoluteAccuracy()}.
     */
    @org.junit.Test
    public void testGetAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        assertEquals(1.0E-6, solver.getAbsoluteAccuracy(), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#getRelativeAccuracy()}.
     */
    @org.junit.Test
    public void testGetRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-4, 1.0E-6, Method.ILLINOIS);
        assertEquals(1.0E-4, solver.getRelativeAccuracy(), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#getFunctionValueAccuracy()}.
     */
    @org.junit.Test
    public void testGetFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        assertEquals(1.0E-6, solver.getFunctionValueAccuracy(), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#getMaxIterations()}.
     */
    @org.junit.Test
    public void testGetMaxIterations() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        assertEquals(-1, solver.getMaxIterations());
    }

    /**
     * Test method for {@link BaseSecantSolver#setMaxIterations(int)}.
     */
    @org.junit.Test
    public void testSetMaxIterations() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, Method.ILLINOIS);
        solver.setMaxIterations(10);
        assertEquals(10, solver.getMaxIterations());
    }

    /** A polynomial function. */
    static class PolynomialFunction implements UnivariateRealFunction {

        private final int degree;

        private final double coefficient0;

        /**
         * Constructor.
         *
         * @param degree polynomial degree
         * @param coefficient0 leading coefficient
         */
        public PolynomialFunction(int degree, double coefficient0) {
            this.degree = degree;
            this.coefficient0 = coefficient0;
        }

        @Override
        public double value(double x) {
            double result = 0;
            for (int i = degree; i > 0; i--) {
                result += coefficient0 * Math.pow(x, i);
            }
            return result;
        }
    }
}