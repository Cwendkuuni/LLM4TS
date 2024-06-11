package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optimization.GoalType.MINIMIZE;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new SquareFunction();
        double min = optimizer.optimize(function, -10, 10).getValue();
        Assert.assertEquals(0, min, 1e-7);
    }

    @Test
    public void testBrentOptimizerAbsTol() {
        BrentOptimizer optimizer = new BrentOptimizer(0, 1e-6);
        UnivariateFunction function = new AbsFunction();
        double min = optimizer.optimize(function, -10, 10).getValue();
        Assert.assertEquals(1, min, 1e-7);
    }

    @Test
    public void testBrentOptimizerRelTol() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0);
        UnivariateFunction function = new SinFunction();
        double min = optimizer.optimize(function, -10, 10).getValue();
        Assert.assertEquals(0, Math.PI / 2, min, 1e-6);
    }

    @Test
    public void testBrentOptimizerAbsAndRelTol() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariateFunction function = new AbsFunctionWithSquareRoot();
        double min = optimizer.optimize(function, -10, 10).getValue();
        Assert.assertEquals(1, min, 1e-6);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        UnivariateFunction function = new SquareFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, -10, 10);
        Assert.assertEquals(0, result.getValue(), 1e-7);
        Assert.assertFalse(optimizer.isConverged());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsTol() {
        new BrentOptimizer(-1, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelTol() {
        new BrentOptimizer(1e-7, 1e-6);
    }

    @Test
    public void testBrentOptimizerGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new SquareFunction();
        optimizer.setGoalType(GoalType.MAXIMIZE);
        double max = optimizer.optimize(function, -10, 10).getValue();
        Assert.assertEquals(0, max, 1e-7);
    }

    private static class SquareFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }
    }

    private static class AbsFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.abs(x);
        }
    }

    private static class SinFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    }

    private static class AbsFunctionWithSquareRoot implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.abs(x) + Math.sqrt(Math.abs(x));
        }
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return iteration > 10;
        }
    }
}