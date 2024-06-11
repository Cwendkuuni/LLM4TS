package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test with a simple quadratic function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        OptimizationResult result = optimizer.optimize(f, goalType, -3.0, 3.0, 0.0);
        assertEquals(0.0, result.getPointValue(), 1e-5);
        assertEquals(0.0, result.getPoint(), 1e-5);

        // Test with a more complex function
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }
        };
        OptimizationResult result2 = optimizer.optimize(f2, goalType, -10.0, 10.0, 0.0);
        assertEquals(-1.000000136057833, result2.getPointValue(), 1e-5);
        assertEquals(-1.000000136057833, result2.getPoint(), 1e-5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws MaxIterationsExceededException {
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 * x);
            }
        };
        optimizer.setMaximalIterationCount(0);
        optimizer.optimize(f, goalType, -1.0, 1.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException {
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.optimize(f, goalType, -1.0, 1.0, 0.0);
    }

    @Test
    public void testGoalTypeMaximize() {
        GoalType goalType = GoalType.MAXIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test with a simple quadratic function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        OptimizationResult result = optimizer.optimize(f, goalType, -3.0, 3.0, 0.0);
        assertEquals(9.0, result.getPointValue(), 1e-5);
        assertEquals(3.0, result.getPoint(), 1e-5);

        // Test with a more complex function
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }
        };
        OptimizationResult result2 = optimizer.optimize(f2, goalType, -10.0, 10.0, 0.0);
        assertEquals(1.000000136057831, result2.getPointValue(), 1e-5);
        assertEquals(1.000000136057831, result2.getPoint(), 1e-5);
    }
}