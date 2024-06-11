package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.RegulaFalsiSolver;
import org.apache.commons.cli.IllinoisSolver;
import org.apache.commons.cli.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, RegulaFalsiSolver.REGULA_FALSI);

        try {
            double root = solver.solve(100, f, -10.0, 10.0);
            assertEquals(0.0, root, 1.0e-6);
        } catch (RootFindersException e) {
            fail("Should have found a root.");
        }

        // Test with negative function values.
        assertThrows(RootFindersException.class, () -> solver.solve(100, f, -10.0, 0.0));
        assertThrows(RootFindersException.class, () -> solver.solve(100, f, 0.0, 10.0));

        // Test with inverted intervals.
        BaseSecantSolver solverInverted = new RegulaFalsiSolver(1.0e-6, RegulaFalsiSolver.REGULA_FALSI);
        try {
            double rootInverted = solverInverted.solve(100, f, 10.0, -10.0);
            assertEquals(0.0, rootInverted, 1.0e-6);
        } catch (RootFindersException e) {
            fail("Should have found a root.");
        }

        // Test with MaxEvalExceededException.
        try {
            solver.solve(0, f, -10.0, 10.0);
            fail("MaxEvalExceededException expected.");
        } catch (MaxEvalExceededException e) {
            // Expected.
        }
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6, IllinoisSolver.ILLINOIS);

        try {
            double root = solver.solve(100, f, -10.0, 10.0);
            assertEquals(0.0, root, 1.0e-6);
        } catch (RootFindersException e) {
            fail("Should have found a root.");
        }

        // Test with negative function values.
        assertThrows(RootFindersException.class, () -> solver.solve(100, f, -10.0, 0.0));
        assertThrows(RootFindersException.class, () -> solver.solve(100, f, 0.0, 10.0));

        // Test with inverted intervals.
        BaseSecantSolver solverInverted = new IllinoisSolver(1.0e-6, IllinoisSolver.ILLINOIS);
        try {
            double rootInverted = solverInverted.solve(100, f, 10.0, -10.0);
            assertEquals(0.0, rootInverted, 1.0e-6);
        } catch (RootFindersException e) {
            fail("Should have found a root.");
        }

        // Test with MaxEvalExceededException.
        try {
            solver.solve(0, f, -10.0, 10.0);
            fail("MaxEvalExceededException expected.");
        } catch (MaxEvalExceededException e) {
            // Expected.
        }
    }

    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        BaseSecantSolver solver = new PegasusSolver(1.0e-6, PegasusSolver.PEGASUS);

        try {
            double root = solver.solve(100, f, -10.0, 10.0);
            assertEquals(0.0, root, 1.0e-6);
        } catch (RootFindersException e) {
            fail("Should have found a root.");
        }

        // Test with negative function values.
        assertThrows(RootFindersException.class, () -> solver.solve(100, f, -10.0, 0.0));
        assertThrows(RootFindersException.class, () -> solver.solve(100, f, 0.0, 10.0));

        // Test with inverted intervals.
        BaseSecantSolver solverInverted = new PegasusSolver(1.0e-6, PegasusSolver.PEGASUS);
        try {
            double rootInverted = solverInverted.solve(100, f, 10.0, -10.0);
            assertEquals(0.0, rootInverted, 1.0e-6);
        } catch (RootFindersException e) {
            fail("Should have found a root.");
        }

        // Test with MaxEvalExceededException.
        try {
            solver.solve(0, f, -10.0, 10.0);
            fail("MaxEvalExceededException expected.");
        } catch (MaxEvalExceededException e) {
            // Expected.
        }
    }

    // Additional tests for specific allowed solutions can be added here.

    private void assertThrows(Class<? extends Throwable> expectedExceptionClass, Runnable runnable) {
        try {
            runnable.run();
            fail("Exception expected: " + expectedExceptionClass.getName());
        } catch (Throwable e) {
            if (expectedExceptionClass.isInstance(e)) {
                // Expected.
            } else {
                fail("Unexpected exception: " + e.getClass().getName());
            }
        }
    }
}