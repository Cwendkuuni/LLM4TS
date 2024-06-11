package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {
    /** A function to minimize. */
    private static final UnivariateRealFunction FUNCTION_MIN = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * (Math.sin(x) * Math.cos(x) - Math.cos(x) * Math.sin(x));
        }
    };

    /** A function to maximize. */
    private static final UnivariateRealFunction FUNCTION_MAX = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * (Math.sin(x) * Math.cos(x) - Math.cos(x) * Math.sin(x));
        }
    };

    /** A random data generator to use for testing. */
    private RandomDataGenerator random;

    /** The optimizer to test. */
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    /** The base optimizer used by the multi-start optimizer. */
    private FibonacciSearchRealOptimizer baseOptimizer;

    /** The number of starts in the multi-start optimization. */
    private static final int NUM_STARTS = 3;

    /** The number of evaluations for each start. */
    private static final int MAX_EVALS_PER_START = 20;

    /** The total number of evaluations allowed. */
    private static final int MAX_TOTAL_EVALS = NUM_STARTS * MAX_EVALS_PER_START;

    /** The convergence threshold. */
    private static final double CONVERGENCE_THRESHOLD = 1e-6;

    /** The goal type for optimization. */
    private static final GoalType GOAL_TYPE = GoalType.MINIMIZE;

    /** The bounds for optimization. */
    private static final SimpleBounds BOUNDS = new SimpleBounds(-5.0, 5.0);

    @Before
    public void setUp() {
        random = new RandomDataGenerator();
        baseOptimizer = new FibonacciSearchRealOptimizer(GOAL_TYPE, BOUNDS);
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_TOTAL_EVALS);
    }

    /**
     * Test minimization with a given function.
     */
    @Test
    public void testMinimize() {
        try {
            PointValuePair result = optimizer.optimize(FUNCTION_MIN, GOAL_TYPE, BOUNDS.getMin(), BOUNDS.getMax());
            double expected = -1.2764;
            Assert.assertEquals(expected, result.getPoint(), 1e-9);
            Assert.assertEquals(expected, result.getValue(), 1e-9);
        } catch (FunctionEvaluationException e) {
            Assert.fail(e.getMessage());
        }

        // Check optima
        PointValuePair[] allOptima = optimizer.getOptima();
        Assert.assertArrayEquals(NUM_STARTS, allOptima.length);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                double v1 = o1.getValue();
                double v2 = o2.getValue();
                return Double.compare(v1, v2);
            }
        };
        Arrays.sort(allOptima, comparator);
        Assert.assertEquals(expected, allOptima[0].getValue(), 1e-9);
    }

    /**
     * Test maximization with a given function.
     */
    @Test
    public void testMaximize() {
        try {
            PointValuePair result = optimizer.optimize(FUNCTION_MAX, GOAL_TYPE.MAXIMIZE, BOUNDS.getMin(), BOUNDS.getMax());
            double expected = 1.2764;
            Assert.assertEquals(expected, result.getPoint(), 1e-9);
            Assert.assertEquals(expected, result.getValue(), 1e-9);
        } catch (FunctionEvaluationException e) {
            Assert.fail(e.getMessage());
        }

        // Check optima
        PointValuePair[] allOptima = optimizer.getOptima();
        Assert.assertArrayEquals(NUM_STARTS, allOptima.length);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                double v1 = o1.getValue();
                double v2 = o2.getValue();
                return Double.compare(v2, v1);
            }
        };
        Arrays.sort(allOptima, comparator);
        Assert.assertEquals(expected, allOptima[0].getValue(), 1e-9);
    }

    /**
     * Test that optimization fails when given bounds are invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBounds() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_TOTAL_EVALS);
        optimizer.optimize(FUNCTION_MIN, GOAL_TYPE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Test that optimization fails when given function is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_TOTAL_EVALS);
        optimizer.optimize(null, GOAL_TYPE, BOUNDS.getMin(), BOUNDS.getMax());
    }

    /**
     * Test that optimization fails when given goal type is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_TOTAL_EVALS);
        optimizer.optimize(FUNCTION_MIN, null, BOUNDS.getMin(), BOUNDS.getMax());
    }

    /**
     * Test that optimization fails when given initial guess is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInitialGuess() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_TOTAL_EVALS);
        optimizer.optimize(FUNCTION_MIN, GOAL_TYPE, null, BOUNDS.getMin(), BOUNDS.getMax());
    }
}