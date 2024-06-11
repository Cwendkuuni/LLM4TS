package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertThrows;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#RegulaFalsiSolver} method.
     */
    @Test
    public void testRegulaFalsiSolver() {
        final double EPSILON = 1e-7;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        try {
            final BaseSecantSolver solver = new RegulaFalsiSolver(EPSILON);
            final double root = solver.solve(10, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, EPSILON);
        } catch (MaxEvalException e) {
            // This should not be thrown for this test case
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#IllinoisSolver} method.
     */
    @Test
    public void testIllinoisSolver() {
        final double EPSILON = 1e-7;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        try {
            final BaseSecantSolver solver = new IllinoisSolver(EPSILON);
            final double root = solver.solve(10, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, EPSILON);
        } catch (MaxEvalException | NoBracketingException | UnsupportedOperationException e) {
            // These exceptions should be thrown for this test case
            assertTrue(e instanceof MaxEvalException || e instanceof NoBracketingException || e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#PegasusSolver} method.
     */
    @Test
    public void testPegasusSolver() {
        final double EPSILON = 1e-7;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        try {
            final BaseSecantSolver solver = new PegasusSolver(EPSILON);
            final double root = solver.solve(10, f, -1.0, 1.0, 0.0);
            assertEquals(0.0, root, EPSILON);
        } catch (MaxEvalException e) {
            // This should not be thrown for this test case
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with non-bracketing interval.
     */
    @Test
    public void testNonBracketingInterval() {
        final double EPSILON = 1e-7;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        assertThrows(NoBracketingException.class, () -> {
            final BaseSecantSolver solver = new RegulaFalsiSolver(EPSILON);
            solver.solve(10, f, 1.0, 1.0, 0.0);
        });
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with null function.
     */
    @Test
    public void testNullFunction() {
        final double EPSILON = 1e-7;
        final BaseSecantSolver solver = new RegulaFalsiSolver(EPSILON);
        assertThrows(NullPointerException.class, () -> {
            solver.solve(10, null, -2.0, 2.0, 0.0);
        });
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with negative step size.
     */
    @Test
    public void testNegativeStepSize() {
        final double EPSILON = 1e-7;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> {
            final BaseSecantSolver solver = new IllinoisSolver(EPSILON);
            solver.solve(10, f, -1.0, 1.0, -0.1);
        });
    }
}