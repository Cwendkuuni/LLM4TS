package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithAnySide() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        final double root = solver.solve(100, function, -10, 10);
        assertEquals("Solution not correct", 1.0, root, 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        final double root = solver.solve(100, function, -10, 10, 2.0);
        assertEquals("Solution not correct", 1.0, root, 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Checks case when function doesn't bracket a root and algorithm fails.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveNoBracketing() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x * x, 0.25);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, function, 0, 1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Checks case when maximum number of iterations is reached.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testSolveMaxIterations() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x * x, 0.25);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI, 1);
        solver.solve(1, function, 0, 1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Checks case when an exception is thrown during method execution.
     */
    @Test
    public void testSolveException() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                throw new IllegalArgumentException();
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, function, -10, 10));
    }

}