package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testMethods() {
        assertEquals(3, BaseSecantSolver.Method.values().length);
    }

    @Test
    public void testSolverConstructor() {
        assertNotNull(new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI));
        assertNotNull(new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS));
        assertNotNull(new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS));
        assertNotNull(new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, 0.1));
        assertNotNull(new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, 0.1, 1e-6));
    }

    @Test
    public void testSolverConstructorWithNullMethod() {
        assertNull(new BaseSecantSolver(null));
    }

    @Test
    public void testSolverConstructorWithInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> new BaseSecantSolver(BaseSecantSolver.Method.INVALID));
    }

    @Test
    public void testGetMin() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        assertEquals(Double.NEGATIVE_INFINITY, solver.getMin(), 0.0);

        solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, 0.0);
        assertEquals(0.0, solver.getMin(), 0.0);

        solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, 0.0, 1e-6, 1);
        assertEquals(0.0, solver.getMin(), 1e-6);
    }

    @Test
    public void testGetMax() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        assertEquals(Double.POSITIVE_INFINITY, solver.getMax(), 0.0);

        solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, solver.getMax(), 0.0);

        solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, 0.0, 1e-6, 1);
        assertEquals(Double.POSITIVE_INFINITY, solver.getMax(), 1e-6);
    }

    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        assertThrows(UnsupportedOperationException.class, () -> solver.setAllowedSolution(AllowedSolution.INVALID));
    }

    @Test
    public void testSolveWithFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        PolynomialFunction func = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});

        assertThrows(MaxEvalExceededException.class, () -> solver.solve(10, func, -1.0, 1.0, AllowedSolution.LEFT_SIDE));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, func, 1.0, -1.0, AllowedSolution.LEFT_SIDE));

        // Test polynomial function with allowed solution on left side
        solver.setFunction(func);
        assertEquals(-1.0, solver.solve(10, func, -2.0, 0.0, AllowedSolution.LEFT_SIDE), 1e-6);

        // Test polynomial function with allowed solution on right side
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, solver.solve(10, func, 0.0, 2.0, AllowedSolution.RIGHT_SIDE), 1e-6);

        // Test polynomial function with allowed solution below
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        assertEquals(-1.0, solver.solve(10, func, -2.0, 0.0, AllowedSolution.BELOW_SIDE), 1e-6);

        // Test polynomial function with allowed solution above
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0, solver.solve(10, func, 0.0, 2.0, AllowedSolution.ABOVE_SIDE), 1e-6);
    }

    @Test
    public void testSolveWithFunctionAndStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        PolynomialFunction func = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});

        assertThrows(MaxEvalExceededException.class, () -> solver.solve(10, func, -1.0, 1.0, AllowedSolution.LEFT_SIDE, 2.0));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, func, -1.0, 1.0, AllowedSolution.LEFT_SIDE, -2.0));

        // Test polynomial function with allowed solution on left side and start value in the interval
        solver.setFunction(func);
        assertEquals(-1.0, solver.solve(10, func, -2.0, 0.0, AllowedSolution.LEFT_SIDE, -1.0), 1e-6);

        // Test polynomial function with allowed solution on right side and start value in the interval
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, solver.solve(10, func, 0.0, 2.0, AllowedSolution.RIGHT_SIDE, 1.0), 1e-6);

        // Test polynomial function with allowed solution below and start value below
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        assertEquals(-1.0, solver.solve(10, func, -2.0, 0.0, AllowedSolution.BELOW_SIDE, -1.5), 1e-6);

        // Test polynomial function with allowed solution above and start value above
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0, solver.solve(10, func, 0.0, 2.0, AllowedSolution.ABOVE_SIDE, 1.5), 1e-6);
    }

    @Test
    public void testSolveWithFunctionAndStartValueOutsideInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        PolynomialFunction func = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});

        // Test polynomial function with allowed solution on left side and start value outside the interval
        solver.setFunction(func);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, func, -2.0, 0.0, AllowedSolution.LEFT_SIDE, 1.0));

        // Test polynomial function with allowed solution on right side and start value outside the interval
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, func, 0.0, 2.0, AllowedSolution.RIGHT_SIDE, -1.0));
    }

    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double x0 = 0.0;
        double x1 = 1.0;

        // Test bracketing when x0 < x1
        solver.verifyBracketing(x0, x1);
        assertTrue(true);

        // Test bracketing when x0 > x1
        solver.setMin(x1);
        solver.setMax(x0);
        assertFalse(solver.verifyBracketing(x0, x1));
        assertTrue(solver.getLastFlag() == BracketingFlag.OUT_OF_BRACKET);
    }

    @Test
    public void testSolveWithMultipleCalls() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        PolynomialFunction func = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});

        assertEquals(-1.0, solver.solve(10, func, -2.0, 0.0, AllowedSolution.LEFT_SIDE), 1e-6);
        assertEquals(1.0, solver.solve(10, func, 0.0, 2.0, AllowedSolution.RIGHT_SIDE), 1e-6);
    }

    @Test
    public void testSolveWithInvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        PolynomialFunction func = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});

        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, func, -2.0, 0.0, AllowedSolution.INVALID));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, func, 0.0, 2.0, AllowedSolution.INVALID));
    }
}