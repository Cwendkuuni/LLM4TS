package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Test function for solving x^2 - 1 = 0. */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(final double x) {
            return x * x - 1;
        }

        @Override
        public double derivative(final double x) {
            return 2 * x;
        }
    };

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link IllinoisSolver}.
     */
    @Test
    public void testSolve_IllinoisSolver() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, x, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link RegulaFalsiSolver}.
     */
    @Test
    public void testSolve_RegulaFalsiSolver() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, x, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link PegasusSolver}.
     */
    @Test
    public void testSolve_PegasusSolver() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, x, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link PegasusSolver} and invalid arguments.
     */
    @Test
    public void testSolve_PegasusSolver_InvalidArguments() {
        assertThrows(NullPointerException.class, () -> {
            BaseSecantSolver.class.newInstance()
                    .solve(100, null, -2.0, 2.0, 0.0, AllowedSolution.ANY_SIDE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSecantSolver.class.newInstance()
                    .solve(100, TEST_FUNCTION, -2.0, -2.0, 0.0, AllowedSolution.ANY_SIDE);
        });
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * method with the {@link IllinoisSolver}.
     */
    @Test
    public void testSolve_IllinoisSolver_MinMaxOnly() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, x, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * method with the {@link RegulaFalsiSolver}.
     */
    @Test
    public void testSolve_RegulaFalsiSolver_MinMaxOnly() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, x, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * method with the {@link PegasusSolver}.
     */
    @Test
    public void testSolve_PegasusSolver_MinMaxOnly() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, x, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link IllinoisSolver} and different starting points.
     */
    @Test
    public void testSolve_IllinoisSolver_DifferentStartingPoints() {
        final double tolerance = 1e-12;
        final double x1 = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.RIGHT_SIDE);
        final double x2 = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, x1, tolerance);
        assertEquals(0.0, x2, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link RegulaFalsiSolver} and different starting points.
     */
    @Test
    public void testSolve_RegulaFalsiSolver_DifferentStartingPoints() {
        final double tolerance = 1e-12;
        final double x1 = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        final double x2 = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, x1, tolerance);
        assertEquals(0.0, x2, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link PegasusSolver} and different starting points.
     */
    @Test
    public void testSolve_PegasusSolver_DifferentStartingPoints() {
        final double tolerance = 1e-12;
        final double x1 = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.ANY_SIDE);
        final double x2 = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, x1, tolerance);
        assertEquals(0.0, x2, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link IllinoisSolver} and the {@link NumberIsTooSmallException}.
     */
    @Test
    public void testSolve_IllinoisSolver_NumberIsTooSmallException() {
        final double tolerance = 1e-11;
        assertThrows(NumberIsTooSmallException.class, () -> {
            BaseSecantSolver.class.newInstance()
                    .solve(1, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.RIGHT_SIDE);
        });
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link MaxEvalException}.
     */
    @Test
    public void testSolve_MaxEvalException() {
        final double tolerance = 1e-12;
        assertThrows(MaxEvalException.class, () -> {
            BaseSecantSolver.class.newInstance()
                    .solve(0, TEST_FUNCTION, -2.0, 2.0, 0.0, AllowedSolution.ANY_SIDE);
        });
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link PegasusSolver} and a solution outside the bracket.
     */
    @Test
    public void testSolve_PegasusSolver_SolutionOutsideBracket() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 3.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, x, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link PegasusSolver} and a solution inside the bracket.
     */
    @Test
    public void testSolve_PegasusSolver_SolutionInsideBracket() {
        final double tolerance = 1e-12;
        final double x = BaseSecantSolver.class.newInstance()
                .solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, x, tolerance);
    }
}