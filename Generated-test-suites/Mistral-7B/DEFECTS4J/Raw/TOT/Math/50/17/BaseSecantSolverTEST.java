package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple quadratic function for testing. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 4.0;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    };

    /** The solver to test. */
    private BaseSecantSolver solver;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        solver = new RegulaFalsiSolver(1.0e-6);
    }

    /**
     * Test solver with a simple quadratic function.
     */
    @Test
    public void testSolver() {
        final double solution = solver.solve(10, FUNCTION, -5.0, 5.0);
        assertEquals(2.0, solution, 1e-6);
    }

    /**
     * Test solver with a simple quadratic function with a given initial value.
     */
    @Test
    public void testSolverInitialValue() {
        final double solution = solver.solve(10, FUNCTION, -5.0, 5.0, 3.0);
        assertEquals(2.0, solution, 1e-6);
    }

    /**
     * Test solver with a simple quadratic function with an absolute accuracy.
     */
    @Test
    public void testSolverAbsoluteAccuracy() {
        final double solution = solver.solve(10, FUNCTION, -5.0, 5.0, 0.0,
                AllowedSolution.ANY_SIDE, 1e-8);
        assertEquals(2.0, solution, 1e-8);
    }

    /**
     * Test solver with a simple quadratic function with a relative and an absolute
     * accuracy.
     */
    @Test
    public void testSolverRelativeAndAbsoluteAccuracy() {
        final double solution = solver.solve(10, FUNCTION, -5.0, 5.0, 0.0,
                AllowedSolution.ANY_SIDE, 1e-5, 1e-8);
        assertEquals(2.0, solution, 1e-8);
    }

    /**
     * Test solver with a simple quadratic function with a function value accuracy.
     */
    @Test
    public void testSolverFunctionValueAccuracy() {
        final double solution = solver.solve(10, FUNCTION, -5.0, 5.0, 0.0,
                AllowedSolution.ANY_SIDE, 1e-6, 1e-6);
        assertEquals(2.0, solution, 1e-6);
    }

    /**
     * Test solver with a function that has no roots in the given interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 9.0;
            }

            @Override
            public double derivative(double x) {
                return 2.0 * x;
            }
        };
        solver.solve(10, function, -5.0, 5.0);
    }

    /**
     * Test solver with a function that is not defined at the end of the interval.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNotDefined() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 1.0) {
                    throw new UnsupportedOperationException();
                }
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2.0 * x;
            }
        };
        solver.solve(10, function, -5.0, 6.0);
    }

    /**
     * Test solver with a function that is not continuous at the interval end.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNotContinuous() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.5) {
                    throw new UnsupportedOperationException();
                }
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2.0 * x;
            }
        };
        solver.solve(10, function, -5.0, 1.0);
    }

    /**
     * Test solver with a function that is not monotonic in the interval.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testNotMonotonic() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0.0) {
                    return x * x;
                }
                return -x * x;
            }

            @Override
            public double derivative(double x) {
                return 2.0 * x;
            }
        };
        solver.solve(10, function, -1.0, 1.0);
    }

}