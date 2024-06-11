package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MaxEvalExceededException;
import org.apache.commons.commons.math.exception.RootFinderException;
import org.apache.commons.commons.math.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

public class BaseSecantSolverTest {

    /**
     * Test the constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                     solver.getAbsoluteAccuracy(), 0);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                     solver.getFunctionValueAccuracy(), 0);
        assertEquals(BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE,
                     solver.getAllowedSolution());
    }

    /**
     * Test the constructor with specified arguments.
     */
    @Test
    public void testConstructor() {
        double absoluteAccuracy = 1e-8;
        double relativeAccuracy = 1e-7;
        double functionValueAccuracy = 1e-9;
        Method method = Method.PEGASUS;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                      absoluteAccuracy,
                                                      functionValueAccuracy,
                                                      method);
        assertEquals(Method.PEGASUS, solver.getMethod());
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0);
        assertEquals(BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE,
                     solver.getAllowedSolution());
    }

    /**
     * Test the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with invalid arguments.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS);
        UnivariateRealFunction f = f -> f * f;
        solver.solve(-1, f, 0, 0.5, 0);
    }

    /**
     * Test the {@link #solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with valid arguments.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        UnivariateRealFunction f = x -> Math.pow(Math.E, Math.sin(x)) - 1;
        double min = -1.5;
        double max = 1.5;
        double x = 0;

        try {
            double root = solver.solve(10, f, min, max, x,
                                       BracketedUnivariateRealSolver.AllowedSolution.LEFT_SIDE);
            assertEquals(-1, root, 0);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(10, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.RIGHT_SIDE);
            assertEquals(1, root, 0);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(10, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.BELOW_SIDE);
            assertTrue(Math.abs(root - Math.PI) < 0.01);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(10, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.ABOVE_SIDE);
            assertTrue(Math.abs(root + Math.PI) < 0.01);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(10, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.ANY_SIDE);
            assertTrue(Math.abs(root - Math.E) < 0.01);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(1, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.LEFT_SIDE);
            assertEquals(-1, root, 0);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(1, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.RIGHT_SIDE);
            assertEquals(1, root, 0);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(1, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.BELOW_SIDE);
            assertTrue(Math.abs(root - Math.PI) < 0.01);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(1, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.ABOVE_SIDE);
            assertTrue(Math.abs(root + Math.PI) < 0.01);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(1, f, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.ANY_SIDE);
            assertTrue(Math.abs(root - Math.E) < 0.01);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }

        try {
            double root = solver.solve(1, null, min, max, x,
                                       BracketedUnivariateRealSolution.AllowedSolution.ANY_SIDE);
            assertNull(root);
        } catch (NullPointerException e) {
            // Expected.
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }
    }

    /**
     * Test the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with the default allowed solution.
     */
    @Test
    public void testSolveDefault() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        UnivariateRealFunction f = x -> Math.sin(x) - 0.5;
        double min = -2;
        double max = 2;
        double x = 0;

        try {
            double root = solver.solve(10, f, min, max, x);
            assertTrue(Math.abs(root - Math.PI / 2) < 0.01);
        } catch (MaxEvalExceededException e) {
            // Ignore.
        }
    }

    /**
     * Test the {@link #getAllowedSolution()} method.
     */
    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);

        assertEquals(BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE,
                     solver.getAllowedSolution());
    }

    /**
     * Test the {@link #setAllowedSolution(AllowedSolution)} method.
     */
    @Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        AllowedSolution allowed = BracketedUnivariateRealSolver.AllowedSolution.LEFT_SIDE;

        solver.setAllowedSolution(allowed);
        assertEquals(allowed, solver.getAllowedSolution());
    }

    /**
     * Test the {@link #getAllowedSolutions()} method.
     */
    @Test
    public void testGetAllowedSolutions() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);

        assertEquals(BracketedUnivariateRealSolver.AllowedSolution.values().length,
                     solver.getAllowedSolutions().length);
    }

    /**
     * Test the {@link #setAllowedSolutions(AllowedSolution[])} method.
     */
    @Test
    public void testSetAllowedSolutions() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        AllowedSolution[] allowed = {BracketedUnivariateRealSolver.AllowedSolution.LEFT_SIDE,
                                      BracketedUnivariateRealSolver.AllowedSolution.RIGHT_SIDE};

        solver.setAllowedSolutions(allowed);
        assertArrayEquals(allowed, solver.getAllowedSolutions());
    }
}