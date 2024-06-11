package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple polynomial function for testing. */
    private UnivariateFunction function;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1.0, 1.0, 2.0, 3.0});
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testConstructor() {
        final double absAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, Method.REGULA_FALSI);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracy() {
        final double relAccuracy = 1e-4;
        final double absAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, Method.REGULA_FALSI);
        assertEquals(relAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testConstructorWithFunctionValueAccuracy() {
        final double relAccuracy = 1e-4;
        final double absAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
        assertEquals(relAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-12);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() throws MaxEvalException {
        final double absAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, Method.REGULA_FALSI);
        final double root = -2.0;
        final double result = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertEquals(root, result, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() throws MaxEvalException {
        final double absAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, Method.REGULA_FALSI);
        final double root = -2.0;
        final double result = solver.solve(10, function, -3.0, 3.0, 0.0, AllowedSolution.BELOW_SIDE);
        assertEquals(root, result, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolutionLeftSide() throws MaxEvalException {
        final double absAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, Method.REGULA_FALSI);
        final double root = 2.0;
        final double result = solver.solve(10, function, -3.0, 3.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(root, result, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolutionRightSide() throws MaxEvalException {
        final double absAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, Method.REGULA_FALSI);
        final double root = -2.0;
        final double result = solver.solve(10, function, -3.0, 3.0, 0.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(root, result, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgument() {
        final BaseSecantSolver solver = new BaseSecantSolver(1.0, Method.REGULA_FALSI);
        solver.solve(10, FunctionUtils.CONSTANT_FUNCTION, -1.0, 1.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentAllowedSolution() {
        final BaseSecantSolver solver = new BaseSecantSolver(1.0, Method.REGULA_FALSI);
        solver.solve(10, FunctionUtils.CONSTANT_FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#getAllowed()}.
     */
    @Test
    public void testGetAllowed() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowed());
    }

    /**
     * Test method for {@link BaseSecantSolver#setAllowed(AllowedSolution)}.
     */
    @Test
    public void testSetAllowed() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.setAllowed(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowed());
    }

    /**
     * Test method for {@link BaseSecantSolver#getMethod()}.
     */
    @Test
    public void testGetMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#setMethod(Method)}.
     */
    @Test
    public void testSetMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.setMethod(Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#getMin()}.
     */
    @Test
    public void testGetMin() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(-Double.POSITIVE_INFINITY, solver.getMin(), 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#getMax()}.
     */
    @Test
    public void testGetMax() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(Double.POSITIVE_INFINITY, solver.getMax(), 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#getAbsoluteAccuracy()}.
     */
    @Test
    public void testGetAbsoluteAccuracy() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#getRelativeAccuracy()}.
     */
    @Test
    public void testGetRelativeAccuracy() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#getFunctionValueAccuracy()}.
     */
    @Test
    public void testGetFunctionValueAccuracy() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, 1e-9);
        assertEquals(1e-9, solver.getFunctionValueAccuracy(), 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#getMethodName()}.
     */
    @Test
    public void testGetMethodName() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals("Regula Falsi", solver.getMethodName());
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketingIllegalArgument() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.verifyBracketing(0.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test
    public void testVerifyBracketing() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.verifyBracketing(-1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#doSolve()}.
     */
    @Test(expected = IllegalStateException.class)
    public void testDoSolveIllegalStateException() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.doSolve();
    }
}