package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * A simple test function for testing the solver.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 2.0;
        }

        @Override
        public double derivative(double x) {
             return 2.0 * x;
        }
    };

    /**
     * Test that a simple solver with default settings finds the root of the
     * test function within reasonable tolerance.
     */
    @Test
    public void testSimpleSolver() {
        try {
            BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-4, 0.0);
            double root = solver.solve(100, TEST_FUNCTION, -3.0, 3.0);
            assertEquals(0.0, root, 1e-5);
        } catch (NoBracketException e) {
            fail("Expected the solver to find a bracket.");
        }
    }

    /**
     * Test that a solver with a small tolerance finds the root of the test
     * function within the tolerance.
     */
    @Test
    public void testTightToleranceSolver() {
        try {
            BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-8, 0.0);
            double root = solver.solve(100, TEST_FUNCTION, -3.0, 3.0);
            assertEquals(0.0, root, 1e-8);
        } catch (NoBracketException e) {
            fail("Expected the solver to find a bracket.");
        }
    }

    /**
     * Test that a solver with a large tolerance finds the root of the test
     * function within the tolerance.
     */
    @Test
    public void testLooseToleranceSolver() {
        try {
            BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-2, 0.0);
            double root = solver.solve(100, TEST_FUNCTION, -3.0, 3.0);
            assertEquals(0.0, root, 1e-2);
        } catch (NoBracketException e) {
            fail("Expected the solver to find a bracket.");
        }
    }

    /**
     * Test that a solver with an invalid tolerance fails correctly.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidToleranceSolver() {
        new BaseSecantSolver.IllinoisSolver(-1e-4, 0.0);
    }

    /**
     * Test that a solver with an invalid method fails correctly.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethodSolver() {
        new BaseSecantSolver.InvalidMethodSolver(1e-4, 0.0);
    }

    /**
     * Test that a solver with an invalid function fails correctly.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidFunctionSolver() {
        new BaseSecantSolver(1e-4, 0.0) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                                double min, double max, double startValue) {
                throw new UnsupportedOperationException();
            }
        }.solve(100, null, -3.0, 3.0);
    }

    /**
     * Test that a solver with an empty search interval fails correctly.
     */
    @Test(expected = NoBracketException.class)
    public void testEmptySearchIntervalSolver() {
        new BaseSecantSolver(1e-4, 0.0).solve(100, TEST_FUNCTION, -3.0, -3.0);
    }

    /**
     * Test that a solver with a function that has no roots fails correctly.
     */
    @Test(expected = NoBracketException.class)
    public void testNoRootsFunctionSolver() {
        new BaseSecantSolver(1e-4, 0.0).solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1.0;
            }

            @Override
            public double derivative(double x) {
                return 2.0 * x;
            }
        }, -3.0, 3.0);
    }

    /**
     * Test that a solver with a maximum number of evaluations exceeded
     * fails correctly.
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEvalSolver() {
        new BaseSecantSolver(1e-4, 0.0).solve(0, TEST_FUNCTION, -3.0, 3.0);
    }
}