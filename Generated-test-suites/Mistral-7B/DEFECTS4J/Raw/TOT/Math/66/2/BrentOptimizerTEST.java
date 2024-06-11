package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test minimization with a simple function.
     */
    @Test
    public void testMinimizationSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double startValue = 1.0;
        double expectedMinimum = 0.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, startValue);

        assertEquals(expectedMinimum, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test minimization with a function having multiple local minima.
     */
    @Test
    public void testMinimizationFunctionMultipleLocalMinima() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * (x + 1) * (x - 1) * (x - 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.MIN_VALUE;
        double startValue = 1.0;
        double expectedMinimum = -2.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, startValue);

        assertEquals(expectedMinimum, result.getPoint(), 1e-8);
        assertEquals(Double.MIN_VALUE, result.getValue(), 1e-8);
    }

    /**
     * Test maximization with a simple function.
     */
    @Test
    public void testMaximizationSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double startValue = 1.0;
        double expectedMaximum = 3.678794421372357;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, startValue);

        assertEquals(expectedMaximum, result.getPoint(), 1e-8);
        assertEquals(expectedMaximum, result.getValue(), 1e-8);
    }

    /**
     * Test maximization with a function having multiple local maxima.
     */
    @Test
    public void testMaximizationFunctionMultipleLocalMaxima() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -1.0;
        double startValue = 0.0;
        double expectedMaximum = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, startValue);

        assertEquals(expectedMaximum, result.getPoint(), 1e-8);
        assertEquals(expectedMaximum, result.getValue(), 1e-8);
    }

    /**
     * Test exception when the function is not defined in the interval.
     */
    @Test
    public void testExceptionFunctionNotDefined() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(FunctionEvaluationException.class, () -> {
            optimizer.optimize(f, goalType, min, max, startValue);
        });
    }

    /**
     * Test exception when the accuracy is not positive.
     */
    @Test
    public void testExceptionAccuracyNotPositive() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double eps = -1.0e-8;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> {
            optimizer.optimize(f, goalType, min, max, startValue, eps);
        });
    }

    /**
     * Test exception when the tolerance is too small.
     */
    @Test
    public void testExceptionToleranceTooSmall() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-10 * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double eps = 1e-20;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(NumberIsTooSmallException.class, () -> {
            optimizer.optimize(f, goalType, min, max, startValue, eps);
        });
    }

    /**
     * Test exception when the maximum number of evaluations is exceeded.
     */
    @Test
    public void testExceptionMaxEval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        int maxEval = 1;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(maxEval);
        assertThrows(MaxIterationsExceededException.class, () -> {
            optimizer.optimize(f, goalType, min, max, startValue);
        });
    }

    /**
     * Test exception when the function is not strictly positive.
     */
    @Test
    public void testExceptionFunctionNotStrictlyPositive() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) * Math.log(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> {
            optimizer.optimize(f, goalType, min, max, startValue);
        });
    }
}