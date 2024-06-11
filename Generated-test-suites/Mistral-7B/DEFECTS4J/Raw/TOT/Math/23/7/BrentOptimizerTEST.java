package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.GoalTypeMismatchException;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketedRealValuedFunction;
import org.apache.commons.math3.optimization.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optimization.initializers.RealVectorValue;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * A simple function to test {@link BrentOptimizer}.
     */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 2, 2);
        }

        @Override
        public double derivative(double x) {
            return 4 * (x - 2);
        }
    }

    /**
     * A bracketed real-valued function for testing {@link BrentOptimizer}.
     */
    private static class TestBracketedFunction implements BracketedRealValuedFunction {
        private final TestFunction function;

        public TestBracketedFunction() {
            function = new TestFunction();
        }

        @Override
        public double lowerBound() {
            return 0;
        }

        @Override
        public double upperBound() {
            return 4;
        }

        @Override
        public double value(double x) {
            return function.value(x);
        }

        @Override
        public double value(double x, double[] gradient) {
            function.value(x);
            if (gradient != null) {
                gradient[0] = function.derivative(x);
            }
            return function.value(x);
        }
    }

    private BrentOptimizer optimizer;
    private TestBracketedFunction testFunction;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
        testFunction = new TestBracketedFunction();
    }

    @After
    public void tearDown() {
        optimizer = null;
        testFunction = null;
    }

    /**
     * Test that {@link BrentOptimizer} finds the minimum of a simple function.
     */
    @Test
    public void testSimpleFunctionMinimum() {
        optimizer.optimize(testFunction);
        PointValuePair result = optimizer.getPoint();
        Assert.assertArrayEquals(new double[]{2.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test that {@link BrentOptimizer} fails to find the minimum of a simple function with a wrong goal type.
     */
    @Test(expected = GoalTypeMismatchException.class)
    public void testSimpleFunctionMaximum() {
        optimizer = new BrentOptimizer(testFunction, GoalType.MAXIMIZE);
        optimizer.optimize(testFunction);
    }

    /**
     * Test that {@link BrentOptimizer} converges to the minimum of a simple function.
     */
    @Test
    public void testSimpleFunctionConvergence() {
        optimizer.setMaxIterations(100);
        optimizer.optimize(testFunction);
        PointValuePair result = optimizer.getPoint();
        Assert.assertArrayEquals(new double[]{2.0}, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test that {@link BrentOptimizer} fails to converge to the minimum of a simple function with a very small tolerance.
     */
    @Test(expected = ConvergenceException.class)
    public void testSimpleFunctionNoConvergence() {
        optimizer = new BrentOptimizer(1e-12, 1e-12);
        optimizer.optimize(testFunction);
    }

    /**
     * Test that {@link BrentOptimizer} finds the minimum of a function with an initial guess.
     */
    @Test
    public void testSimpleFunctionWithInitialGuess() {
        optimizer = new BrentOptimizer(1e-6, 1e-6, new ArrayRealVectorInitializer(new double[]{3.0}));
        optimizer.optimize(testFunction);
        PointValuePair result = optimizer.getPoint();
        Assert.assertArrayEquals(new double[]{2.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test that {@link BrentOptimizer} finds the minimum of a non-convex function.
     */
    @Test
    public void testNonConvexFunctionMinimum() {
        optimizer.optimize(new NonConvexFunction());
        PointValuePair result = optimizer.getPoint();
        Assert.assertArrayEquals(new double[]{-1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(-3.0, result.getValue(), 0.0);
    }

    /**
     * A non-convex function for testing {@link BrentOptimizer}.
     */
    private static class NonConvexFunction implements BracketedRealValuedFunction {
        @Override
        public double lowerBound() {
            return -3;
        }

        @Override
        public double upperBound() {
            return 1;
        }

        @Override
        public double value(double x) {
            if (x < 0) {
                return Math.pow(x, 3) - 3 * x * x + 2;
            } else {
                return -Math.pow(x - 1, 3) - 3 * (x - 1) * (x - 1) - 2;
            }
        }

        @Override
        public double value(double x, double[] gradient) {
            double result = value(x);
            if (gradient != null) {
                if (x < 0) {
                    gradient[0] = 3 * x * x * 2 - 6 * x;
                } else {
                    gradient[0] = -3 * (x - 1) * (x - 1) * 2 - 6 * (x - 1);
                }
            }
            return result;
        }
    }
}