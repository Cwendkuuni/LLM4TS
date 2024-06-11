package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularityException;
import org.apache.commons.math3.solver.BracketedUnivariateFunction;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.LBFGSB;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.Solver;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1.0, -1.0, 1.0);
        double x0 = 1.0;
        double x1 = 2.0;

        double root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertTrue(Math.abs(root - 1.5) < 1e-6);

        // test with absolute accuracy
        double absAccuracy = 1e-8;
        root = UnivariateRealSolverUtils.solve(f, x0, x1, absAccuracy);
        assertTrue(Math.abs(root - 1.5) < absAccuracy);

        // test with null function
        try {
            UnivariateRealSolverUtils.solve(null, x0, x1);
            fail("FunctionEvaluationException should be thrown.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // test with invalid arguments
        try {
            UnivariateRealSolverUtils.solve(f, Double.NaN, x1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.solve(f, x0, Double.NaN);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.solve(f, x0, x0);
            fail("ConvergenceException should be thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.solve(f, x0, x1, absAccuracy, 0);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1.0, -1.0, 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;

        double[] bracketed = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertArrayEquals(new double[]{1.0, 2.0}, bracketed, 1e-6);

        // test with null function
        try {
            UnivariateRealSolverUtils.bracket(null, initial, lowerBound, upperBound);
            fail("FunctionEvaluationException should be thrown.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // test with invalid arguments
        try {
            UnivariateRealSolverUtils.bracket(f, null, lowerBound, upperBound);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, null, upperBound);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 0);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, -1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, Integer.MAX_VALUE + 1);
            fail("ConvergenceException should be thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }

        // test with function that doesn't bracket a root
        UnivariateRealFunction nonBracketingFunction = new PolynomialFunction(1.0, -2.0, 1.0);
        try {
            UnivariateRealSolverUtils.bracket(nonBracketingFunction, initial, lowerBound, upperBound);
            fail("NoBracketingException should be thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        // test with function that is always positive
        UnivariateRealFunction positiveFunction = new PolynomialFunction(1.0, 1.0, 1.0);
        try {
            UnivariateRealSolverUtils.bracket(positiveFunction, initial, lowerBound, upperBound);
            fail("ConvergenceException should be thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }

        // test with function that is always negative
        UnivariateRealFunction negativeFunction = new PolynomialFunction(1.0, -2.0, 1.0);
        try {
            UnivariateRealSolverUtils.bracket(negativeFunction, initial, lowerBound, upperBound);
            fail("NoBracketingException should be thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    // Test helper class
    public static class PolynomialFunction implements UnivariateRealFunction {
        private final double a;
        private final double b;
        private final double c;

        public PolynomialFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return a * Math.pow(x, 2) + b * x + c;
        }
    }
}