package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 0);
        assertEquals(1e-5, optimizer.getAbsoluteThreshold(), 0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1.0, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        new BrentOptimizer(0.5, 1.0, null);
    }

    @Test
    public void testBrentOptimizerZeroRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1.0, null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, optimizer.getRelativeThreshold(), 0);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), 0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker
                = (iter, previous, current) -> iter > 10;

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, convergenceChecker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 0);
        assertEquals(1e-5, optimizer.getAbsoluteThreshold(), 0);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        UnivariatePointValuePair optimum = optimizer.doOptimize();
        assertNull(optimum);
    }

    @Test
    public void testDoOptimizeMinimizeFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        Function testFunction = new FunctionWithMinimum(0.5);
        optimizer.setFunction(testFunction);
        UnivariatePointValuePair optimum = optimizer.doOptimize();
        assertEquals(0.5, optimum.getX(), 1e-6);
        assertEquals(-0.25, optimum.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeMinimizeFunctionBounded() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        Function testFunction = new FunctionWithMinimum(0.5, 1.0, -1.0);
        optimizer.setFunction(testFunction);
        UnivariatePointValuePair optimum = optimizer.doOptimize();
        assertEquals(0.5, optimum.getX(), 1e-6);
        assertEquals(-0.25, optimum.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeMinimizeFunctionThrowsException() {
        Function testFunction = new FunctionWithException();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setFunction(testFunction);
        try {
            optimizer.doOptimize();
            fail();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimizeMaximizeFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        Function testFunction = new FunctionWithMaximum(0.5);
        optimizer.setFunction(testFunction);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair optimum = optimizer.doOptimize();
        assertEquals(0.5, optimum.getX(), 1e-6);
        assertEquals(0.25, optimum.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeMaximizeFunctionBounded() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        Function testFunction = new FunctionWithMaximum(0.5, 1.0, -1.0);
        optimizer.setFunction(testFunction);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair optimum = optimizer.doOptimize();
        assertEquals(0.5, optimum.getX(), 1e-6);
        assertEquals(0.25, optimum.getValue(), 1e-6);
    }

    private interface Function {
        double value(double x);
    }

    private static class FunctionWithMinimum implements Function {
        private final double minimum;

        public FunctionWithMinimum(double minimum) {
            this.minimum = minimum;
        }

        @Override
        public double value(double x) {
            return Math.pow(x - minimum, 2);
        }
    }

    private static class FunctionWithMaximum implements Function {
        private final double maximum;
        private final double lowerBound;
        private final double upperBound;

        public FunctionWithMaximum(double maximum) {
            this(maximum, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        public FunctionWithMaximum(double maximum, double lowerBound, double upperBound) {
            this.maximum = maximum;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public double value(double x) {
            if (x < lowerBound) {
                throw new IllegalArgumentException("x is less than lower bound.");
            }
            if (x > upperBound) {
                throw new IllegalArgumentException("x is greater than upper bound.");
            }
            return Math.pow(x - maximum, 2);
        }
    }

    private static class FunctionWithException implements Function {
        @Override
        public double value(double x) {
            throw new IllegalArgumentException("Exception in function value.");
        }
    }
}