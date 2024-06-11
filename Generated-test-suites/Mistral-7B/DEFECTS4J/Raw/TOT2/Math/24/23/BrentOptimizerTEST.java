package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.ScalarFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.util.Precision;

/**
 * Test for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function f(x) = x^2.
     */
    static class SquareFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }

        @Override
        public double derivative2(double x) {
            return 2;
        }
    }

    /**
     * Test function f(x) = exp(x) - x.
     */
    static class ExpMinusXFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.exp(x) - x;
        }

        @Override
        public double derivative(double x) {
            return Math.exp(x) + 1;
        }

        @Override
        public double derivative2(double x) {
            return Math.exp(x);
        }
    }

    /**
     * Test function f(x) = sin(x) / x.
     */
    static class SinOverXFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) / x;
        }

        @Override
        public double derivative(double x) {
            return (Math.cos(x) / x - Math.sin(x) / x.square()) / x;
        }

        @Override
        public double derivative2(double x) {
            return ((-Math.cos(x) / x.square() + 2 * Math.sin(x) / x.square()) / x
                    + ((Math.cos(x) / x.square() + 2 * Math.sin(x) / x.square()) / x)) / x;
        }
    }

    /**
     * Test that {@code BrentOptimizer} finds the minimum of a quadratic function.
     */
    @org.junit.Test
    public void testQuadraticFunction() {
        SquareFunction squareFunction = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(new SquareFunction(),
                new SimpleBounds(0.5, 1.5));
        assertEquals(0.25, result.getPoint(), 1e-6);
    }

    /**
     * Test that {@code BrentOptimizer} finds the minimum of a function with
     * positive and negative values.
     */
    @org.junit.Test
    public void testExpMinusXFunction() {
        ExpMinusXFunction expMinusXFunction = new ExpMinusXFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(expMinusXFunction,
                new SimpleBounds(-2, 3));
        assertEquals(-1.163861, result.getPoint(), 1e-6);
    }

    /**
     * Test that {@code BrentOptimizer} finds the minimum of a function with
     * singularity.
     */
    @org.junit.Test
    public void testSinOverXFunction() {
        SinOverXFunction sinOverXFunction = new SinOverXFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        UnivariatePointValuePair result = optimizer.optimize(sinOverXFunction,
                new SimpleBounds(0.0001, 1));
        assertEquals(0.6366197723675814, result.getPoint(), 1e-6);
    }

    /**
     * Test that {@code BrentOptimizer} does not converge if function values
     * are not monotonically decreasing.
     */
    @org.junit.Test(expected = AssertionError.class)
    public void testNonMonotonicFunction() {
        ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return x * x;
                } else {
                    return -x * x;
                }
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double derivative2(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, new SimpleBounds(-1, 1));
    }

    /**
     * Test that {@code BrentOptimizer} does not converge if function values
     * are not defined in the bounds.
     */
    @org.junit.Test(expected = AssertionError.class)
    public void testFunctionNotDefinedInBounds() {
        ExpMinusXFunction expMinusXFunction = new ExpMinusXFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(expMinusXFunction, new SimpleBounds(-3, 1));
    }

    /**
     * Test that BrentOptimizer converges to the minimum when given a function
     * with very small gradient.
     */
    @org.junit.Test
    public void testSmallGradient() {
        SquareFunction squareFunction = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        UnivariatePointValuePair result = optimizer.optimize(squareFunction,
                new SimpleBounds(0.99, 1.01));
        assertEquals(1.0, result.getPoint(), 1e-11);
    }

    /**
     * Test that BrentOptimizer can find the minimum of a function with a
     * very large gradient.
     */
    @org.junit.Test
    public void testLargeGradient() {
        ExpMinusXFunction expMinusXFunction = new ExpMinusXFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(expMinusXFunction,
                new SimpleBounds(-5, 5));
        assertEquals(-1.713682, result.getPoint(), 1e-6);
    }

    /**
     * Test that BrentOptimizer can find the minimum of a function with a
     * very large absolute value.
     */
    @org.junit.Test
    public void testLargeAbsoluteValue() {
        SquareFunction squareFunction = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e12);
        UnivariatePointValuePair result = optimizer.optimize(squareFunction,
                new SimpleBounds(-100, 100));
        assertEquals(0.0, result.getPoint(), 1e12);
    }

    /**
     * Test that BrentOptimizer throws an exception if the relative threshold
     * is too small.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-6);
    }

    /**
     * Test that BrentOptimizer throws an exception if the absolute threshold
     * is less than or equal to zero.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

    /**
     * Test that BrentOptimizer correctly handles the case when the minimum
     * is at the boundary.
     */
    @org.junit.Test
    public void testMinimumAtBoundary() {
        SquareFunction squareFunction = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(squareFunction,
                new SimpleBounds(1, 1));
        assertEquals(1.0, result.getPoint(), 1e-6);
    }

    /**
     * Test that BrentOptimizer correctly handles the case when the minimum
     * is at the upper boundary.
     */
    @org.junit.Test
    public void testMinimumAtUpperBoundary() {
        SquareFunction squareFunction = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(squareFunction,
                new SimpleBounds(1, 10));
        assertEquals(1.0, result.getPoint(), 1e-6);
    }

    /**
     * Test that BrentOptimizer correctly handles the case when the minimum
     * is at the lower boundary.
     */
    @org.junit.Test
    public void testMinimumAtLowerBoundary() {
        SquareFunction squareFunction = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(squareFunction,
                new SimpleBounds(-10, 1));
        assertEquals(-3.0, result.getPoint(), 1e-6);
    }

    /**
     * Test that BrentOptimizer correctly handles the case when the minimum
     * is at the boundary and the function value is zero.
     */
    @org.junit.Test
    public void testMinimumAtZeroValueBoundary() {
        SquareFunction squareFunction = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(squareFunction,
                new SimpleBounds(0, 1));
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test that BrentOptimizer correctly handles the case when the minimum
     * is not a root of the function.
     */
    @org.junit.Test
    public void testNonRootMinimum() {
        ExpMinusXFunction expMinusXFunction = new ExpMinusXFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(expMinusXFunction,
                new SimpleBounds(-5, 5));
        assertEquals(-1.713682, result.getPoint(), 1e-6);
    }

    /**
     * Test that BrentOptimizer correctly handles the case when the minimum
     * is not a root of the function and the function value is zero.
     */
    @org.junit.Test
    public void testNonRootMinimumWithZeroFunctionValue() {
        ExpMinusXFunction expMinusXFunction = new ExpMinusXFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(expMinusXFunction,
                new SimpleBounds(-5, -5));
        assertEquals(-2.718281828459045, result.getPoint(), 1e-6);
    }
}