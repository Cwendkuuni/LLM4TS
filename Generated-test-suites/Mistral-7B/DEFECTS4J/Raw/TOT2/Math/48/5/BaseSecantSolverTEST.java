package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithoutAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.REGULA_FALSI);
        solver.solve(10, f, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.REGULA_FALSI);
        solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.INVALID_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.REGULA_FALSI);
        final double result = solver.solve(10, f, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, result, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketing() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.REGULA_FALSI);
        solver.solve(10, f, 0.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x) - Math.log(2.0);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.REGULA_FALSI);
        solver.solve(10, f, 1.0, 2.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithMaxEvalException() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException();
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.REGULA_FALSI);
        solver.solve(1, f, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithRegulaFalsi() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.REGULA_FALSI);
        final double result = solver.solve(10, f, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, result, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithIllinois() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) * Math.sin(x);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.ILLINOIS);
        final double result = solver.solve(10, f, -1.0, 1.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, result, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithPegasus() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2.0) * Math.exp(x / 2.0);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0, Method.PEGASUS);
        final double result = solver.solve(10, f, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, result, 1e-6);
    }

}