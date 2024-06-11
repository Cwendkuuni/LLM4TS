package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootFindersException;
import org.junit.Test;

/**
 * Test cases for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** A simple test function for the solver (f(x) = x^2 - 1). */
    private static class TestFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 1;
        }

        @Override
        public double derivate(double x) {
            return 2.0 * x;
        }

    }

    /**
     * Tests the {@link BaseSecantSolver} with the Regula Falsi method and the
     * given test function.
     *
     * @throws Exception if the solver fails
     */
    @Test
    public void testRegulaFalsi() throws Exception {
        final double accuracy = 1e-6;
        final double min = -1.5;
        final double max = 1.5;
        final UnivariateFunction function = new TestFunction();

        final BaseSecantSolver solver = new RegulaFalsiSolver(accuracy);
        final double root = solver.solve(min, max, function);
        assertEquals("The root found by the solver is incorrect.", -1.0, root, accuracy);
    }

    /**
     * Tests the {@link BaseSecantSolver} with the Illinois method and the
     * given test function.
     *
     * @throws Exception if the solver fails
     */
    @Test
    public void testIllinois() throws Exception {
        final double accuracy = 1e-6;
        final double min = -1.5;
        final double max = 1.5;
        final UnivariateFunction function = new TestFunction();

        final BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .method(BaseSecantSolver.Method.ILLINOIS)
                .relativeAccuracy(accuracy)
                .build();
        final double root = solver.solve(min, max, function);
        assertEquals("The root found by the solver is incorrect.", -1.0, root, accuracy);
    }

    /**
     * Tests the {@link BaseSecantSolver} with the Pegasus method and the
     * given test function.
     *
     * @throws Exception if the solver fails
     */
    @Test
    public void testPegasus() throws Exception {
        final double accuracy = 1e-6;
        final double min = -1.5;
        final double max = 1.5;
        final UnivariateFunction function = new TestFunction();

        final BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .method(BaseSecantSolver.Method.PEGASUS)
                .relativeAccuracy(accuracy)
                .build();
        final double root = solver.solve(min, max, function);
        assertEquals("The root found by the solver is incorrect.", -1.0, root, accuracy);
    }

    /**
     * Tests the {@link BaseSecantSolver} with an unsolvable function.
     *
     * @throws Exception if the solver fails
     */
    @Test(expected = RootFindersException.class)
    public void testUnsolvable() throws Exception {
        final double accuracy = 1e-6;
        final double min = -1.5;
        final double max = 1.5;
        final UnivariateFunction function = new UnivariateFunction() {

            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Unsolvable function.");
            }

            @Override
            public double derivate(double x) {
                throw new UnsupportedOperationException("Unsolvable function.");
            }
        };

        final BaseSecantSolver solver = new RegulaFalsiSolver(accuracy);
        solver.solve(min, max, function);
    }

    /**
     * Tests the {@link BaseSecantSolver} with a function having a relative
     * accuracy smaller than required.
     *
     * @throws Exception if the solver fails
     */
    @Test
    public void testRelativeAccuracySmallerThanRequired() throws Exception {
        final double accuracy = 1e-10;
        final double min = -1.5;
        final double max = 1.5;
        final UnivariateFunction function = new TestFunction();

        final BaseSecantSolver solver = new RegulaFalsiSolver(accuracy);
        try {
            solver.solve(min, max, function);
            fail("The solver should not have been able to converge with the given accuracy.");
        } catch (MaxEvalExceededException e) {
            // Expected result.
        }
    }

}