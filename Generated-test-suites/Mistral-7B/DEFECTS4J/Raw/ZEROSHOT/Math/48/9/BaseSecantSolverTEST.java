package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionRequirementExceededException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test a polynomial function with known roots.
     */
    @Test
    public void testPolynomial() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .besselJ0(0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1.0e-6);
    }

    /**
     * Test a polynomial function with known roots and a different solver method.
     */
    @Test
    public void testPolynomialRegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .besselJ0(0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.REGULA_FALSI);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1.0e-6);
    }

    /**
     * Test a polynomial function with known roots and a given tolerance on the
     * minimum number of iterations.
     */
    @Test
    public void testPolynomialMinIterations() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .besselJ0(0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                0.0, 1.0e-6, Method.ILLINOIS, 5);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1.0e-6);
    }

    /**
     * Test a polynomial function with known roots and a given tolerance on the
     * maximum number of iterations.
     */
    @Test
    public void testPolynomialMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .besselJ0(0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS, 10);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1.0e-6);
    }

    /**
     * Test a polynomial function with known roots, tolerance on the function
     * value, and a given tolerance on the maximum number of iterations.
     */
    @Test
    public void testPolynomialMaxIterationsFunctionValue() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .besselJ0(0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-4, 1.0e-6, Method.ILLINOIS, 10);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1.0e-6);
    }

    /**
     * Test an unbracketed function.
     */
    @Test
    public void testUnbracketed() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .sin(Math.PI);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(UnbracketedException.class, () -> solver.solve(function, 0.0, 0.0));
    }

    /**
     * Test a function with a singularity.
     */
    @Test
    public void testSingularity() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(0.5, 2.0)
                .divide(new PolynomialFunctions()
                        .pow(Math.PI, 2.0));
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(ConvergenceException.class, () -> solver.solve(function, -1.0, 1.0));
    }

    /**
     * Test a function with a singularity at the minimum.
     */
    @Test
    public void testSingularityMin() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(0.5, 2.0)
                .divide(new PolynomialFunctions()
                        .pow(Math.PI, 2.0));
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(ConvergenceException.class, () -> solver.solve(function, -1.0, 0.0));
    }

    /**
     * Test a function with a singularity at the maximum.
     */
    @Test
    public void testSingularityMax() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(0.5, 2.0)
                .divide(new PolynomialFunctions()
                        .pow(Math.PI, 2.0));
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(ConvergenceException.class, () -> solver.solve(function, 0.0, 1.0));
    }

    /**
     * Test a function with a singularity at a point between the minimum and the
     * maximum.
     */
    @Test
    public void testSingularityMid() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(0.5, 2.0)
                .divide(new PolynomialFunctions()
                        .pow(Math.PI, 2.0));
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(ConvergenceException.class, () -> solver.solve(function, 0.1, 0.9));
    }

    /**
     * Test a function with no roots in a given interval.
     */
    @Test
    public void testNoRoots() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(Math.E, x -> Math.cos(x) - Math.sin(x));
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(MaxIterationsExceededException.class, () -> solver
                .solve(function, -Math.PI, Math.PI));
    }

    /**
     * Test a function with roots on the boundary of the interval.
     */
    @Test
    public void testRootsOnBoundary() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(x -> Math.sin(x), 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        double root = solver.solve(function, -Math.PI, Math.PI);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Test a function with roots on opposite sides of the interval.
     */
    @Test
    public void testRootsOppositeSides() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(x -> Math.sin(x), 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        double root1 = solver.solve(function, -Math.PI, 0.0);
        double root2 = solver.solve(function, 0.0, Math.PI);
        assertEquals(Math.PI / 2.0, root1, 1.0e-6);
        assertEquals(-Math.PI / 2.0, root2, 1.0e-6);
    }

    /**
     * Test a function with roots that are not bracketed initially.
     */
    @Test
    public void testRootsNotBracketed() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(Math.sin, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(UnbracketedException.class, () -> solver.solve(function, 0.0, 0.5));
    }

    /**
     * Test a function with roots that are not bracketed initially but are
     * found when the algorithm converges.
     */
    @Test
    public void testRootsNotBracketedConverge() {
        UnivariateRealFunction function = new PolynomialFunctions()
                .pow(Math.sin, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(
                1.0e-6, Method.ILLINOIS);
        assertThrows(PrecisionRequirementExceededException.class, () -> solver
                .solve(function, 1.0, 2.0));
    }

}