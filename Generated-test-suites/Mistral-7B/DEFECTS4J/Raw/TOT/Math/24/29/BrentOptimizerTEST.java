package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * A simple test function.
     */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.exp(-5 * x);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    };

    /**
     * Test BrentOptimizer with a simple test function.
     */
    @Test
    public void testBrentOptimizer() {
        final double relativerTolerance = 1e-4;
        final double absolueTolerance = 1e-4;
        final BrentOptimizer optimizer = new BrentOptimizer(relativerTolerance, absolueTolerance);

        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new SimpleBounds(0, 5));
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertTrue(result != null);
        assertTrue(Math.abs(result.getPoint() - 1.27137967116482) < absolueTolerance);
        assertTrue(Math.abs(TEST_FUNCTION.value(result.getPoint()) - 0.0) < relativerTolerance);
    }

    /**
     * Test BrentOptimizer with a function with multiple minima.
     */
    @Test
    public void testBrentOptimizerMultipleMinima() {
        final double relativerTolerance = 1e-5;
        final double absolueTolerance = 1e-5;
        final BrentOptimizer optimizer = new BrentOptimizer(relativerTolerance, absolueTolerance);

        // Test with a function with multiple minima.
        final UnivariateFunction TEST_FUNCTION_MULTIPLE_MINIMA = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(x - 1, 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        PointValuePair result = optimizer.optimize(TEST_FUNCTION_MULTIPLE_MINIMA, new SimpleBounds(-5, 5));
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertTrue(result != null);
        assertTrue(Math.abs(result.getPoint() - 1) < absolueTolerance);
        assertTrue(Math.abs(TEST_FUNCTION_MULTIPLE_MINIMA.value(result.getPoint()) - 0.01) < relativerTolerance);
    }

    /**
     * Test BrentOptimizer with a function that is not monotonous.
     */
    @Test(expected = IllegalStateException.class)
    public void testBrentOptimizerNotMonotonous() {
        final double relativerTolerance = 1e-4;
        final double absolueTolerance = 1e-4;
        final BrentOptimizer optimizer = new BrentOptimizer(relativerTolerance, absolueTolerance);

        // Test with a function that is not monotonous.
        final UnivariateFunction TEST_FUNCTION_NOT_MONOTONOUS = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        optimizer.optimize(TEST_FUNCTION_NOT_MONOTONOUS, new SimpleBounds(-5, 5));
    }

    /**
     * Test BrentOptimizer with a function which returns NaN.
     */
    @Test(expected = ArithmeticException.class)
    public void testBrentOptimizerNaN() {
        final double relativerTolerance = 1e-4;
        final double absolueTolerance = 1e-4;
        final BrentOptimizer optimizer = new BrentOptimizer(relativerTolerance, absolueTolerance);

        // Test with a function which returns NaN.
        final UnivariateFunction TEST_FUNCTION_NAN = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("NaN");
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        optimizer.optimize(TEST_FUNCTION_NAN, new SimpleBounds(-5, 5));
    }

    /**
     * Test BrentOptimizer with a function with an infinite value.
     */
    @Test(expected = ArithmeticException.class)
    public void testBrentOptimizerInfinity() {
        final double relativerTolerance = 1e-4;
        final double absolueTolerance = 1e-4;
        final BrentOptimizer optimizer = new BrentOptimizer(relativerTolerance, absolueTolerance);

        // Test with a function with an infinite value.
        final UnivariateFunction TEST_FUNCTION_INFINITY = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1 / x;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        optimizer.optimize(TEST_FUNCTION_INFINITY, new SimpleBounds(-5, 0));
    }

    /**
     * Test BrentOptimizer with a function that is not defined for negative values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeValues() {
        final double relativerTolerance = 1e-4;
        final double absolueTolerance = 1e-4;
        final BrentOptimizer optimizer = new BrentOptimizer(relativerTolerance, absolueTolerance);

        // Test with a function that is not defined for negative values.
        final UnivariateFunction TEST_FUNCTION_NEGATIVE_VALUES = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new IllegalArgumentException("x must be non-negative");
                }
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        optimizer.optimize(TEST_FUNCTION_NEGATIVE_VALUES, new SimpleBounds(-5, 5));
    }
}