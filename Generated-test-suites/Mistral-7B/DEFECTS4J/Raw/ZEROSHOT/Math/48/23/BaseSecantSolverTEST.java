package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.Method;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with {@code REGULA_FALSI} method.
     * @throws Exception if test case fails
     */
    @org.junit.Test
    public void testRegulaFalsi() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-4);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double root = solver.solve(10, f, -5.0, 5.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, root, 1.0e-6);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with {@code ILLINOIS} method.
     * @throws Exception if test case fails
     */
    @org.junit.Test
    public void testIllinois() throws Exception {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-4, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double root = solver.solve(10, f, -5.0, 5.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, root, 1.0e-6);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with {@code PEGASUS} method.
     * @throws Exception if test case fails
     */
    @org.junit.Test
    public void testPegasus() throws Exception {
        BaseSecantSolver solver = new PegasusSolver(1.0e-4, 1.0e-4, 1.0e-6, Method.PEGASUS);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double root = solver.solve(100, f, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI / 2.0, root, 1.0e-6);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with {@code REGULA_FALSI} method and convergence exception.
     * @throws Exception if test case fails
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testRegulaFalsiConvergenceException() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-16);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(x)) - 1.0;
            }
        };
        solver.solve(10, f, -10.0, 10.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with {@code ILLINOIS} method and maximum iterations exceeded exception.
     * @throws Exception if test case fails
     */
    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testIllinoisMaxIterationsExceededException() throws Exception {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-16, 0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        solver.solve(1, f, -5.0, 5.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with {@code REGULA_FALSI} method and no bracketing exception.
     * @throws Exception if test case fails
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testRegulaFalsiNoBracketingException() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-16);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2.0;
            }
        };
        solver.solve(10, f, -5.0, 5.0, 0.0, AllowedSolution.LEFT_SIDE);
    }
}