package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(x -> x * x, 0.0);
        assertEquals(-1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(x -> x * x, 0.0);
        assertEquals(-1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(x -> x * x, 0.0);
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.abs(x), 0.0);
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerRelThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.0);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.abs(x), 0.0);
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsThreshold() {
        new BrentOptimizer(0.0, -1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelThreshold() {
        new BrentOptimizer(0.0, 1e-6);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5,
                (iter, previous, current) -> Math.abs(current.getValue() - previous.getValue()) < 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(x -> x * x, 0.0);
        assertEquals(-1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerConvergenceCheckerMultipleIterations() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5,
                (iter, previous, current) -> Math.abs(current.getValue() - previous.getValue()) < 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.abs(x), 0.0);
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 1e-5);
    }
}