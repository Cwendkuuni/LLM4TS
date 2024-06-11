package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.simple.SimpleValueException;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations(), 0);
        assertEquals(100, optimizer.getMaximalIterationCount(), 0);
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(null, GoalType.MINIMIZE, 0, 1));
    }

    @Test
    public void testNullGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new SimpleUnivariateFunction(x -> x), null));
    }

    @Test
    public void testMinimizeFunctionWithinInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> x * x - 1);

        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max, x).getValue();
        assertEquals(0.0, result, 1e-5);
    }

    @Test
    public void testMinimizeFunctionWithinIntervalWithInitialValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> x * x - 1);

        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max, x).getValue();
        assertEquals(0.0, result, 1e-5);
    }

    @Test
    public void testMaximizeFunctionWithinInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> -x * x + 1);

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, min, max, x).getValue();
        assertEquals(1.0, result, 1e-5);
    }

    @Test
    public void testMinimizeFunctionWithinIntervalWithGoalTypeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> x * x - 1);

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, min, max, x).getValue();
        assertEquals(-1.0, result, 1e-5);
    }

    @Test
    public void testMinimizeFunctionWithinIntervalWithInitialValueAndGoalTypeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> x * x - 1);

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, min, max, x).getValue();
        assertEquals(-1.0, result, 1e-5);
    }

    @Test
    public void testMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);

        double x = 1.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> Math.sin(x));

        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, min, max, x));
    }

    @Test
    public void testFunctionEvaluationException() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0 / 0.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> 1 / x);

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, min, max, x));
    }

    @Test
    public void testNotStrictlyPositiveAccuracy() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer().setRelativeAccuracy(-1.0));
    }

    @Test
    public void testNotStrictlyPositiveMaxEvaluations() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer().setMaxEvaluations(-1));
    }

    @Test
    public void testNotStrictlyPositiveMaxIterationCount() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer().setMaximalIterationCount(-1));
    }

    @Test
    public void testNotStrictlyPositiveMin() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = -1.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> Math.abs(x));

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, min, max, x));
    }

    @Test
    public void testNotStrictlyPositiveMax() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = 0.0;
        double max = -1.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> Math.abs(x));

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, min, max, x));
    }

    @Test
    public void testMinimizeFunctionWithinIntervalWithDifferentGoalTypes() {
        BrentOptimizer optimizer = new BrentOptimizer();

        double x = 1.0;
        double min = 0.0;
        double max = 2.0;

        UnivariateRealFunction f = new SimpleUnivariateFunction(x -> x * x - 1);

        PointValuePair result1 = optimizer.optimize(f, GoalType.MINIMIZE, min, max, x);
        PointValuePair result2 = optimizer.optimize(f, GoalType.MAXIMIZE, min, max, x);

        assertArrayEquals(new double[]{0.0, 0.0}, result1.getPoint(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, result2.getPoint(), 0.001);
    }

    static class SimpleUnivariateFunction implements UnivariateRealFunction {
        private final DoubleFunction function;

        public SimpleUnivariateFunction(DoubleFunction function) {
            this.function = function;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return function.applyAsDouble(x);
        }

        @FunctionalInterface
        interface DoubleFunction {
            double applyAsDouble(double x);
        }
    }
}