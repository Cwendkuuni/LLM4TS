package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple function to test with. */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
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
     * Test the {@link BaseSecantSolver} with {@link RegulaFalsiSolver}.
     */
    @Test
    public void testRegulaFalsi() {
        final double tolerance = 1e-9;
        final BaseSecantSolver solver = new RegulaFalsiSolver(tolerance);
        try {
            final double root = solver.solve(TEST_FUNCTION, -3.0, 3.0);
            assertEquals("Regula Falsi: root is incorrect.", -1.0, root, tolerance);
        } catch (final ConvergenceException e) {
            // Expected
        } catch (final MaxIterationsExceededException e) {
            fail("Regula Falsi: too many iterations.");
        } catch (final MathRuntimeError e) {
            fail("Regula Falsi: unexpected error.");
        } catch (final MathIllegalArgumentException e) {
            fail("Regula Falsi: invalid argument.");
        }
    }

    /**
     * Test the {@link BaseSecantSolver} with {@link IllinoisSolver}.
     */
    @Test
    public void testIllinois() {
        final double tolerance = 1e-9;
        final BaseSecantSolver solver = new IllinoisSolver(tolerance);
        try {
            final double root = solver.solve(TEST_FUNCTION, -3.0, 3.0);
            assertEquals("Illinois: root is incorrect.", -1.0, root, tolerance);
        } catch (final ConvergenceException e) {
            // Expected
        } catch (final MaxIterationsExceededException e) {
            fail("Illinois: too many iterations.");
        } catch (final MathRuntimeError e) {
            fail("Illinois: unexpected error.");
        } catch (final MathIllegalArgumentException e) {
            fail("Illinois: invalid argument.");
        }
    }

    /**
     * Test the {@link BaseSecantSolver} with {@link PegasusSolver}.
     */
    @Test
    public void testPegasus() {
        final double tolerance = 1e-9;
        final BaseSecantSolver solver = new PegasusSolver(tolerance);
        try {
            final double root = solver.solve(TEST_FUNCTION, -3.0, 3.0);
            assertEquals("Pegasus: root is incorrect.", -1.0, root, tolerance);
        } catch (final ConvergenceException e) {
            // Expected
        } catch (final MaxIterationsExceededException e) {
            fail("Pegasus: too many iterations.");
        } catch (final MathRuntimeError e) {
            fail("Pegasus: unexpected error.");
        } catch (final MathIllegalArgumentException e) {
            fail("Pegasus: invalid argument.");
        }
    }

    /**
     * Test the {@link BaseSecantSolver} when the function has no roots.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoRoots() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI) * x;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(Math.PI) * x;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        solver.solve(function, -10.0, 10.0);
    }

    /**
     * Test the {@link BaseSecantSolver} when the function is not bracketing.
     */
    @Test(expected = ConvergenceException.class)
    public void testNotBracketing() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }

            @Override
            public double derivative(double x) {
                return 2.0 * x;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        solver.solve(function, -2.0, -1.0);
    }

    /**
     * Test the {@link BaseSecantSolver} when the function is too steep.
     */
    @Test(expected = ConvergenceException.class)
    public void testTooSteep() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - Math.pow(Math.E, 2.0 * x);
            }

            @Override
            public double derivative(double x) {
                return Math.pow(Math.E, x) * (2.0 - 2.0 * Math.E * x);
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        solver.solve(function, 0.0, 1.0);
    }
}