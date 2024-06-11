package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    /** A simple test function for the solvers. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) - 0.5 * x;
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x) - 0.5;
        }
    };

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#REGULA_FALSI}.
     */
    @Test
    public void testRegulaFalsi() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0);
            Assert.assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#ILLINOIS}.
     */
    @Test
    public void testIllinois() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, Method.ILLINOIS);
            Assert.assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#PEGASUS}.
     */
    @Test
    public void testPegasus() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, Method.PEGASUS);
            Assert.assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#REGULA_FALSI} and the function value accuracy.
     */
    @Test
    public void testRegulaFalsiFunctionValueAccuracy() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, 1e-6);
            Assert.assertEquals(0.0, root, 1e-10);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#ILLINOIS} and the function value accuracy.
     */
    @Test
    public void testIllinoisFunctionValueAccuracy() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, 1e-6, Method.ILLINOIS);
            Assert.assertEquals(0.0, root, 1e-10);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#PEGASUS} and the function value accuracy.
     */
    @Test
    public void testPegasusFunctionValueAccuracy() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, 1e-6, Method.PEGASUS);
            Assert.assertEquals(0.0, root, 1e-10);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#REGULA_FALSI} and the relative and absolute accuracies.
     */
    @Test
    public void testRegulaFalsiAccuracies() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, 1e-6, 1e-8);
            Assert.assertEquals(0.0, root, 1e-11);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#ILLINOIS} and the relative and absolute accuracies.
     */
    @Test
    public void testIllinoisAccuracies() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, 1e-6, 1e-8, Method.ILLINOIS);
            Assert.assertEquals(0.0, root, 1e-11);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#PEGASUS} and the relative and absolute accuracies.
     */
    @Test
    public void testPegasusAccuracies() {
        try {
            final double root = BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, 1e-6, 1e-8, Method.PEGASUS);
            Assert.assertEquals(0.0, root, 1e-11);
        } catch (MaxEvalExceededException e) {
            // Should not happen with the given max evaluations.
            Assert.fail("MaxEvalExceededException should not be thrown.");
        } catch (NoBracketingException e) {
            // Should not happen with the given function.
            Assert.fail("NoBracketingException should not be thrown.");
        } catch (UnsupportedOperationException e) {
            // Should not happen with the given function.
            Assert.fail("UnsupportedOperationException should not be thrown.");
        }
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#REGULA_FALSI} and invalid allowed solution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegulaFalsiInvalidAllowedSolution() {
        BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE_AND_ABOVE_SIDE);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#ILLINOIS} and invalid allowed solution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllinoisInvalidAllowedSolution() {
        BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE_AND_ABOVE_SIDE, Method.ILLINOIS);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link Method#PEGASUS} and invalid allowed solution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPegasusInvalidAllowedSolution() {
        BaseSecantSolver.solve(20, TEST_FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE_AND_ABOVE_SIDE, Method.PEGASUS);
    }
}