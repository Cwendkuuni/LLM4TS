package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -1}, 3);

        try {
            double root = solver.solve(100, f, -10, 10);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 2.0, AllowedSolution.LEFT_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, -2.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 0.0, AllowedSolution.BELOW_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 0.0, AllowedSolution.ABOVE_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 5.0, AllowedSolution.LEFT_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 5.0, AllowedSolution.RIGHT_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 3.0, AllowedSolution.ABOVE_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 3.0, AllowedSolution.BELOW_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testIllinoisSolver() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -1}, 3);

        try {
            double root = solver.solve(100, f, -10, 10);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 2.0, AllowedSolution.LEFT_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, -2.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 0.0, AllowedSolution.BELOW_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 0.0, AllowedSolution.ABOVE_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 5.0, AllowedSolution.LEFT_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 5.0, AllowedSolution.RIGHT_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 3.0, AllowedSolution.ABOVE_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 3.0, AllowedSolution.BELOW_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testPegasusSolver() {
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -1}, 3);

        try {
            double root = solver.solve(100, f, -10, 10);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 2.0, AllowedSolution.LEFT_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, -2.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 0.0, AllowedSolution.BELOW_SIDE);
            assertEquals(1.0, root, 1e-6);
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 0.0, AllowedSolution.ABOVE_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 5.0, AllowedSolution.LEFT_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 5.0, AllowedSolution.RIGHT_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 3.0, AllowedSolution.ABOVE_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 3.0, AllowedSolution.BELOW_SIDE);
            fail("Unexpected result");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (MaxEvalExceededException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, 2.5, AllowedSolution.LEFT_SIDE);
            assertTrue(Precision.isClose(root, 1.0, 1e-6));
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }

        try {
            double root = solver.solve(100, f, -10, 10, -2.5, AllowedSolution.RIGHT_SIDE);
            assertTrue(Precision.isClose(root, 1.0, 1e-6));
        } catch (MaxEvalExceededException | NumberIsTooSmallException | UnsupportedOperationException e) {
            fail("Unexpected exception thrown");
        }
    }
}