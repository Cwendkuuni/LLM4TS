package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnSupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.DoubleField;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test solve method with RegulaFalsiSolver.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRegulaFalsiSolver() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        double root = 0.0;

        root = solver.solve(f, 1.0, 2.0);
        assertEquals(root, 1.0, 1.0e-6);

        root = solver.solve(f, -1.0, 0.0);
        assertEquals(root, -1.0, 1.0e-6);

        root = solver.solve(f, 0.0, 2.0);
        assertEquals(root, 1.0, 1.0e-6);

        root = solver.solve(f, -2.0, 0.0);
        assertEquals(root, -1.0, 1.0e-6);

        root = solver.solve(f, 0.0, 1.0);
        assertEquals(root, 1.0, 1.0e-6);

        try {
            solver.solve(f, 0.0, 0.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 1.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }

        try {
            solver.solve(null, 1.0, 2.0);
            fail("Expected UnSupportedOperationException");
        } catch (UnSupportedOperationException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.BELOW_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Test solve method with IllinoisSolver.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIllinoisSolver() throws Exception {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        double root = 0.0;

        root = solver.solve(f, 1.0, 2.0);
        assertEquals(root, 1.0, 1.0e-6);

        root = solver.solve(f, -1.0, 0.0);
        assertEquals(root, -1.0, 1.0e-6);

        root = solver.solve(f, 0.0, 2.0);
        assertEquals(root, 1.0, 1.0e-6);

        root = solver.solve(f, -2.0, 0.0);
        assertEquals(root, -1.0, 1.0e-6);

        root = solver.solve(f, 0.0, 1.0);
        assertEquals(root, 1.0, 1.0e-6);

        try {
            solver.solve(f, 0.0, 0.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 1.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.BELOW_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Test solve method with PegasusSolver.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testPegasusSolver() throws Exception {
        BaseSecantSolver solver = new PegasusSolver(1.0e-6);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        double root = 0.0;

        root = solver.solve(f, 1.0, 2.0);
        assertEquals(root, 1.0, 1.0e-6);

        root = solver.solve(f, -1.0, 0.0);
        assertEquals(root, -1.0, 1.0e-6);

        root = solver.solve(f, 0.0, 2.0);
        assertEquals(root, 1.0, 1.0e-6);

        root = solver.solve(f, -2.0, 0.0);
        assertEquals(root, -1.0, 1.0e-6);

        root = solver.solve(f, 0.0, 1.0);
        assertEquals(root, 1.0, 1.0e-6);

        try {
            solver.solve(f, 0.0, 0.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 1.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.BELOW_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Test solve method with CustomFunction.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomFunction() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        UnivariateFunction f = new CustomFunction();
        double root = 0.0;

        root = solver.solve(f, -1.0, 1.0);
        assertEquals(root, 0.276483157, 1.0e-6);

        root = solver.solve(f, 1.0, 2.0);
        assertEquals(root, 1.628382882, 1.0e-6);

        try {
            solver.solve(f, 0.0, 0.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 1.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(f, 1.0, 2.0, AllowedSolution.BELOW_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Custom function for testing.
     */
    private static class CustomFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        public double value(double x) {
            return Math.pow(x, 3) - 2.0 * Math.pow(x, 2) + x;
        }

        /**
         * {@inheritDoc}
         */
        public double derivative(double x) {
            return 3.0 * Math.pow(x, 2) - 4.0 * x + 1.0;
        }

        /**
         * {@inheritDoc}
         */
        public boolean isDerivativeDefined(double x) {
            return true;
        }
    }
}