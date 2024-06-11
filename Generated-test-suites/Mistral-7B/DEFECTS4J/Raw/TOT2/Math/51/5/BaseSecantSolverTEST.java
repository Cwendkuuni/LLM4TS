package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 2 * x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(100, f, -1.5, 1.5);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(100, f, -5, 5);
        assertEquals(Math.PI / 2, root, 1e-4);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has no roots.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(100, f, -1, 1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has multiple roots.
     */
    @Test
    public void testSolveMultipleRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 2 * x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double root1 = solver.solve(100, f, -2, 2);
        double root2 = solver.solve(100, f, root1, 2);
        assertEquals(-2.0, root1, 1e-8);
        assertEquals(2.0, root2, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(100, f, -1, 1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has a vertical asymptote.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveVerticalAsymptote() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1 / x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(100, f, -1, 1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has a horizontal asymptote.
     */
    @Test
    public void testSolveHorizontalAsymptote() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x / (x + 1);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(100, f, -2, 2);
        assertEquals(-1.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has a local maximum.
     */
    @Test
    public void testSolveLocalMaximum() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        try {
            solver.solve(100, f, -3.1415, 3.1415);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has a local minimum.
     */
    @Test
    public void testSolveLocalMinimum() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.cos(x), 2);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(100, f, -3.1415, 3.1415);
        assertEquals(1.0, root, 1e-8);
    }
}