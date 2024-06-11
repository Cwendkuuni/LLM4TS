package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.special.E;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMin() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(x - 1, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-7);
        SimpleBounds bounds = new SimpleBounds(-5, 5);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMax() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - E.E;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-7);
        SimpleBounds bounds = new SimpleBounds(0, 2);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MAXIMIZE);
        assertEquals(1.718281828459045, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinCustomChecker() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(x - 1, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-7, new CustomConvergenceChecker());
        SimpleBounds bounds = new SimpleBounds(-5, 5);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaxCustomChecker() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - E.E;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-7, new CustomConvergenceChecker());
        SimpleBounds bounds = new SimpleBounds(0, 2);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MAXIMIZE);
        assertEquals(1.718281828459045, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-7);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelThresholdTooSmall() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-7);
    }

    static class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return current.getValue() > previous.getValue() * 1.1;
        }
    }
}