package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(-1.4137192262999414, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizeWithCustomThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(-1.4137192262999414, result.getPoint(), 1e-8);
    }

    @Test
    public void testBrentOptimizerMinimizeConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair prev, UnivariatePointValuePair current) {
                return iter > 100;
            }
        });
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(-1.4137192262999414, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(2.918280763329952, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeWithCustomThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(2.918280763329952, result.getPoint(), 1e-8);
    }

    @Test
    public void testBrentOptimizerMaximizeConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair prev, UnivariatePointValuePair current) {
                return iter > 100;
            }
        });
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(2.918280763329952, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizeInvalidThreshold() {
        Assert.throwsException(NotStrictlyPositiveException.class, () -> {
            new BrentOptimizer(-1e-6, 1e-6);
        });
    }

    @Test
    public void testBrentOptimizerMinimizeInvalidRelativeThreshold() {
        Assert.throwsException(NumberIsTooSmallException.class, () -> {
            new BrentOptimizer(1e-7, 1e-7);
        });
    }

}