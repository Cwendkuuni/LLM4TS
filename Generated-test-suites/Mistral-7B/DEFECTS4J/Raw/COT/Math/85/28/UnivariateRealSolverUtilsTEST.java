package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertDoubleNaNs;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketedBrentSolver;
import org.apache.commons.math3.solvers.LBFGSBRegularizer;
import org.apache.commons.math3.solvers.PowellSolver;
import org.apache.commons.math3.solvers.SecantSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverFactory;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * JUnit test for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test solver functionality.
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testSolver() throws Exception {
        // Test solve method with default settings
        UnivariateRealFunction testFunction = new FunctionWithRoot(1.0);
        double result = UnivariateRealSolverUtils.solve(testFunction, -1.0, 3.0);
        assertTrue("Solve method should return the root of the function", testFunction.value(result) < 1e-10);

        // Test solve method with specified absolute accuracy
        double absoluteAccuracy = 1e-5;
        result = UnivariateRealSolverUtils.solve(testFunction, -1.0, 3.0, absoluteAccuracy);
        assertTrue("Solve method should return the root of the function with specified absolute accuracy",
                Math.abs(testFunction.value(result)) < absoluteAccuracy);

        // Test exception handling
        testFunction = new FunctionWithRootNegative(-1.0);
        try {
            UnivariateRealSolverUtils.solve(testFunction, 0.0, 0.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        testFunction = new FunctionWithRootAtEndpoints(-1.0, -1.0);
        try {
            UnivariateRealSolverUtils.solve(testFunction, -1.0, -1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        testFunction = new FunctionWithRootAtEndpoints(1.0, 1.0);
        try {
            UnivariateRealSolverUtils.solve(testFunction, 1.0, 1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        testFunction = new FunctionWithDifferentRoots();
        try {
            UnivariateRealSolverUtils.solve(testFunction, -1.0, 3.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        // Test bracket method functionality
        UnivariateRealFunction function = new FunctionWithRoot(1.0);
        double[] bracketing = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 3.0);
        assertArrayEquals(new double[]{0.0, 1.0}, bracketing, 0.0);

        function = new FunctionWithRootNegative(-1.0);
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        function = new FunctionWithRootAtEndpoints(-1.0, -1.0);
        try {
            UnivariateRealSolverUtils.bracket(function, -1.0, -1.0, 1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        function = new FunctionWithRootAtEndpoints(1.0, 1.0);
        try {
            UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        function = new FunctionWithDifferentRoots();
        try {
            UnivariateRealSolverUtils.bracket(function, -1.0, 3.0, 3.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        // Test midpoint method functionality
        assertDoubleNaNs(UnivariateRealSolverUtils.midpoint(Double.NaN, 1.0));
        assertDoubleNaNs(UnivariateRealSolverUtils.midpoint(1.0, Double.NaN));
        double midpoint = UnivariateRealSolverUtils.midpoint(1.0, 2.0);
        assertEquals("Midpoint method should return the average of two given values", 1.5, midpoint, 1e-10);
    }

    /**
     * Utility function for testing solvers.
     */
    private static class FunctionWithRoot implements UnivariateRealFunction {
        private double root;

        public FunctionWithRoot(double root) {
            this.root = root;
        }

        @Override
        public double value(double x) {
            return x - this.root;
        }
    }

    /**
     * Utility function for testing solvers with a negative root.
     */
    private static class FunctionWithRootNegative implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sqrt(x + 1.0) - 1.0;
        }
    }

    /**
     * Utility function for testing solvers with roots at the endpoints.
     */
    private static class FunctionWithRootAtEndpoints implements UnivariateRealFunction {
        private double root;
        private double lowerBound;

        public FunctionWithRootAtEndpoints(double root, double lowerBound) {
            this.root = root;
            this.lowerBound = lowerBound;
        }

        @Override
        public double value(double x) {
            return Math.pow(x - this.root, 2);
        }

        @Override
        public double value(double x, double step) {
            return Math.pow(x - this.root, 2);
        }
    }

    /**
     * Utility function for testing solvers with different roots.
     */
    private static class FunctionWithDifferentRoots implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 1.0, 2) - Math.pow(x - 2.0, 2);
        }
    }

    /**
     * Utility function for testing solvers.
     */
    private static class FunctionWithRootAtZero implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }
    }

    /**
     * Utility function for testing bracket method.
     */
    private static class FunctionWithBracketRoot implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.cos(x) - 0.5;
        }
    }

    /**
     * Helper method for asserting equality with a given tolerance.
     * @param expected expected value
     * @param actual  actual value
     * @param tolerance tolerance
     */
    private void assertEquals(double expected, double actual, double tolerance) {
        double diff = Math.abs(expected - actual);
        assertTrue("Expected: " + expected + ", but got: " + actual
                + ", with a difference of: " + diff, diff <= tolerance);
    }
}