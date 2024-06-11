package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        // Test with default parameters
        final UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        final double result = new BaseSecantSolver(0.001, Method.ILLINOIS).solve(10, testFunction, -5.0, 5.0);
        assertEquals(2.0, result, 0.001);

        // Test with custom parameters
        final double relativeAccuracy = 0.0001;
        final double absoluteAccuracy = 0.001;
        final double functionValueAccuracy = 0.0001;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.PEGASUS);
        final double root = Math.PI;
        final UnivariateRealFunction testFunction2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - root;
            }
        };
        final double result2 = solver.solve(10, testFunction2, -Math.PI, Math.PI);
        assertEquals(root, result2, relativeAccuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        // Test with default parameters
        final UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        final double result = new BaseSecantSolver(0.001, Method.ILLINOIS).solve(10, testFunction, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, result, 0.001);

        // Test with custom parameters
        final double relativeAccuracy = 0.0001;
        final double absoluteAccuracy = 0.001;
        final double functionValueAccuracy = 0.0001;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.PEGASUS);
        final double root = Math.PI;
        final UnivariateRealFunction testFunction2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - root;
            }
        };
        assertThrows(ConvergenceException.class, () -> solver.solve(10, testFunction2, -Math.PI, Math.PI, AllowedSolution.BELOW_SIDE));
        final double result2 = solver.solve(10, testFunction2, -Math.PI, Math.PI, AllowedSolution.RIGHT_SIDE);
        assertEquals(root, result2, relativeAccuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     * Test with function that has no roots in given interval
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveNoRoots() {
        final UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - Math.E;
            }
        };
        new BaseSecantSolver(0.001, Method.ILLINOIS).solve(10, testFunction, 0, 1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function that has multiple roots in given interval
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMultipleRoots() {
        final UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2 * Math.E;
            }
        };
        new BaseSecantSolver(0.001, Method.ILLINOIS).solve(10, testFunction, 0, 1);
    }
}