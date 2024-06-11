package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException, NotStrictlyPositiveException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double x0 = 0.0;
        double x1 = 1.0;

        // Test default solver with no absolute accuracy
        double root = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertTrue(Math.abs(root) < 0.2);

        // Test default solver with absolute accuracy
        root = UnivariateRealSolverUtils.solve(function, x0, x1, 0.001);
        assertTrue(Math.abs(root - Math.PI / 2) < 0.01);

        // Test solver with specific solver
        UnivariateRealSolver brentSolver = UnivariateRealSolverFactory.newBrentSolver();
        root = UnivariateRealSolverUtils.solve(function, x0, x1, brentSolver);
        assertTrue(Math.abs(root - Math.PI / 2) < 0.01);
    }

    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException, NotStrictlyPositiveException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        double[] bracketedRoots = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        double a = bracketedRoots[0];
        double b = bracketedRoots[1];
        double root = UnivariateRealSolverUtils.solve(function, a, b);

        assertTrue(Math.abs(root - Math.PI / 2) < 0.01);
    }

    @Test
    public void testBracket_ConvergenceException() throws FunctionEvaluationException, ConvergenceException, NotStrictlyPositiveException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.25;
            }
        };
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, 0);
            assertNull(true);
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testBracket_FunctionEvaluationException() throws FunctionEvaluationException, NotStrictlyPositiveException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Failed to evaluate function", x);
            }
        };
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            assertNull(true);
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testBracket_IllegalArgumentException() throws NotStrictlyPositiveException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            assertNull(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBracket_MaxIterationsExceededException() throws FunctionEvaluationException, NotStrictlyPositiveException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double initial = 1.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        int maximumIterations = 1;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            assertNull(true);
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}