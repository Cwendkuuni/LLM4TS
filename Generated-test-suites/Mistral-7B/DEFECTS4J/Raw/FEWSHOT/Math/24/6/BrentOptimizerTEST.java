package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.GoalType;

import java.util.Arrays;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        new BrentOptimizer(-1e-5, 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-5, -1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerZeroAbsoluteThreshold() {
        new BrentOptimizer(1e-5, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerSmallRelativeThreshold() {
        new BrentOptimizer(1e-9, 1e-5);
    }

    @Test
    public void testBrentOptimizerNullConvergenceChecker() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new DummyConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerWithEmptyFunction() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new DummyUnivariateFunction());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerWithFunctionThatAlwaysReturnsZero() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ZeroFunction());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertEquals(Double.NaN, result.getPoint(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithFunctionThatReturnsMonotonicDecreasingValues() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new DecreasingFunction());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertArrayEquals(new double[]{2.141592653589793, 0.2360287471352669}, result.getPointAndValue(), 0.00001);
    }

    @Test
    public void testBrentOptimizerWithFunctionThatReturnsMonotonicIncreasingValues() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new IncreasingFunction());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertArrayEquals(new double[]{0.1234567890123456, 1.2345678901234568}, result.getPointAndValue(), 0.00001);
    }

    @Test
    public void testBrentOptimizerWithFunctionThatHasMultipleLocalMinima() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new MultipleMinimaFunction());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertArrayEquals(new double[]{4.56, 0.987654321}, result.getPointAndValue(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNonUnivariateFunction() {
        new BrentOptimizer(1e-5, 1e-5, new DummyMultiVariateFunction());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullBounds() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(null);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("bounds must not be null"));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithEmptyBounds() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(new SimpleBounds());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("lower bound must be less than upper bound"));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithBoundsThatIntersect() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(new SimpleBounds(5, 5));
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("lower bound must be less than upper bound"));
            throw e;
        }
    }

    @Test
    public void testBrentOptimizerMaxEval() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new MaxEvalFunction(), new MaxEvalInitialGuess());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertEquals(10, result.getPoint(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullInitialGuess() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(new SimpleBounds(0, 10), null);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("initial guess must not be null"));
            throw e;
        }
    }

    @Test
    public void testBrentOptimizerWithCustomInitialGuess() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10), new InitialGuess() {
            @Override
            public double value(double lowerBound, double upperBound) {
                return 5.0;
            }
        });
        assertArrayEquals(new double[]{5.0, 25.0}, result.getPointAndValue(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNegativeGoalType() {
        new BrentOptimizer(1e-5, 1e-5, new GoalType(-1) {});
    }

    @Test
    public void testBrentOptimizerWithMinimizeGoalType() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new MinimizeFunction());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertEquals(-1.23, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithMaximizeGoalType() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new MaximizeFunction());
        PointValuePair result = optimizer.optimize(new SimpleBounds(0, 10));
        assertEquals(1.23, result.getValue(), 0.0001);
    }

    private static class DummyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return true;
        }
    }

    private static class DummyUnivariateFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return 0.0;
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    private static class ZeroFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return 0.0;
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    private static class DecreasingFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return FastMath.pow(x, 2) - 0.5;
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    private static class IncreasingFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return FastMath.pow(x, 2) + 0.5;
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    private static class MultipleMinimaFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < 4) {
                return FastMath.pow(x, 2) + 0.1 * Math.sin(4 * Math.PI * x);
            } else {
                return FastMath.pow(x - 4.5, 2) - 0.1;
            }
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    private static class MaxEvalFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.max(x, 10.0);
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    private static class MaxEvalInitialGuess implements InitialGuess {
        @Override
        public double value(double lowerBound, double upperBound) {
            return 5.0;
        }
    }
}

class InitialGuess {
    public abstract double value(double lowerBound, double upperBound);
}