package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)}.
     */
    @Test
    public void testBaseSecantSolverConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 1e-9);
        assertNull(solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorWithMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorWithAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-7, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(1e-6, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-7, solver.getFunctionValueAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, function, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a non-bracketing initial value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithNonBracketingInitialValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, function, 3.0, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a function that does not bracket the root.
     */
    @Test(expected = MathRuntimeError.class)
    public void testSolveWithNonBracketingFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 1;
        double root = solver.solve(10, function, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a very large relative accuracy.
     */
    @Test
    public void testSolveWithVeryLargeRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e12, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, function, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.0, 1e11));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a very large absolute accuracy.
     */
    @Test
    public void testSolveWithVeryLargeAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e12, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, function, -3.0, 3.0, 1e12, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e12);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a very small function value accuracy.
     */
    @Test
    public void testSolveWithVerySmallFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-12, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, function, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-12);
    }

}