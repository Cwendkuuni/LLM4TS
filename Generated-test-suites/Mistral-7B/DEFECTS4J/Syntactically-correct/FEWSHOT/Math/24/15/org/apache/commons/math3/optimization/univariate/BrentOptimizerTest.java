package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.special.MathConstants;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        SimpleBounds bounds = new SimpleBounds(-5, 5);
        PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);

        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(4.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerAbsTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(0, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) * Math.sin(Math.PI * x / 2.0) * Math.cos(Math.PI * x / 2.0);
            }
        };

        SimpleBounds bounds = new SimpleBounds(-MathConstants.PI, MathConstants.PI);
        PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);

        assertEquals(-1.1071067811865475, result.getPoint(), 1e-5);
        assertEquals(-2.352838225237315, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerRelTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        SimpleBounds bounds = new SimpleBounds(-5, 5);
        PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);

        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(4.0, result.getValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidRelTolerance() {
        new BrentOptimizer(0.0001, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidAbsTolerance() {
        new BrentOptimizer(1e-5, -0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        new BrentOptimizer(1e-5, 0, null);
    }
}