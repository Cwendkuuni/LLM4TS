package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;

import org.junit.Test;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test solver with a simple polynomial function.
     */
    @Test
    public void testSimplePolynomial() {
        final UnivariateFunction f = new PolynomialFunction(2, -1.0, 1.0);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(1.0, solver.solve(100, f, -2.0, 2.0, 0.0), 1e-8);
    }

    /**
     * Test solver with a function having roots at the endpoints.
     */
    @Test
    public void testRootAtEndpoint() {
        final UnivariateFunction f = new PolynomialFunction(2, 1.0, 1.0);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(-1.0, solver.solve(100, f, -2.0, 2.0, 0.0), 1e-8);
    }

    /**
     * Test solver with a function having roots close to the endpoints.
     */
    @Test
    public void testRootCloseToEndpoint() {
        final UnivariateFunction f = new PolynomialFunction(2, 0.99, 1.0);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(0.99999999999999996, solver.solve(100, f, -2.0, 2.0, 0.0), 1e-9);
    }

    /**
     * Test solver with a non-bracketing initial point.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonBracketing() {
        final UnivariateFunction f = new PolynomialFunction(2, 1.0, 1.0);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(100, f, 2.0, 1.0, 0.0);
    }

    /**
     * Test solver with a non-bracketing initial point in reversed interval.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonBracketingReversed() {
        final UnivariateFunction f = new PolynomialFunction(2, 1.0, 1.0);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(100, f, 1.0, 2.0, 0.0);
    }

    /**
     * Test solver with a function having no real roots.
     */
    @Test
    public void testNoRealRoots() {
        final UnivariateFunction f = new PolynomialFunction(2, -1.0, -1.0);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertThrows(MaxEvalException.class, () -> solver.solve(100, f, -2.0, 2.0, 0.0));
    }
}