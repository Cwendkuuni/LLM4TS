package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsThresholds() {
        double relThreshold = 1.0;
        double absThreshold = 0.0;
        new BrentOptimizer(relThreshold, absThreshold);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelThresholds() {
        double relThreshold = 0.0;
        double absThreshold = 1.0;
        new BrentOptimizer(relThreshold, absThreshold);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, null);
        optimizer.optimize(null);
    }

    @Test
    public void testOptimizeSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        assertNotNull(result);
        assertEquals(-3.14159265359, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeFunctionWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        double initialGuess = 1.2;

        UnivariatePointValuePair result = optimizer.optimize(function, initialGuess, 10.0);
        assertNotNull(result);
        assertEquals(initialGuess - 0.12763636963, result.getPoint(), 1e-6);
        assertEquals(Math.pow(Math.sin(result.getPoint()), 2), result.getValue(), 1e-6);
    }

    @Test
    public void testConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-8, new MaxEval(10));
        UnivariateFunction function = f -> Math.abs(Math.pow(Math.sin(f), 2) - 0.5);

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        assertNotNull(result);
        assertEquals(-0.57735026919, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeFunctionWithNotStrictlyMonotonicFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-8);
        UnivariateFunction function = f -> Math.pow(Math.abs(Math.sin(f)), 2);

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        assertNotNull(result);
        assertEquals(-1.0, result.getPoint(), 1e-8);
        assertEquals(1.0, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeFunctionWithGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-8);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0, GoalType.MAXIMIZE);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-8);
        assertEquals(1.0, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeFunctionWithRandomInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-8);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);

        double min = -10.0;
        double max = 10.0;

        double initialGuess = ThreadLocalRandom.current().nextDouble(min, max);
        UnivariatePointValuePair result = optimizer.optimize(function, initialGuess, max);

        assertNotNull(result);
        assertEquals(initialGuess - 0.12763636963, result.getPoint(), 1e-8);
        assertEquals(Math.pow(Math.sin(result.getPoint()), 2), result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeFunctionWithRandomFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-8);
        UnivariateFunction function = f -> ThreadLocalRandom.current().nextDouble() * Math.abs(f);

        double min = -10.0;
        double max = 10.0;

        UnivariatePointValuePair result = optimizer.optimize(function, min, max);
        assertNotNull(result);

        // Result will vary because of the random function.
    }
}