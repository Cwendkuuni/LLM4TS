package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        // Test with default bounds and initial guess.
        DoubleFunction f = new DoubleFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        SimpleBounds bounds = new SimpleBounds(-5, 5);
        InitialGuess initialGuess = new InitialGuess(0);
        PointValuePair result = optimizer.optimize(f, bounds, initialGuess, GoalType.MINIMIZE);

        assertEquals(-3.141592653589793, result.getPoint(), 1e-6);

        // Test with custom bounds and no initial guess.
        bounds = new SimpleBounds(0, 10);
        result = optimizer.optimize(f, bounds, null, GoalType.MINIMIZE);

        assertEquals(1.000000000000001, result.getPoint(), 1e-6);

        // Test with custom bounds and initial guess outside the bounds.
        initialGuess = new InitialGuess(15.0);
        result = optimizer.optimize(f, bounds, initialGuess, GoalType.MINIMIZE);

        assertEquals(0.0, result.getPoint(), 1e-6);

        // Test with custom bounds and initial guess inside the bounds.
        initialGuess = new InitialGuess(1.5);
        result = optimizer.optimize(f, bounds, initialGuess, GoalType.MINIMIZE);

        assertEquals(1.000000000000001, result.getPoint(), 1e-6);
    }
}

interface DoubleFunction {
    double value(double x);
}