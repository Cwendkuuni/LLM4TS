package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInitializationWithNegativeAccuracy() {
        new BrentOptimizer().setAbsoluteAccuracy(-1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInitializationWithNegativeMaxIterationCount() {
        new BrentOptimizer().setMaxIterations(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInitializationWithNegativeMaxEvaluations() {
        new BrentOptimizer().setMaxEvaluations(-1);
    }

    @Test
    public void testBrentOptimizerInitialization() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 1E-14);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 1E-14);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizerOptimizeWithoutFunction() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test
    public void testBrentOptimizerWithFunction() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x;
            }
        };

        double result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.5);
        assertEquals(-2.0, result, 1E-8);
    }

    @Test
    public void testBrentOptimizerWithMaximumGoalType() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x;
            }
        };

        double result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 0.5);
        assertEquals(2.0, result, 1E-8);
    }

    @Test
    public void testBrentOptimizerWithMinimumAndMaximumBoundaries() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x;
            }
        };

        double result = optimizer.optimize(function, GoalType.MINIMIZE, 1.0, 5.0, 0.5);
        assertEquals(-1.25, result, 1E-8);

        result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 1.0, 0.5);
        assertEquals(1.25, result, 1E-8);
    }

    @Test
    public void testBrentOptimizerWithStartValueInMinimumInterval() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x;
            }
        };

        double result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.5);
        assertEquals(-2.0, result, 1E-8);

        optimizer.setStartValue(1.0);
        result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
        assertEquals(-1.25, result, 1E-8);
    }

    @Test
    public void testBrentOptimizerWithStartValueInMaximumInterval() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x;
            }
        };

        double result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 5.0);
        assertEquals(2.0, result, 1E-8);

        optimizer.setStartValue(4.0);
        result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 4.0);
        assertEquals(1.25, result, 1E-8);
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsMaxIterationsExceededException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsNumberIsTooSmallException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(0);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }
}