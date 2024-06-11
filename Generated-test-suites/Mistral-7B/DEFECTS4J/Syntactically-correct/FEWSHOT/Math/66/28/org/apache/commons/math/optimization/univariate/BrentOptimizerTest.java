package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testDoOptimizeThrowUnsupportedOperationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(UnsupportedOperationException.class, optimizer::doOptimize);
    }

    @Test
    public void testOptimizeWithNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testOptimizeWithInvalidGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new FunctionWithInvalidGoalType(), GoalType.MAXIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testOptimizeWithInvalidBounds() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithInvalidBounds();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 0.0, 1.0));
    }

    @Test
    public void testOptimizeWithInvalidStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithValidBounds();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, -1.0));
    }

    @Test
    public void testOptimizeWithFunctionEvaluationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionThatThrowsException();
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testOptimizeWithMaxIterationsExceededException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithMaxIterationsExceeded();
        optimizer.setMaximalIterationCount(5);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithNotStrictlyPositiveBounds();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, -1.0, 1.0, 0.5));
    }

    @Test
    public void testOptimizeWithRelativeAccuracyBiggerThanMachineEpsilon() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithValidBounds();
        optimizer.setRelativeAccuracy(1.1e-9);
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.5, result, 1.1e-9);
    }

    @Test
    public void testOptimizeWithAbsoluteAccuracyBiggerThanMachineEpsilon() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithValidBounds();
        optimizer.setAbsoluteAccuracy(1.1e-7);
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.5, result, 1.1e-7);
    }

    @Test
    public void testOptimizeWithMaximalIterationCount() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithValidBounds();
        optimizer.setMaximalIterationCount(5);
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.5, result, 1E-9);
    }

    @Test
    public void testMinimizeFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithValidBounds();
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.5, result, 1E-9);
    }

    @Test
    public void testMaximizeFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithValidBounds();
        testFunction = new FunctionWithMaximization(testFunction);
        double result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, 0.0, 1.0, 0.5);
        assertEquals(1.0, result, 1E-9);
    }

    @Test
    public void testMinimizeFunctionWithCustomBounds() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithCustomBounds();
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, new SimpleBounds(-10.0, 10.0), 0.0, null);
        assertEquals(-1.5, result, 1E-9);
    }

    @Test
    public void testMaximizeFunctionWithCustomBounds() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new FunctionWithCustomBounds();
        testFunction = new FunctionWithMaximization(testFunction);
        double result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, new SimpleBounds(-10.0, 10.0), 0.0, null);
        assertEquals(7.25, result, 1E-9);
    }

    private static class FunctionWithInvalidBounds implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new IllegalArgumentException();
        }

        @Override
        public double derive(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] domain() {
            return new double[]{Double.NaN, Double.NaN};
        }
    }

    private static class FunctionWithInvalidGoalType implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new IllegalArgumentException("Invalid GoalType");
        }

        @Override
        public double derive(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] domain() {
            return new double[]{0.0, 1.0};
        }
    }

    private static class FunctionThatThrowsException implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException();
        }

        @Override
        public double derive(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] domain() {
            return new double[]{0.0, 1.0};
        }
    }

    private static class FunctionWithMaxIterationsExceeded implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new MaxIterationsExceededException("Max iterations exceeded", 1);
        }

        @Override
        public double derive(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] domain() {
            return new double[]{0.0, 1.0};
        }
    }

    private static class FunctionWithNotStrictlyPositiveBounds implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new NotStrictlyPositiveException("Non-positive bounds");
        }

        @Override
        public double derive(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] domain() {
            return new double[]{-1.0, 1.0};
        }
    }

    private static class FunctionWithCustomBounds implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1.5;
        }

        @Override
        public double derive(double x) {
            return 2 * x;
        }

        @Override
        public double[] domain() {
            return new double[]{-10.0, 10.0};
        }
    }

    private static class FunctionWithMaximization implements UnivariateRealFunction {

        private final UnivariateRealFunction function;

        public FunctionWithMaximization(UnivariateRealFunction function) {
            this.function = function;
        }

        @Override
        public double value(double x) {
            return -this.function.value(x);
        }

        @Override
        public double derive(double x) {
            return -this.function.derive(x);
        }

        @Override
        public double[] domain() {
            return this.function.domain();
        }
    }
}