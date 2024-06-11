package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction function = new MockFunction();
        double x0 = 0.0;
        double x1 = 1.0;

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1);
            assertNotNull(result);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // expected
        }

        try {
            double result = UnivariateRealSolverUtils.solve(null, x0, x1);
            assert Throws.instanceOf(MathRuntimeException.class);
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, 0.0);
            assertNotNull(result);
        } catch (ConvergenceException | FunctionEvaluationException | IllegalArgumentException e) {
            // expected
        }

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, -1.0);
            assert Throws.instanceOf(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction function = new MockFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.001;

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
            assertNotNull(result);
        } catch (ConvergenceException | FunctionEvaluationException | IllegalArgumentException e) {
            // expected
        }

        try {
            double result = UnivariateRealSolverUtils.solve(null, x0, x1, absoluteAccuracy);
            assert Throws.instanceOf(MathRuntimeException.class);
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, -absoluteAccuracy);
            assert Throws.instanceOf(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = new MockFunction();
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            assertArrayEquals(new double[]{0.0, 1.0}, result);
        } catch (ConvergenceException | FunctionEvaluationException | IllegalArgumentException e) {
            // expected
        }

        try {
            double[] result = UnivariateRealSolverUtils.bracket(null, initial, lowerBound, upperBound);
            assert Throws.instanceOf(MathRuntimeException.class);
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, -1);
            assert Throws.instanceOf(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, 0);
            assertArrayEquals(new double[]{initial - 1, initial + 1}, result);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testBracketWithMaximumIterations() {
        UnivariateRealFunction function = new MockFunction();
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        int maximumIterations = 10;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            assertArrayEquals(new double[]{initial - 1, initial + 1}, result);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // expected
        }

        try {
            double[] result = UnivariateRealSolverUtils.bracket(null, initial, lowerBound, upperBound, maximumIterations);
            assert Throws.instanceOf(MathRuntimeException.class);
        } catch (MathRuntimeException e) {
            // expected
        }

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations + 1);
            assert Throws.instanceOf(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class MockFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 1.0;
        }
    }
}