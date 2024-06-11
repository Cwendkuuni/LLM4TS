package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_minimize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, new double[]{0, 0, 1});
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -5, 5, 0).getValue();
        assertEquals(0.0, min, 1e-9);
    }

    @Test
    public void testBrentOptimizer_maximize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, new double[]{0, 0, 1});
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -5, 5, 0).getValue();
        assertEquals(1.0, max, 1e-9);
    }

    @Test
    public void testBrentOptimizer_minimize_bounded() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, new double[]{0, 1, 1});
        double min = optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0).getValue();
        assertEquals(1.0 / 3.0, min, 1e-9);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizer_functionEvaluationException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new FunctionWithEvaluationException();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizer_maxIterationsExceeded() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        UnivariateFunction function = new FunctionWithFixedMinimum();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0);
    }

    @Test
    public void testBrentOptimizer_notStrictlyPositiveException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.setRelativeAccuracy(-1.0e-9);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Relative accuracy must be a positive number", e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_notStrictlyPositiveException2() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.setAbsoluteAccuracy(-1.0);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Absolute accuracy must be a positive number", e.getMessage());
        }
    }

    private static class FunctionWithEvaluationException implements UnivariateFunction {
        @Override
        public double value(double x) {
            throw new FunctionEvaluationException("Test evaluation exception");
        }

        @Override
        public double value(double x, double... arguments) {
            throw new FunctionEvaluationException("Test evaluation exception");
        }
    }

    private static class FunctionWithFixedMinimum implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI / 2.0);
        }

        @Override
        public double value(double x, double... arguments) {
            return Math.sin(Math.PI / 2.0);
        }
    }
}