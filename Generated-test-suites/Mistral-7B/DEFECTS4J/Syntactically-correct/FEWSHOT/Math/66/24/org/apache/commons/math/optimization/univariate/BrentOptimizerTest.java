package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertTrue(optimizer instanceof BrentOptimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeThrowsUnsupportedOperationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeMin() {
        UnivariateRealFunction function = new Function("sin(x)", x -> Math.sin(x));
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 0);
        assertEquals(-1, min, 1e-9);
    }

    @Test
    public void testOptimizeMinWithInitialValue() {
        UnivariateRealFunction function = new Function("sin(x)", x -> Math.sin(x));
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, Math.PI / 2);
        assertEquals(-1, min, 1e-9);
    }

    @Test
    public void testOptimizeMinWithInitialInterval() {
        UnivariateRealFunction function = new Function("sin(x)", x -> Math.sin(x));
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -5, 5);
        assertEquals(-1, min, 1e-9);
    }

    @Test
    public void testOptimizeMax() {
        UnivariateRealFunction function = new Function("abs(sin(x)", x -> Math.abs(Math.sin(x)));
        BrentOptimizer optimizer = new BrentOptimizer();
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, 0);
        assertEquals(1, max, 1e-9);
    }

    @Test
    public void testOptimizeMaxWithInitialValue() {
        UnivariateRealFunction function = new Function("abs(sin(x)", x -> Math.abs(Math.sin(x)));
        BrentOptimizer optimizer = new BrentOptimizer();
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, Math.PI / 2);
        assertEquals(1, max, 1e-9);
    }

    @Test
    public void testOptimizeMaxWithInitialInterval() {
        UnivariateRealFunction function = new Function("abs(sin(x)", x -> Math.abs(Math.sin(x)));
        BrentOptimizer optimizer = new BrentOptimizer();
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -5, 5);
        assertEquals(1, max, 1e-9);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction function = new Function("x^2 / 0", x -> x * x / 0);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        UnivariateRealFunction function = new Function("x^2", x -> x * x);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0);
    }

    private static UnivariateRealFunction newFunction(String name, double... values) {
        return x -> {
            for (int i = 0; i < values.length; ++i) {
                if (Math.abs(x - values[i]) < 1e-8) {
                    return values[i + 1];
                }
            }
            throw new FunctionEvaluationException("function not defined for x = " + x);
        };
    }

    private static UnivariateRealFunction newFunction(String name, double a, double b, double... values) {
        return x -> {
            for (int i = 0; i < values.length; ++i) {
                if (Math.abs(x - (a + i * (b - a) / (values.length - 1))) < 1e-8) {
                    return values[i];
                }
            }
            throw new FunctionEvaluationException("function not defined for x = " + x);
        };
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;
        private final UnivariateRealFunction function;

        public Function(String name, UnivariateRealFunction function) {
            this.name = name;
            this.function = function;
        }

        public Function(String name, double... values) {
            this.name = name;
            this.function = newFunction(name, values);
        }

        public Function(String name, double a, double b, double... values) {
            this.name = name;
            this.function = newFunction(name, a, b, values);
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return function.value(x);
        }

        @Override
        public double derivative(double x) throws FunctionEvaluationException {
            throw new UnsupportedOperationException("derivative not implemented");
        }

        @Override
        public String toString() {
            return name;
        }
    }
}