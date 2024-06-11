package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.IllegalRootStateException;
import org.apache.commons.commons.math.analysis.solvers.Method;

public class BaseSecantSolverTest {
    private static final double EPSILON = 1e-9;

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArguments() {
        new BaseSecantSolver(Double.NaN, Method.ILLINOIS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArguments2() {
        new BaseSecantSolver(-1.0, Method.ILLINOIS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArguments3() {
        new BaseSecantSolver(0.0, Method.ILLINOIS);
    }

    @Test
    public void testConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testConstructor2() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, 1e-6, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), EPSILON);
        assertEquals(1e-3, solver.getRelativeAccuracy(), EPSILON);
    }

    @Test
    public void testConstructor3() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, 1e-6, 1e-9, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), EPSILON);
        assertEquals(1e-3, solver.getRelativeAccuracy(), EPSILON);
        assertEquals(1e-9, solver.getFunctionValueAccuracy(), EPSILON);
    }

    @Test
    public void testAllowedSolutions() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.BELOW_SIDE);
        assertEquals(AllowedSolution.BELOW_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);
        assertEquals(AllowedSolution.ABOVE_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        double[] roots = {-0.5235987755982989, 1.0471975511965978};
        double x = solver.solve(20, function, -2.0, 2.0, 0.0);
        assertArrayEquals(new double[]{roots[0], roots[1]}, new double[]{x, x}, 1e-9);
    }

    @Test(expected = IllegalRootStateException.class)
    public void testSolveDivergent() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.log(Math.abs(x - 1));
            }

            @Override
            public double derivative(double x) {
                return 1.0 / (x - 1);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(20, function, 0.5, 1.5);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalExceeded() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.abs(x)) - Math.log(Math.abs(x) + 1);
            }

            @Override
            public double derivative(double x) {
                return (Math.E * Math.abs(x) + 1) / (Math.abs(x) + 1);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(0, function, -10, 10, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSolveNumberIsTooLarge() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.exp(x) - Math.pow(2, x);
            }

            @Override
            public double derivative(double x) {
                return Math.exp(x) + Math.pow(2, x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(20, function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
    }

    @Test(expected = PrecisionException.class)
    public void testSolvePrecisionException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-18, Method.PEGASUS);
        solver.solve(20, function, -1.0, 1.0, 0.0);
    }

    @Test
    public void testSolveBracketing() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertFalse(solver.isBracketing(Double.NEGATIVE_INFINITY, Double.NaN));
        assertFalse(solver.isBracketing(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertFalse(solver.isBracketing(Double.NEGATIVE_INFINITY, 0.5));
        assertFalse(solver.isBracketing(Double.NEGATIVE_INFINITY, -0.5));
        assertFalse(solver.isBracketing(Double.POSITIVE_INFINITY, Double.NaN));
        assertFalse(solver.isBracketing(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertFalse(solver.isBracketing(Double.POSITIVE_INFINITY, 0.5));
        assertFalse(solver.isBracketing(Double.POSITIVE_INFINITY, -0.5));
        assertTrue(solver.isBracketing(0.5, -0.5));
        assertTrue(solver.isBracketing(-0.5, 0.5));
    }

    @Test
    public void testGetMin() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals(Double.NEGATIVE_INFINITY, solver.getMin(), 0.0);
    }

    @Test
    public void testGetMax() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals(Double.POSITIVE_INFINITY, solver.getMax(), 0.0);
    }

    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.BELOW_SIDE);
        assertEquals(AllowedSolution.BELOW_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);
        assertEquals(AllowedSolution.ABOVE_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.ANY_SIDE);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testGetMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());

        solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.getMethod());

        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testIsBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertFalse(solver.isBracketing(-1.0, 0.5));
        assertFalse(solver.isBracketing(-1.0, 1.0));
        assertFalse(solver.isBracketing(0.5, 1.0));
        assertTrue(solver.isBracketing(0.5, 0.5));
        assertTrue(solver.isBracketing(0.5, -0.5));
        assertTrue(solver.isBracketing(-0.5, 0.5));
        assertTrue(solver.isBracketing(-0.5, -0.5));
    }

    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        try {
            solver.verifyBracketing(0.5, 1.0);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            solver.verifyBracketing(0.5, 0.5);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }

        try {
            solver.verifyBracketing(0.5, -0.5);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testEqualsAndHashcode() {
        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-5, Method.ILLINOIS);

        assertEquals(solver1, solver1);
        assertEquals(solver1, solver2);
        assertNotEquals(solver1, solver3);
    }

    @Test
    public void testToString() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals("BaseSecantSolver [absoluteAccuracy=1.0E-6, method=ILLINOIS, min=Double.NEGATIVE_INFINITY, max=Double.POSITIVE_INFINITY, relativeAccuracy=1.0E-6]", solver.toString());
    }
}