package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructionWithRelAndAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-6);
        assertEquals(1.0e-5, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1.0e-6, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructionWithNegativeAbs() {
        new BrentOptimizer(1.0e-5, -1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructionWithRelTooSmall() {
        new BrentOptimizer(1.0e-8, 1.0e-6);
    }

    @Test
    public void testBrentOptimizerConstructionWithNoArgs() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(2.0 * FastMath.ulp(1d), optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.0, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerConstructionWithOnlyRel() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5);
        assertEquals(1.0e-5, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.0, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerInstanceVariablesAreSetCorrectly() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-6);
        assertEquals(1.0e-5, optimizer.relativeThreshold, 0.0);
        assertEquals(1.0e-6, optimizer.absoluteThreshold, 0.0);
    }

    @Test
    public void testDefaultTermination() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertTrue(result != null);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithLoHiBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(
                1.0e-5, 1.0e-6, new FooFunction());
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(2.0, result.getPoint(), 0.01);
        assertEquals(-3.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithNoBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(
                1.0e-5, 1.0e-6, new FooFunction(), null);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(1.0, result.getPoint(), 0.01);
        assertEquals(-2.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(
                1.0e-5, 1.0e-6, new FooFunction(), new MyConvergenceChecker());
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(1.5, result.getPoint(), 0.01);
        assertEquals(-2.25, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithLoHiBounds() {
        BrentOptimizer maximizer = new BrentOptimizer(
                1.0e-5, 1.0e-6, new BarFunction(), GoalType.MAXIMIZE);
        UnivariatePointValuePair result = maximizer.doOptimize();
        assertEquals(4.0, result.getPoint(), 0.01);
        assertEquals(3.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithNoBounds() {
        BrentOptimizer maximizer = new BrentOptimizer(
                1.0e-5, 1.0e-6, new BarFunction(), GoalType.MAXIMIZE, null);
        UnivariatePointValuePair result = maximizer.doOptimize();
        assertEquals(3.5, result.getPoint(), 0.01);
        assertEquals(4.5, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithConvergenceChecker() {
        BrentOptimizer maximizer = new BrentOptimizer(
                1.0e-5, 1.0e-6, new BarFunction(), GoalType.MAXIMIZE, new MyConvergenceChecker());
        UnivariatePointValuePair result = maximizer.doOptimize();
        assertEquals(5.5, result.getPoint(), 0.01);
        assertEquals(6.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerWithOutOfBoundsFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(
                1.0e-5, 1.0e-6, new OutOfBoundsFunction(), null);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertFalse(result != null);
    }
}

class FooFunction implements UnivariateFunction {
    public double value(double x) {
        return Math.pow(x, 2) - 3 * x;
    }
}

class BarFunction implements UnivariateFunction {
    public double value(double x) {
        return Math.pow(x, 2) + 3 * x;
    }
}

class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    public boolean converged(int iter, UnivariatePointValuePair previous,
            UnivariatePointValuePair current) {
        return current.getValue() <= previous.getValue() - 0.1;
    }
}

class OutOfBoundsFunction implements UnivariateFunction {
    public double value(double x) {
        throw new IllegalArgumentException("x is out of bounds");
    }
}