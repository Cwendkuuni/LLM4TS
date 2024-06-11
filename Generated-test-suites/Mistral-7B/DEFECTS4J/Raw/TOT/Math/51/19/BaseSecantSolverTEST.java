package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** A simple test function with a root at x = 1. */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x - 1;
        }

        @Override
        public double derivate(double x) {
            return 1;
        }
    };

    /**
     * Test the solver on a simple test function with a known root at x = 1.
     * @throws MaxEvalException if the solver fails to converge within the
     * given maximum number of evaluations.
     * @throws NoBracketException if the initial bounds do not bracket the
     * root.
     */
    @Test
    public void testSimpleFunction() throws MaxEvalException, NoBracketException {
        final double min = 0.0;
        final double max = 2.0;
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, maxEval, BaseSecantSolver.Method.REGULA_FALSI);

        // The root is within the bounds, so a solution should be found.
        double root = solver.solve(TEST_FUNCTION, min, max);
        assertEquals("The solver did not find the root.", 1.0, root, 1e-6);
    }

    /**
     * Test the solver on a simple test function with a known root at x = 1,
     * but with initial bounds outside the root.
     * @throws MaxEvalException if the solver fails to converge within the
     * given maximum number of evaluations.
     * @throws NoBracketException if the initial bounds do not bracket the
     * root.
     */
    @Test(expected = NoBracketException.class)
    public void testOutsideBounds() throws MaxEvalException, NoBracketException {
        final double min = 2.0;
        final double max = 0.0;
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, maxEval, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(TEST_FUNCTION, min, max);
    }

    /**
     * Test the solver on a simple test function with a known root at x = 1,
     * but with a tiny step size causing it to fail to converge.
     * @throws MaxEvalException if the solver fails to converge within the
     * given maximum number of evaluations.
     * @throws NoBracketException if the initial bounds do not bracket the
     * root.
     */
    @Test(expected = MaxEvalException.class)
    public void testSmallStepSize() throws MaxEvalException, NoBracketException {
        final double min = 0.0;
        final double max = 1.0;
        final double relativeAccuracy = 1e-12;
        final double absoluteAccuracy = 1e-13;
        final int maxEval = 1000;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, maxEval, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(TEST_FUNCTION, min, max);
    }

    /**
     * Test the solver on a simple test function with a known root at x = 1,
     * but with a very loose tolerance.
     * @throws MaxEvalException if the solver fails to converge within the
     * given maximum number of evaluations.
     * @throws NoBracketException if the initial bounds do not bracket the
     * root.
     */
    @Test
    public void testLooseTolerance() throws MaxEvalException, NoBracketException {
        final double min = 0.0;
        final double max = 2.0;
        final double relativeAccuracy = 1e-2;
        final double absoluteAccuracy = 1e-3;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, maxEval, BaseSecantSolver.Method.REGULA_FALSI);

        // The root is within the bounds, but not within the tolerance.
        double root = solver.solve(TEST_FUNCTION, min, max);
        assertTrue("The solver found a solution too precisely.",
                Math.abs(1.0 - root) > 0.1);
    }
}