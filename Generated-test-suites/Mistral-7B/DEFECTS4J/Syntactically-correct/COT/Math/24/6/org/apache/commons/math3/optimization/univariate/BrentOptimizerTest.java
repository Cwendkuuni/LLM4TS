package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.test.AbstractMathTestCase;

import java.util.ArrayList;
import java.util.List;

public class BrentOptimizerTest extends AbstractMathTestCase {

    @SuppressWarnings("unchecked")
    private static UnivariateFunction f(final double x) {
        return new UnivariateFunction() {
            @Override
            public double value(double arg) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1.5;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
    }

    @org.junit.Test
    public void testBrentOptimizerMinimum() {
        final double rel = 1e-5;
        final double abs = 1e-5;
        final double lo = -3.0;
        final double hi = 3.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(f, lo, hi);
        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        double rel = 1e-5;
        double abs = -1e-5;
        new BrentOptimizer(rel, abs);
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        double rel = 1e-12;
        double abs = 1e-6;
        new BrentOptimizer(rel, abs);
    }

    @org.junit.Test
    public void testBrentOptimizerEdgeCases() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        // Test with a = lo and objective value = 0
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(x -> 0.0, -1.0, 1.0);
        assertEquals(-1.0, result.getPoint(), 1e-8);

        // Test with hi = lo
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, -1.0, -1.0);
        assertNotNull(result);

        // Test with lo < x < a
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, -1.0, 0.0);
        assertArrayEquals(new double[]{0.0}, result.getPoints(), 1e-8);

        // Test with hi > x > b
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, 2.0, 3.0);
        assertArrayEquals(new double[]{2.0}, result.getPoints(), 1e-8);

        // Test with lo = hi
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(x -> Math.exp(x) - 1.0, 0.0, 0.0);
        assertNotNull(result);

        // Test with objective value = 0
        UnivariateFunction constFunction = x -> 0.0;
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(constFunction, -1.0, 1.0);
        assertEquals(-1.0, result.getPoint(), 1e-8);

        // Test with objective value = infinity
        UnivariateFunction infiniteFunction = x -> Double.POSITIVE_INFINITY;
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(infiniteFunction, -1.0, 1.0);
        assertArrayEquals(new double[]{1.0}, result.getPoints(), 1e-8);

        // Test with lo = -infinity
        UnivariateFunction negativeInfiniteFunction = x -> Double.NEGATIVE_INFINITY;
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(negativeInfiniteFunction, -1.0, 1.0);
        assertArrayEquals(new double[]{1.0}, result.getPoints(), 1e-8);
    }

    @org.junit.Test
    public void testBrentOptimizerConvergenceChecker() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        // Test default
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertTrue(result.isOptimum());

        // Test with custom convergence checker
        BrentOptimizer optimizerWithChecker = new BrentOptimizer(rel, abs, (iter, previous, current) -> true);
        result = optimizerWithChecker.optimize(f, -5.0, 5.0);
        assertTrue(result.isOptimum());

        // Test with custom convergence checker that always fails
        BrentOptimizer optimizerWithFailingChecker = new BrentOptimizer(rel, abs, (iter, previous, current) -> false);
        try {
            optimizerWithFailingChecker.optimize(f, -5.0, 5.0);
            fail("Expected ConvergenceException not thrown");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testBrentOptimizerDifferentGoalTypes() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        BrentOptimizer optimizerMinim = new BrentOptimizer(rel, abs);
        BrentOptimizer optimizerMaxim = new BrentOptimizer(rel, abs, GoalType.MAXIMIZE);

        UnivariateFunction fMinim = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1.5;
        UnivariateFunction fMaxim = x -> -(Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1.5);

        UnivariatePointValuePair resultMinim = optimizerMinim.optimize(fMinim, -1.0, 1.0);
        UnivariatePointValuePair resultMaxim = optimizerMaxim.optimize(fMaxim, -1.0, 1.0);

        assertEquals(0.0, resultMinim.getPoint(), 1e-8);
        assertEquals(0.0, resultMaxim.getPoint(), 1e-8);
        assertEquals(-0.0, resultMinim.getValue(), 1e-8);
        assertEquals(0.0, resultMaxim.getValue(), 1e-8);
    }

    @org.junit.Test
    public void testBrentOptimizerDifferentSearchIntervals() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        UnivariateFunction f = this::cosineFunction;
        BrentOptimizer optimizerMinim = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair resultMinim1 = optimizerMinim.optimize(f, -1.0, 0.0);
        UnivariatePointValuePair resultMinim2 = optimizerMinim.optimize(f, 0.0, 1.0);

        assertEquals(-1.0, resultMinim1.getPoint(), 1e-8);
        assertEquals(1.0, resultMinim2.getPoint(), 1e-8);

        BrentOptimizer optimizerMaxim = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair resultMaxim1 = optimizerMaxim.optimize(f, -1.0, 0.0);
        UnivariatePointValuePair resultMaxim2 = optimizerMaxim.optimize(f, 0.0, 1.0);

        assertEquals(1.0, resultMaxim1.getPoint(), 1e-8);
        assertEquals(-1.0, resultMaxim2.getPoint(), 1e-8);
    }

    @org.junit.Test
    public void testBrentOptimizerBrentsCriterion() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        UnivariateFunction f = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1.5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);

        // Test with x < m
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, -5.0, -1.0);
        assertEquals(-1.0, result.getPoint(), 1e-8);

        // Test with x > m
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, 1.0, 5.0);
        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @org.junit.Test
    public void testBrentOptimizerParabolicInterpolationStep() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        UnivariateFunction f = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1.5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);

        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, -5.0, 1.0);

        assertEquals(-1.0, result.getPoint(), 1e-8);

        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, 1.0, 5.0);

        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @org.junit.Test
    public void testBrentOptimizerGoldenSectionStep() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        UnivariateFunction f = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1.5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);

        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, -5.0, 0.0);

        assertEquals(-3.0, result.getPoint(), 1e-8);

        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(f, 0.0, 5.0);

        assertEquals(1.0, result.getPoint(), 1e-8);
    }

    @org.junit.Test
    public void testBrentOptimizerMultipleCalls() {
        final double rel = 1e-5;
        final double abs = 1e-5;

        UnivariateFunction f = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1.5;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        List<UnivariatePointValuePair> results = new ArrayList<>();
        results.add(optimizer.optimize(f, -5.0, 5.0));
        results.add(optimizer.optimize(f, -5.0, 5.0));
        results.add(optimizer.optimize(f, -5.0, 5.0));

        for (UnivariatePointValuePair result : results) {
            assertEquals(0.0, result.getPoint(), 1e-8);
        }
    }

    private UnivariatePointValuePair best(UnivariatePointValuePair a,
                                          UnivariatePointValuePair b,
                                          boolean isMinim) {
        return BrentOptimizer.best(a, b, isMinim);
    }

    private double cosineFunction(double x) {
        return Math.cos(x);
    }
}