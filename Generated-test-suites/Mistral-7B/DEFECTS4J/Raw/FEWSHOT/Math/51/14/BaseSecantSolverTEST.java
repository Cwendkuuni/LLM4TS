package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFunction() {
        new BaseSecantSolver(0.0, Method.ILLINOIS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeAbsoluteAccuracy() {
        new BaseSecantSolver(-1.0, Method.ILLINOIS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeRelativeAccuracy() {
        new BaseSecantSolver(-1.0, 0.0, Method.ILLINOIS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeFunctionValueAccuracy() {
        new BaseSecantSolver(0.0, 0.0, -1.0, Method.ILLINOIS);
    }

    @Test
    public void testConstructorDefault() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.0, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.0, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testConstructorAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.0, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.0, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testConstructorRelativeAndAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-3, 1.0e-6, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(1.0e-3, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1.0e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.0, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testConstructorRelativeAccuracyFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-3, 1.0e-6, 1.0e-6, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(1.0e-3, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1.0e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1.0e-6, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testSolveSimpleFunction() {
        double[] expectedSolutions = {-1.0, 1.0};
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.ILLINOIS);
        UnivariateRealFunction function = f -> Math.pow(Math.E, x) - 2.0;

        for (int i = 0; i < expectedSolutions.length; i++) {
            double result = solver.solve(100, function, -5.0, 5.0, expectedSolutions[i]);
            assertEquals(expectedSolutions[i], result, 1e-6);
        }
    }

    @Test
    public void testIllegalArgumentExceptionWithWrongNumberOfArguments() {
        double[] expectedSolutions = {-1.0, 1.0};
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.ILLINOIS);
        UnivariateRealFunction function = (x) -> Math.pow(Math.E, x) - 2.0;

        assertArrayEquals(new double[]{}, solver.solve(100, function, -5.0));
        assertArrayEquals(new double[]{}, solver.solve(100, function, -5.0, 5.0));
        assertArrayEquals(new double[]{}, solver.solve(100, function, -5.0, 5.0, 0.0));
        assertArrayEquals(new double[]{}, solver.solve(100, function, -5.0, 5.0, 0.0, null));
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        double[] expectedSolutions = {-1.0, 1.0};
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.ILLINOIS);
        UnivariateRealFunction function = (x) -> (x - 1.0) * (x - 2.0);

        assertArrayEquals(new double[]{}, solver.solve(100, function, -5.0, 2.0));
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        double[] expectedSolutions = {-1.0, 1.0};
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.ILLINOIS);
        UnivariateRealFunction function = (x) -> Math.pow(Math.E, x) - 2.0;

        try {
            solver.solve(0, function, -5.0, 5.0, 0.0);
        } catch (MaxEvalException e) {
            // Success
            assertTrue(true);
        }
    }

    @Test
    public void testAllowedSolution() {
        double[] expectedSolutions = {-1.0, 1.0};
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.ILLINOIS);
        UnivariateRealFunction function = (x) -> Math.pow(Math.E, x) - 2.0;

        for (int i = 0; i < expectedSolutions.length; i++) {
            double result = solver.solve(100, function, -5.0, 5.0, expectedSolutions[i]);
            assertEquals(expectedSolutions[i], result, 1e-6);
        }

        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        result = solver.solve(100, function, -5.0, 5.0, expectedSolutions[0]);
        assertEquals(expectedSolutions[0], result, 1e-6);

        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        result = solver.solve(100, function, -5.0, 5.0, expectedSolutions[1]);
        assertEquals(expectedSolutions[1], result, 1e-6);
    }

    @Test
    public void testAllowedSolutionInvertedInterval() {
        double[] expectedSolutions = {-1.0, 1.0};
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.ILLINOIS);
        UnivariateRealFunction function = (x) -> Math.pow(Math.E, x) - 2.0;

        for (int i = 0; i < expectedSolutions.length; i++) {
            double result = solver.solve(100, function, -5.0, 5.0, expectedSolutions[i]);
            assertEquals(expectedSolutions[i], result, 1e-6);
        }

        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        try {
            result = solver.solve(100, function, 5.0, 5.0, expectedSolutions[0]);
        } catch (IllegalArgumentException e) {
            // Success
            assertTrue(true);
        }

        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        try {
            result = solver.solve(100, function, -5.0, 5.0, expectedSolutions[1]);
        } catch (IllegalArgumentException e) {
            // Success
            assertTrue(true);
        }
    }
}