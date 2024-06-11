package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.function.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer()}.
     */
    @Test
    public void testConstructor() {
        new BrentOptimizer();
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * Should throw an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double, double)}.
     * Tests the optimization of a simple quadratic function.
     */
    @Test
    public void testOptimizeQuadraticFunction() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;
        double startValue = 1;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(0, result.getPoint(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     * Tests the optimization of a simple quadratic function with bounds.
     */
    @Test
    public void testOptimizeQuadraticFunctionWithBounds() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1;
        double max = 2;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, new SimpleBounds(min, max));
        assertEquals(1, result.getPoint(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     * Tests the optimization of a function with a local minimum and a global minimum.
     */
    @Test
    public void testOptimizeFunctionWithLocalAndGlobalMinima() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x * (Math.sin(Math.PI * x) - Math.sin(2 * Math.PI * x));
            }

            @Override
            public double derivative(double x) {
                return 2 * x * (Math.cos(Math.PI * x) + 2 * Math.cos(2 * Math.PI * x))
                      - 2 * Math.PI * (Math.cos(Math.PI * x) - 2 * Math.cos(2 * Math.PI * x));
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -2;
        double max = 2;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max);
        assertEquals(-1, result.getPoint(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     * Tests the optimization of a function with a local minimum outside the bounds.
     */
    @Test
    public void testOptimizeFunctionWithLocalMinimumOutsideBounds() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, goalType, min, max));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     * Tests the optimization of a function with a relative accuracy smaller than 2 machine epsilons.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeFunctionWithRelativeAccuracyTooSmall() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1;
        double max = 1;
        double eps = 1e-17;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(eps);
        optimizer.optimize(f, goalType, min, max);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double, double)}.
     * Tests the optimization of a function with an initial point outside the bounds.
     */
    @Test
    public void testOptimizeFunctionWithInitialPointOutsideBounds() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1;
        double max = 1;
        double startValue = 2;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(-1, result.getPoint(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double, double)}.
     * Tests the optimization of a function with an initial point at the boundary.
     */
    @Test
    public void testOptimizeFunctionWithInitialPointAtBoundary() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1;
        double max = 1;
        double startValue = 1;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(-1, result.getPoint(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double, double)}.
     * Tests the optimization of a function with an initial point at the minimum.
     */
    @Test
    public void testOptimizeFunctionWithInitialPointAtMinimum() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1;
        double max = 1;
        double startValue = -1;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(-1, result.getPoint(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double, double)}.
     * Tests the optimization of a function with an initial point at the global minimum.
     */
    @Test
    public void testOptimizeFunctionWithInitialPointAtGlobalMinimum() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1;
        double max = 1;
        double startValue = -2;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(-1, result.getPoint(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double, double)}.
     * Tests the optimization of a function with a maximum number of evaluations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeFunctionWithMaximumNumberOfEvaluations() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1;
        double max = 1;
        double startValue = 0;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(MaxEval.FAST_CONVERGENCE);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, double, double, double)}.
     * Tests the optimization of a function with a non-monotonous behaviour.
     */
    @Test
    public void testOptimizeFunctionWithNonMonotonousBehaviour() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x) * Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.exp(-Math.pow(x, 2)) - 2 * Math.PI * Math.sin(Math.PI * x) * Math.cos(Math.PI * x)
                      - Math.cos(Math.PI * x) * Math.cos(Math.PI * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1;
        double max = 1;
        double startValue = 0;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(-0.741363252346401, result.getPoint(), 1e-5);
    }

}