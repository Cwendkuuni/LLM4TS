package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.simple.AbstractUnivariateFunction;
import org.apache.commons.math3.optimization.simple.Cubic;
import org.apache.commons.math3.optimization.simple.Linear;
import org.apache.commons.math3.optimization.simple.Sine;
import org.apache.commons.math3.optimization.simple.ThresholdFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        final double rel = 1e-5;
        final double abs = 1e-7;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);

        // Test with null function and bounds.
        assertNull(optimizer.optimize(null, null, null));

        // Test with invalid bounds.
        assertNull(optimizer.optimize(new ThresholdFunction(0), new double[]{Double.NEGATIVE_INFINITY, 0}, null));
        assertNull(optimizer.optimize(new ThresholdFunction(0), new double[]{0, Double.POSITIVE_INFINITY}, null));

        // Test with no convergence checker.
        AbstractUnivariateFunction function = new Linear(1);
        UnivariatePointValuePair result = optimizer.optimize(function, new double[]{0, 1}, null);
        assertEquals(0, result.getX(), 0);
        assertEquals(0, result.getValue(), 0);

        // Test with a convergence checker.
        ThresholdFunction thresholdFunction = new ThresholdFunction(1e-6);
        optimizer = new BrentOptimizer(rel, abs, thresholdFunction);
        function = new Cubic(-1);
        result = optimizer.optimize(function, new double[]{-1, 2}, null);
        assertEquals(-0.80735396875, result.getX(), 0);
        assertEquals(3.57326324683, result.getValue(), 0);
        assertTrue(thresholdFunction.converged(0, null, result));

        // Test with a convergence checker that never converges.
        ThresholdFunction neverConvergeFunction = new ThresholdFunction(Double.POSITIVE_INFINITY);
        optimizer = new BrentOptimizer(rel, abs, neverConvergeFunction);
        function = new Sine();
        result = optimizer.optimize(function, new double[]{-10, 10}, null);
        assertNull(result);

        // Test with different goal types.
        optimizer = new BrentOptimizer(rel, abs);
        result = optimizer.optimize(new Linear(-1), new double[]{-1, 1}, null);
        assertEquals(0, result.getX(), 0);
        assertEquals(-1, result.getValue(), 0);

        optimizer = new BrentOptimizer(rel, abs, GoalType.MAXIMIZE);
        result = optimizer.optimize(new Linear(1), new double[]{-1, 1}, null);
        assertEquals(1, result.getX(), 0);
        assertEquals(1, result.getValue(), 0);
    }

    @Test
    public void testUnivariateFunction() {
        final double rel = 1e-5;
        final double abs = 1e-7;
        final double minFunctionValue = -E.EULER_NUMBER.doubleValue();

        // Test with a simple linear function.
        UnivariateFunction linearFunction = new Linear(1);
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(linearFunction, new double[]{0, 1}, null);
        assertEquals(0, result.getX(), 0);
        assertEquals(0, result.getValue(), 0);

        // Test with a cubic function.
        UnivariateFunction cubicFunction = new Cubic(-1);
        result = optimizer.optimize(cubicFunction, new double[]{-1, 2}, null);
        assertEquals(-0.80735396875, result.getX(), 0);
        assertEquals(3.57326324683, result.getValue(), 0);

        // Test with a sine function.
        UnivariateFunction sineFunction = new Sine();
        result = optimizer.optimize(sineFunction, new double[]{-10, 10}, null);
        assertEquals(1.27323954473, result.getX(), 0);
        assertEquals(-1.17520119364, result.getValue(), 0);

        // Test with a threshold function.
        UnivariateFunction thresholdFunction = new ThresholdFunction(minFunctionValue);
        optimizer = new BrentOptimizer(rel, abs, GoalType.MINIMIZE, thresholdFunction);
        result = optimizer.optimize(thresholdFunction, new double[]{-1, 1}, null);
        assertEquals(-E.EULER_NUMBER.doubleValue(), result.getValue(), 0);
        assertEquals(Double.NaN, result.getX(), 0);

        // Test with a negative threshold function.
        thresholdFunction = new ThresholdFunction(-minFunctionValue);
        optimizer = new BrentOptimizer(rel, abs, GoalType.MAXIMIZE, thresholdFunction);
        result = optimizer.optimize(thresholdFunction, new double[]{-1, 1}, null);
        assertEquals(E.EULER_NUMBER.doubleValue(), result.getValue(), 0);
        assertEquals(0, result.getX(), 0);
    }

    @Test
    public void testConvergenceChecker() {
        final double rel = 1e-5;
        final double abs = 1e-7;

        ThresholdFunction thresholdFunction = new ThresholdFunction(1e-6);
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, thresholdFunction);

        // Test with a function that converges quickly.
        UnivariateFunction function = new Linear(1);
        UnivariatePointValuePair result = optimizer.optimize(function, new double[]{-1, 1}, null);
        assertEquals(0, result.getX(), 0);
        assertEquals(0, result.getValue(), 0);
        assertTrue(thresholdFunction.converged(0, null, result));

        // Test with a function that converges slowly.
        function = new Sine();
        result = optimizer.optimize(function, new double[]{-10, 10}, null);
        assertEquals(1.27323954473, result.getX(), 0);
        assertEquals(-1.17520119364, result.getValue(), 0);
        assertTrue(thresholdFunction.converged(1000, null, result));

        // Test with a function that doesn't converge.
        UnivariateFunction nonConvergingFunction = new NonConvergingFunction();
        result = optimizer.optimize(nonConvergingFunction, new double[]{0, 1}, null);
        assertNull(result);
        assertFalse(thresholdFunction.converged(1, null, null));
    }

    @Test
    public void testInterpolation() {
        final double rel = 1e-5;
        final double abs = 1e-7;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Test parabolic interpolation.
        UnivariateFunction function = new ParabolicFunction(-1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, function.value(1));
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, function.value(2));
        UnivariatePointValuePair c = optimizer.optimize(function, null, a, b).getPoint();
        assertEquals(-0.25, c.getX(), 0);
        assertEquals(0.25, function.value(c.getX()), 0);

        // Test golden section step.
        function = new GoldenSectionFunction(-1);
        a = new UnivariatePointValuePair(1, function.value(1));
        b = new UnivariatePointValuePair(2, function.value(2));
        c = optimizer.optimize(function, null, a, b).getPoint();
        assertEquals(-1.648721271, c.getX(), 0);
        assertEquals(0.618034095, function.value(c.getX()), 0);
    }

    @Test
    public void testBracketing() {
        final double rel = 1e-5;
        final double abs = 1e-7;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Test bracketing with a function that has two roots.
        UnivariateFunction function = new TwoRootsFunction();
        double[] bounds = new double[]{0, 2};
        UnivariatePointValuePair a = optimizer.optimize(function, bounds, null).getPoint();
        UnivariatePointValuePair b = optimizer.optimize(function, bounds, a).getPoint();
        UnivariatePointValuePair c = optimizer.optimize(function, bounds, b).getPoint();

        assertArrayEquals(new double[]{0, 1, 1}, new double[]{a.getX(), b.getX(), c.getX()});

        // Test bracketing with a function that has no roots.
        function = new NoRootsFunction();
        bounds = new double[]{-1, 1};
        a = optimizer.optimize(function, bounds, null).getPoint();
        b = optimizer.optimize(function, bounds, a).getPoint();
        c = optimizer.optimize(function, bounds, b).getPoint();

        assertArrayEquals(new double[]{-0.5, 0.5, 0}, new double[]{a.getX(), b.getX(), c.getX()});
    }
}

class ParabolicFunction extends AbstractUnivariateFunction {
    private double a;

    public ParabolicFunction(double a) {
        this.a = a;
    }

    @Override
    public double value(double x) {
        return a * (x - 0.25) * (x - 0.25);
    }
}

class GoldenSectionFunction extends AbstractUnivariateFunction {
    private double a;

    public GoldenSectionFunction(double a) {
        this.a = a;
    }

    @Override
    public double value(double x) {
        return a * Math.pow(x - 0.5, 2) + 1;
    }
}

class TwoRootsFunction extends AbstractUnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x - 1, 3) - 1;
    }
}

class NoRootsFunction extends AbstractUnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(Math.abs(x), 0.5) - 0.5;
    }
}

class NonConvergingFunction extends AbstractUnivariateFunction {
    @Override
    public double value(double x) {
        return Math.log(Math.abs(x)) / Math.log(2);
    }
}