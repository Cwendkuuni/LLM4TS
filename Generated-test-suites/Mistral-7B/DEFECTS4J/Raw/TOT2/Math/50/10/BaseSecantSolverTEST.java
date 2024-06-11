package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.junit.Test;
import org.apache.commons.math3.exception.MaxEvalException;
import static org.junit.Assert.*;

/**
 * Test class for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * A simple function for testing.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x - 2;
        }
    };

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                                                               BaseSecantSolver.Method.PEGASUS);
            final double root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0);
            assertEquals("Expected root to be approximately -1.4142135625", -1.4142135625, root, 1e-6);
        } catch (MaxEvalException e) {
            fail("Root finding process should have finished within the given number of evaluations");
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                                                               BaseSecantSolver.Method.PEGASUS,
                                                               AllowedSolution.LEFT_SIDE);
            final double root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0);
            assertEquals("Expected root to be approximately -1.4142135625", -1.4142135625, root, 1e-6);
        } catch (MaxEvalException e) {
            fail("Root finding process should have finished within the given number of evaluations");
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test for the case where the root is not bracketed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveNotBracketed() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                                                               BaseSecantSolver.Method.PEGASUS);
            final double root = solver.solve(100, TEST_FUNCTION, 1.0, 1.0, 0.0);
            fail("Root finding process should have thrown an IllegalArgumentException for unbracketed roots");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test for the case where the root is not bracketed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveNotBracketedAllowedSolution() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                                                               BaseSecantSolver.Method.PEGASUS,
                                                               AllowedSolution.LEFT_SIDE);
            final double root = solver.solve(100, TEST_FUNCTION, 1.0, 1.0, 0.0);
            fail("Root finding process should have thrown an IllegalArgumentException for unbracketed roots");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test for the case where the function is not defined at one of the bounds.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveFunctionNotDefinedAtBound() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        try {
            final UnivariateRealFunction function = new UnivariateRealFunction() {

                @Override
                public double value(double x) {
                    if (x == 0.0) {
                        throw new UnsupportedOperationException("Function not defined at x = 0");
                    }
                    return x * x - 2;
                }
            };
            final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                                                               BaseSecantSolver.Method.PEGASUS);
            solver.solve(100, function, 0.0, 2.0, 0.0);
            fail("Root finding process should have thrown an UnsupportedOperationException for functions not " +
                 "defined at a bound");
        } catch (UnsupportedOperationException e) {
            // Expected result
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test for the case where the function is not monotonous in the interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionNotMonotonous() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        try {
            final UnivariateRealFunction function = new UnivariateRealFunction() {
                double x = 0.0;

                @Override
                public double value(double x) {
                    this.x = x;
                    return Math.sin(this.x) / this.x;
                }
            };
            final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                                                               BaseSecantSolver.Method.REGULA_FALSI);
            solver.solve(100, function, -2.0, 2.0, 0.0);
            fail("Root finding process should have thrown an IllegalArgumentException for functions not" +
                 " monotonous in the interval");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }
}