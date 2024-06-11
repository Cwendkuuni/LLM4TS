package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        // Test with a Polynomial function
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, false);
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(30, f, 1.0, 2.0, 1.5);
        assertEquals(1.0, result, 1e-6);

        // Test with a function that has no solution in the given interval
        UnivariateFunction fNoSolution = new PolynomialFunction(new double[]{-1.0, 1.0, 2.0}, false);
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(30, fNoSolution, 1.0, 2.0, 1.5);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that does not implement the required interface
        UnivariateRealFunction fNotImplemented = () -> {};
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(30, fNotImplemented, 1.0, 2.0, 1.5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a function that throws an exception in its evaluation
        UnivariateFunction fException = f -> {
            throw new RuntimeException();
        };
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(30, fException, 1.0, 2.0, 1.5);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        // Test with a Polynomial function and left-side solution
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, false);
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        double result = solver.solve(30, f, 1.0, 2.0, 1.5);
        assertEquals(1.0, result, 1e-6);

        // Test with a Polynomial function and right-side solution
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        result = solver.solve(30, f, 1.0, 2.0, 1.5);
        assertEquals(2.0, result, 1e-6);

        // Test with a Polynomial function and below-side solution
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.BELOW_SIDE);
        result = solver.solve(30, f, 1.0, 2.0, 1.5);
        assertTrue(f.value(result) < 0);

        // Test with a Polynomial function and above-side solution
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);
        result = solver.solve(30, f, 1.0, 2.0, 1.5);
        assertTrue(f.value(result) > 0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     * This test case checks for edge cases by providing the solver with a very small
     * interval and a function with very small roots.
     */
    @Test
    public void testSolveEdgeCases() {
        // Test with a Polynomial function and a very small interval
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0e-10, 1.0e-10, 1.0e-10}, false);
        BaseSecantSolver solver = new BaseSecantSolver(1e-16, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(30, f, 1.0, 2.0, 1.5);
        assertEquals(1.5, result, 1e-12);
        assertEquals(f.value(result), 0.0, 1e-12);

        // Test with a Polynomial function and a function with very small roots
        f = new PolynomialFunction(new double[]{-1.0e-15, 1.0e-15, 1.0e-15}, false);
        solver = new BaseSecantSolver(1e-16, BaseSecantSolver.Method.ILLINOIS);
        result = solver.solve(30, f, 1.0, 2.0, 1.5);
        assertEquals(1.5, result, 1e-12);
        assertEquals(f.value(result), 0.0, 1e-12);
    }
}