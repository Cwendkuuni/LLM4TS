package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerInstance() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerThrowsExceptionWhenMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> Math.pow(Math.sin(x), 2));
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0));
    }

    @Test
    public void testBrentOptimizerThrowsExceptionWhenFunctionEvaluationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> {
            throw new FunctionEvaluationException("FunctionEvaluationException");
        });
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0));
    }

    @Test
    public void testBrentOptimizerThrowsExceptionWhenNotStrictlyPositive() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaxEvaluations(-1));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaximalIterationCount(-1));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(-1.0));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setRelativeAccuracy(-1.0));
    }

    @Test
    public void testBrentOptimizerFindsMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> Math.pow(x, 2));
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(0.0, min, 0.001);
    }

    @Test
    public void testBrentOptimizerFindsMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> -Math.pow(x, 2));
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0);
        assertEquals(25.0, max, 0.001);
    }

    @Test
    public void testBrentOptimizerHandlesSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> Math.pow(x, 2) + Math.sin(x));
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(-1.1752, min, 0.001);
    }

    @Test
    public void testBrentOptimizerHandlesFunctionWithMultipleLocalMinima() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> Math.pow(Math.sin(x), 2) - Math.cos(x));
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(-1.2732, min, 0.001);
    }

    @Test
    public void testBrentOptimizerHandlesFunctionWithMultipleLocalMaxima() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> Math.pow(Math.cos(x), 2) - Math.sin(x));
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0);
        assertEquals(1.0, max, 0.001);
    }

    @Test
    public void testBrentOptimizerHandlesFunctionWithMultipleLocalMinimaAndMaxima() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> Math.pow(Math.sin(x), 2) - Math.cos(x) + Math.exp(-(Math.pow(x, 2) + 1)));
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(-2.4050, min, 0.001);
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0);
        assertEquals(0.2289, max, 0.001);
    }

    @Test
    public void testBrentOptimizerHandlesFunctionWithTwoLocalMinimaAndOneLocalMaxima() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = (x -> Math.pow(Math.sin(x), 2) * Math.exp(-x) + Math.sin(x) * Math.cos(x) - Math.pow(Math.cos(x), 2));
        double[] min = new double[]{-1.135, 0.785};
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0);
        assertEquals(0.785, max, 0.001);
        double[] results = optimizer.getResults();
        assertArrayEquals(new double[]{min[0], function.value(min[0]), -Math.pow(Math.sin(min[0]), 2) * Math.exp(min[0]) + Math.sin(min[0]) * Math.cos(min[0]) - Math.pow(Math.cos(min[0]), 2), 0}, results);
        assertFalse(optimizer.isOptimumFound());
        min = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(-1.135, min, 0.001);
        results = optimizer.getResults();
        assertArrayEquals(new double[]{min[0], function.value(min[0]), Math.pow(Math.sin(min[0]), 2) * Math.exp(min[0]) + Math.sin(min[0]) * Math.cos(min[0]) - Math.pow(Math.cos(min[0]), 2), 1}, results);
        assertTrue(optimizer.isOptimumFound());
    }

    @Test
    public void testBrentOptimizerHandlesFunctionWithMultipleLocalMinimaAndMaximaAndPrecisionExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        UnivariateRealFunction function = (x -> Math.pow(Math.sin(x), 2) * Math.exp(-x) + Math.sin(x) * Math.cos(x) - Math.pow(Math.cos(x), 2));
        assertThrows(PrecisionExceededException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0));
    }
}