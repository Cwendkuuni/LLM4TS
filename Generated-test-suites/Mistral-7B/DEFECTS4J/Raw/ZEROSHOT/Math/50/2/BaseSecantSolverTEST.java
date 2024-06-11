package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.PrecisionException;
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
        final UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 2 * x * x + 1);
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);

        assertEquals(0.0, solver.solve(10, f, -1.0, 1.0), 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        final UnivariateFunction f = x -> Math.sin(x) - 0.5;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);

        assertThrows(NoBracketException.class, () -> solver.solve(10, f, 0.0, 0.0, AllowedSolution.LEFT_SIDE));
        assertEquals(Math.PI / 4, solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE), 1e-7);
        assertEquals(-Math.PI / 4, solver.solve(10, f, -1.0, 1.0, AllowedSolution.RIGHT_SIDE), 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with function value tolerance.
     */
    @Test
    public void testSolveFunctionValueTolerance() {
        final UnivariateFunction f = x -> Math.pow(Math.sin(x), 2) - 0.25;
        final BaseSecantSolver solver = new PegasusSolver(1e-6, 1e-8, PegasusSolver.Method.PEGASUS);

        assertEquals(Math.PI / 2, solver.solve(10, f, -1.0, 1.0, 0.0), 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with max iterations.
     */
    @Test
    public void testSolveMaxIterations() {
        final UnivariateFunction f = x -> Math.pow(Math.sin(x), 2) - 0.25;
        final BaseSecantSolver solver = new PegasusSolver(1e-6, 1e-8, PegasusSolver.Method.PEGASUS);

        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(0, f, -1.0, 1.0, 0.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)} with invalid method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethod() {
        final UnivariateFunction f = x -> Math.pow(Math.sin(x), 2) - 0.25;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, RegulaFalsiSolver.Method.ILLINOIS);
        solver.solve(10, f, -1.0, 1.0);
    }
}