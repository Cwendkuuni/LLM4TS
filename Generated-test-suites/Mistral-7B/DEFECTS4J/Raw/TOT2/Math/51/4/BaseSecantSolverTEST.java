package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test suite for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#ILLINOIS} method.
     */
    @Test
    public void testSolveIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
        UnivariateFunction f = new Polynomials.Quartic(1, -1, 1, -1, 1);
        double x = solver.solve(100, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, x, Precision.EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#PEGASUS} method.
     */
    @Test
    public void testSolvePegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
        UnivariateFunction f = new Polynomials.Quartic(1, -1, 1, -1, 1);
        double x = solver.solve(100, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, x, Precision.EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#REGULA_FALSI} method.
     */
    @Test
    public void testSolveRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
        UnivariateFunction f = new Polynomials.Quartic(1, -1, 1, -1, 1);
        double x = solver.solve(100, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, x, Precision.EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#REGULA_FALSI} method and unbracketed bounds.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
        UnivariateFunction f = new Polynomials.Quartic(1, 1, 1, 1, 1);
        solver.solve(100, f, 0.0, 0.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#ILLINOIS} method and maxEval.
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
        UnivariateFunction f = new Polynomials.Quartic(1, -1, 1, -1, 1);
        solver.solve(0, f, -2.0, 2.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with invalid method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, null) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
        UnivariateFunction f = new Polynomials.Quartic(1, -1, 1, -1, 1);
        solver.solve(100, f, -2.0, 2.0, 0.0);
    }
}