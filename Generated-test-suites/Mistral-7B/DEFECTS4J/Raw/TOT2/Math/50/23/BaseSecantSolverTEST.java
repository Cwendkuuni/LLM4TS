package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * <p>
     * Test a simple function with known roots.
     * </p>
     */
    @Test
    public void testSolveSimpleFunction() {
        UnivariateFunction func = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new PegasusSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(100, func, -2.5, 2.5, 0.0);
        assertTrue(Precision.doubleEquals(root, -1.0, 1e-6));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * <p>
     * Test a function with roots at the bounds.
     * </p>
     */
    @Test
    public void testSolveFunctionWithRootsAtBounds() {
        UnivariateFunction func = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new PegasusSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(100, func, -2.0, -2.0, 0.0);
            fail("Expected NoBracketException");
        } catch (NoBracketException e) {
            // Expected
        }

        try {
            solver.solve(100, func, 2.0, 2.0, 0.0);
            fail("Expected NoBracketException");
        } catch (NoBracketException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * <p>
     * Test a function with roots between the bounds.
     * </p>
     */
    @Test
    public void testSolveFunctionWithRootsBetweenBounds() {
        UnivariateFunction func = x -> x * x - 2;
        BaseSecantSolver solver = new PegasusSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(100, func, -1.5, 1.5, 0.0);
        assertTrue(Precision.doubleEquals(root, 0.0, 1e-6));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * <p>
     * Test a function with no roots in the interval.
     * </p>
     */
    @Test
    public void testSolveFunctionWithNoRoots() {
        UnivariateFunction func = x -> Math.sin(x) - Math.cos(x);
        BaseSecantSolver solver = new PegasusSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(100, func, -10.0, 10.0, 0.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * <p>
     * Test a method with an illegal argument.
     * </p>
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIllegalArgument() {
        UnivariateFunction func = x -> x * x - 2;
        BaseSecantSolver solver = new PegasusSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        solver.solve(-1, func, 0.0, 0.0, 0.0);
    }
}