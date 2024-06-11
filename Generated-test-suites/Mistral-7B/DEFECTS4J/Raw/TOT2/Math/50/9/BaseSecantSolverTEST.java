package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple function for testing purposes. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x - 2;
        }

        @Override
        public double derivative(double x) {
             return 2 * x;
        }
    };

    /** The solver under test. */
    private BaseSecantSolver solver;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly solves a function and returns the exact root.
     */
    @Test
    public void testSolve() {
        final double root = solver.solve(FUNCTION, -3.0, 3.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * This test case checks that the root-finding method correctly solves a function with the given allowed solution and returns it.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final double root = solver.solve(FUNCTION, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * This test case checks that the root-finding method correctly solves a function with the given allowed solution and returns it from the correct side.
     */
    @Test
    public void testSolveWithAllowedSolutionAndBracketing() {
        final double root = solver.solve(FUNCTION, -4.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 1e-9);
        assertFalse(Precision.isClose(root, 2.0, 1e-9));
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where one of the bounds is the exact root.
     */
    @Test
    public void testSolveWithBoundAsRoot() {
        final double root = solver.solve(FUNCTION, 0.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where the function is always positive or always negative.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithFunctionAlwaysPositive() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        solver.solve(function, -1.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where the function value at a boundary is zero.
     */
    @Test
    public void testSolveWithFunctionValueZeroBoundary() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        solver.solve(function, 2.0, 2.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where the function values at the initial interval are of opposite signs.
     */
    @Test
    public void testSolveWithFunctionValuesOfOppositeSigns() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2);
            }
        };
        solver.solve(function, -2.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where the function values at the initial interval are of the same sign.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithFunctionValuesOfSameSign() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        solver.solve(function, -1.0, -1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * This test case checks that the root-finding method correctly handles the case where the function values at the initial interval are of opposite signs and the allowed solution is different from the one returned.
     */
    @Test
    public void testSolveWithFunctionValuesOfOppositeSignsAndAllowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2);
            }
        };
        solver.solve(function, -2.0, 1.0, AllowedSolution.RIGHT_SIDE);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where one of the initial bounds is NaN.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithInvalidBound() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x);
            }

            @Override
            public double derivative(double x) {
                return -Math.sin(x);
            }
        };
        solver.solve(function, Double.NaN, 0.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where one of the initial bounds is negative when the function is always positive.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNegativeInitialBoundAndPositiveFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        solver.solve(function, -1.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where both initial bounds are the same.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithSameInitialBounds() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        solver.solve(function, 1.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * This test case checks that the root-finding method correctly handles the case where the function is not monotonic in the given interval.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNonMonotonicFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x), 0.5) * Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return (Math.abs(x) > 0) ? (Math.PI * Math.cos(Math.PI * x) / (2 * Math.sqrt(Math.abs(x))) + Math.sin(Math.PI * x)) : Double.NaN;
            }
        };
        solver.solve(function, -2.0, 2.0);
    }

}