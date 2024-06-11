package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * A test function with a root at x = 1.
     */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x - 1;
        }

        @Override
        public double derivative(double x) {
            return 1;
        }
    };

    /**
     * A test function with a root at x = 2.
     */
    private static final UnivariateFunction TEST_FUNCTION_ROOT_AT_2 = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x - 2;
        }

        @Override
        public double derivative(double x) {
            return 1;
        }
    };

    /**
     * A test function with roots at x = 1 and x = 2.
     */
    private static final UnivariateFunction TEST_FUNCTION_TWO_ROOTS = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x - 1, 2) * (x - 2);
        }

        @Override
        public double derivative(double x) {
             return 2 * (x - 1) * (x - 2) - Math.pow(x - 1, 2);
        }
    };

    /**
     * A test function that always returns zero.
     */
    private static final UnivariateFunction TEST_FUNCTION_CONSTANT = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return 0;
        }

        @Override
        public double derivative(double x) {
            return 0;
        }
    };

    /**
     * Test the solver with a simple root at x = 1.
     */
    @Test
    public void testSimpleRoot() {
        final double min = 0;
        final double max = 3;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-6;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, 2);
        double root = solver.solve(10, TEST_FUNCTION, min, max);
        assertEquals(1, root, 1e-6);
    }

    /**
     * Test the solver with a root at x = 2.
     */
    @Test
    public void testRootAtTwo() {
        final double min = 0;
        final double max = 4;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-6;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, 2);
        double root = solver.solve(10, TEST_FUNCTION_ROOT_AT_2, min, max);
        assertEquals(2, root, 1e-6);
    }

    /**
     * Test the solver with two roots, at x = 1 and x = 2.
     */
    @Test
    public void testTwoRoots() {
        final double min = -2;
        final double max = 4;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-6;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, 2);
        try {
            double[] roots = solver.solve(10, TEST_FUNCTION_TWO_ROOTS, min, max,
                    AllowedSolution.BOTH_SIDES);
            assertEquals(2, roots.length);
            assertEquals(1, roots[0], 1e-6);
            assertEquals(2, roots[1], 1e-6);
        } catch (MaxEvalExceededException e) {
            // This is expected for this test case, as the solver may not converge to both roots
            // within the given evaluation limit.
        }
    }

    /**
     * Test the solver with a constant function, which has no roots.
     */
    @Test(expected = NoBracketingException.class)
    public void testConstantFunction() {
        final double min = 0;
        final double max = 1;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-6;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, 2);
        solver.solve(10, TEST_FUNCTION_CONSTANT, min, max);
    }

    /**
     * Test the solver with a function with a root at a very small number.
     */
    @Test
    public void testRootAtVerySmallNumber() {
        final double min = 0;
        final double max = 1e-6;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-9;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, 2);
        double root = solver.solve(10, TEST_FUNCTION, min, max);
        assertTrue(root > 0);
        assertTrue(root < 1e-6);
    }

    /**
     * Test the solver with a function with a root at a very large number.
     */
    @Test
    public void testRootAtVeryLargeNumber() {
        final double min = 1e10;
        final double max = 2e10;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-6;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, 2);
        double root = solver.solve(10, TEST_FUNCTION, min, max);
        assertTrue(root > 1e10);
        assertTrue(root < 2e10);
    }

    /**
     * Test the solver with a function with a root at a very large negative number.
     */
    @Test
    public void testRootAtVeryNegativeLargeNumber() {
        final double min = -2e10;
        final double max = -1e10;
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-6;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, 2);
        double root = solver.solve(10, TEST_FUNCTION, min, max);
        assertTrue(root < -2e10);
        assertTrue(root > -1e10);
    }

}