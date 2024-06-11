package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.junit.Test;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructorRelative() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-7, Method.ILLINOIS);
        assertEquals(1e-5, solver.getMaxRelativeError(), 1e-9);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructorRelativeAbsolute() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, 1e-9, Method.REGULA_FALSI);
        assertEquals(1e-5, solver.getMaxRelativeError(), 1e-9);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        double x = solver.solve(100, new SinFunction(), -10, 10, 0);
        assertEquals(0, x, 1e-7);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        double x = solver.solve(100, new SinFunction(), -10, 10, 0, AllowedSolution.LEFT_SIDE);
        assertFalse(Double.isNaN(x));
        assertEquals(-2.4495, x, 1e-7);

        x = solver.solve(100, new SinFunction(), -10, 10, 0, AllowedSolution.RIGHT_SIDE);
        assertFalse(Double.isNaN(x));
        assertEquals(2.4495, x, 1e-7);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Bracketing exception test.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(100, new SinFunction(), 2.5, 2.5, 0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Max iterations reached exception test.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(1, new SinFunction(), -10, 10, 0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Precision exception test.
     */
    @Test(expected = PrecisionException.class)
    public void testPrecisionException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-34, Method.PEGASUS);
        solver.solve(100, new SinFunction(), -1, 1, 0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Unbracketed exception test.
     */
    @Test(expected = UnbracketedException.class)
    public void testUnbracketedException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(100, new SinFunction(), 0, 0, 0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Bracketing exception test.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingExceptionAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(100, new SinFunction(), -10, 10, 0, AllowedSolution.LEFT_SIDE);
        solver.solve(100, new SinFunction(), -10, 10, 0, AllowedSolution.RIGHT_SIDE);
        solver.solve(100, new SinFunction(), 2.5, 2.5, 0, AllowedSolution.LEFT_SIDE);
        solver.solve(100, new SinFunction(), 2.5, 2.5, 0, AllowedSolution.RIGHT_SIDE);
        solver.solve(100, new SinFunction(), 0, 0, 0, AllowedSolution.LEFT_SIDE);
        solver.solve(100, new SinFunction(), 0, 0, 0, AllowedSolution.RIGHT_SIDE);
        solver.solve(100, new SinFunction(), -10, 10, 0, AllowedSolution.BELOW_SIDE);
        solver.solve(100, new SinFunction(), -10, 10, 0, AllowedSolution.ABOVE_SIDE);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Max iterations reached exception test.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededExceptionAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(1, new SinFunction(), -10, 10, 0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Precision exception test.
     */
    @Test(expected = PrecisionException.class)
    public void testPrecisionExceptionAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-34, Method.PEGASUS);
        solver.solve(100, new SinFunction(), -1, 1, 0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Unbracketed exception test.
     */
    @Test(expected = UnbracketedException.class)
    public void testUnbracketedExceptionAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(100, new SinFunction(), 0, 0, 0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Sin function for testing.
     */
    private static class SinFunction implements UnivariateRealFunction {

        @Override
        public double value(final double x) {
            return Math.sin(x);
        }

        @Override
        public double derivative(final double x) {
            return Math.cos(x);
        }

        @Override
        public double[] gradient(final double[] x) {
            return new double[] {-Math.cos(x[0])};
        }

        @Override
        public double[] value(final double[] x) {
            return new double[] {Math.sin(x[0])};
        }

        @Override
        public double[] gradient(final double[] x, final int size) {
            return new double[0];
        }

        @Override
        public double[] value(final double[] x, final int size) {
            return new double[0];
        }

    }

}