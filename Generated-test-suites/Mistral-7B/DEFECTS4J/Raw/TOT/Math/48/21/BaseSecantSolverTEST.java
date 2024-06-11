package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * A simple test function for the solver.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x - 2;
        }

        @Override
        public double derivative(double x) {
             return 2 * x;
        }
    };

    /**
     * Test the solver with a simple function where we know the roots.
     */
    @Test
    public void testSolver() {
        final double absTol = 1e-12;
        final double relTol = 1e-12;
        final int maxIter = 100;

        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, maxIter, BaseSecantSolver.Method.ILLINOIS);

        final double root = solver.solve(TEST_FUNCTION, -10.0, 10.0);
        assertEquals("Wrong root", -1.0, root, absTol);

        final double root2 = solver.solve(TEST_FUNCTION, 1.0, 2.0);
        assertEquals("Wrong root", 1.0, root2, absTol);
    }

    /**
     * Test the solver when the root is outside of the bracketing.
     */
    @Test
    public void testSolverOutsideBracketing() {
        final double absTol = 1e-12;
        final double relTol = 1e-12;
        final int maxIter = 100;

        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, maxIter, BaseSecantSolver.Method.ILLINOIS);

        assertThrows(NoBracketingException.class, () -> solver.solve(TEST_FUNCTION, 10.0, 1.0));
    }

    /**
     * Test the solver when the maximum number of iterations is exceeded.
     */
    @Test
    public void testSolverMaxIterations() {
        final double absTol = 1e-12;
        final double relTol = 1e-12;
        final int maxIter = 0;

        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, maxIter, BaseSecantSolver.Method.ILLINOIS);

        assertThrows(MaxEvalExceededException.class, () -> solver.solve(TEST_FUNCTION, -10.0, 10.0));
    }

    /**
     * Test the solver when the function value is too large.
     */
    @Test
    public void testSolverFunctionValueTooLarge() {
        final double absTol = 1e-12;
        final double relTol = 1e-12;
        final int maxIter = 100;

        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, maxIter, BaseSecantSolver.Method.ILLINOIS);

        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Double.MAX_VALUE;
            }

            @Override
            public double derivative(double x) {
                return 0;
            }
        };

        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(function, -1.0, 1.0));
    }

    /**
     * Test the solver with a function that has no roots.
     */
    @Test
    public void testSolverFunctionNoRoots() {
        final double absTol = 1e-12;
        final double relTol = 1e-12;
        final int maxIter = 100;

        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, maxIter, BaseSecantSolver.Method.ILLINOIS);

        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }

            @Override
            public double derivative(double x) {
                return 1;
            }
        };

        assertNull("Solver must not find a root", solver.solve(function, -1.0, 1.0));
    }

    /**
     * Test the solver when the function is not differentiable.
     */
    @Test
    public void testSolverFunctionNotDifferentiable() {
        final double absTol = 1e-12;
        final double relTol = 1e-12;
        final int maxIter = 100;

        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, maxIter, BaseSecantSolver.Method.ILLINOIS);

        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.log(x);
                } else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public double derivative(double x) {
                // Not differentiable at x = 0.
                if (x == 0) {
                    throw new UnsupportedOperationException();
                }
                return 1 / x;
            }
        };

        final double root = solver.solve(function, -1.0, 1.0);
        // The root is approximately 0.718281828.
        assertEquals("Wrong root", Precision.round(Math.E, 6), root, absTol);
    }
}