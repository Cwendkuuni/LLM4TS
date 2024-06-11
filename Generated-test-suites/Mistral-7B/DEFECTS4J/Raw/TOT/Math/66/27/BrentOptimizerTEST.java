package org.apache.commons.math.optimization.univariate.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConverter;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() {
        // Test with a simple function.
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new Function("func", x -> Math.pow(x, 2));
        double min = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0).getPoint()[0];
        assertEquals(-2.5, min, 1e-8);

        // Test with a more complex function.
        UnivariateRealFunction g = new Function("func", x -> Math.sin(x) * Math.exp(-x));
        double minG = optimizer.optimize(g, GoalType.MINIMIZE, -10.0, 10.0, 0.0).getPoint()[0];
        assertEquals(-1.1780975471828182, minG, 1e-6);

        // Test with a non-differentiable function.
        UnivariateRealFunction h = new Function("func", x -> Math.abs(Math.sin(Math.PI / 2.0) * Math.pow(x, 2.0)));
        double minH = optimizer.optimize(h, GoalType.MINIMIZE, 0.0, 10.0, 0.0).getPoint()[0];
        assertEquals(1.0, minH, 1e-6);

        // Test with a function that returns infinity.
        UnivariateRealFunction i = new Function("func", x -> {
            if (x > 2.0) {
                throw new ArithmeticException("Function i is undefined for x > 2");
            }
            return Double.POSITIVE_INFINITY;
        });
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(i, GoalType.MINIMIZE, 0.0, 3.0, 0.0));
    }

    @Test
    public void testMaximization() {
        // Test with a simple function.
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new Function("func", x -> Math.pow(x, 2));
        double max = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0).getPoint()[0];
        assertEquals(2.5, max, 1e-8);

        // Test with a more complex function.
        UnivariateRealFunction g = new Function("func", x -> Math.cos(x) * Math.exp(x));
        double maxG = optimizer.optimize(g, GoalType.MAXIMIZE, -10.0, 10.0, 0.0).getPoint()[0];
        assertEquals(3.545853433380331, maxG, 1e-6);

        // Test with a non-differentiable function.
        UnivariateRealFunction h = new Function("func", x -> Math.abs(Math.sin(Math.PI / 2.0) * Math.pow(x, 2.0)));
        double maxH = optimizer.optimize(h, GoalType.MAXIMIZE, 0.0, 10.0, 0.0).getPoint()[0];
        assertEquals(1.0, maxH, 1e-6);

        // Test with a function that returns negative infinity.
        UnivariateRealFunction j = new Function("func", x -> {
            if (x < -2.0) {
                throw new ArithmeticException("Function j is undefined for x < -2");
            }
            return Double.NEGATIVE_INFINITY;
        });
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(j, GoalType.MAXIMIZE, -3.0, 0.0, 0.0));
    }

    @Test
    public void testInvalidInputs() {
        // Test when the function is null.
        UnivariateRealFunction nullFunc = null;
        assertThrows(NullPointerException.class, () -> new BrentOptimizer().optimize(nullFunc, GoalType.MINIMIZE, 0.0, 0.0));

        // Test when the bounds are invalid.
        UnivariateRealFunction k = new Function("func", x -> x * x);
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(k, GoalType.MINIMIZE, -1.0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(k, GoalType.MINIMIZE, 1.0, -1.0));

        // Test when the start value is outside the bounds.
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(k, GoalType.MINIMIZE, 5.0, 1.0, 0.0));

        // Test when the relative and absolute accuracies are negative.
        BrentOptimizer optimizer2 = new BrentOptimizer();
        optimizer2.setRelativeAccuracy(-1.0e-5);
        optimizer2.setAbsoluteAccuracy(-1.0e-8);
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer2.optimize(k, GoalType.MINIMIZE, 1.0, 2.0, 0.0));
    }

    @Test
    public void testMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        UnivariateRealFunction l = new Function("func", x -> Math.pow(x, 2));
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(l, GoalType.MINIMIZE, 0.0, 0.0));
    }

    private static UnivariateRealFunction newFunction(String name, UnivariateRealFunction f) {
        return new Function(name, x -> {
            double value = f.value(x);
            if (Double.isNaN(value)) {
                throw new FunctionEvaluationException("Function evaluation failed: NaN");
            }
            return value;
        });
    }

    private static class Function implements UnivariateRealFunction {
        private String name;
        private UnivariateRealFunction underlying;

        public Function(String name, UnivariateRealFunction underlying) {
            this.name = name;
            this.underlying = underlying;
        }

        @Override
        public double value(double x) {
            return underlying.value(x);
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException("This function does not support derivative evaluation.");
        }

        @Override
        public String toString() {
            return name;
        }
    }
}