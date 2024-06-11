package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMultistart;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.special.Erf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Function to optimize. */
    private static UnivariateRealFunction function;

    /** Optimizer to use for testing. */
    private UnivariateRealOptimizer optimizer;

    /** Random generator. */
    private RandomDataGenerator random;

    /**
     * Set up test.
     */
    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            public double value(double x) {
                return Erf.erf(x);
            }
        };
        optimizer = new MultiStartUnivariateRealOptimizer<>(new FibonacciMinimizer(), 10, new RandomDataGenerator());
    }

    /**
     * Tear down test.
     */
    @After
    public void tearDown() {
        optimizer = null;
        function = null;
    }

    /**
     * Test optimize with valid arguments.
     */
    @Test
    public void testOptimizeValid() throws Exception {
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -5.0;
        final double max = 5.0;
        final PointValuePair result = optimizer.optimize(function, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getValue() <= 0.0);
    }

    /**
     * Test optimize with invalid arguments: null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullFunction() throws Exception {
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 0.0);
    }

    /**
     * Test optimize with invalid arguments: invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidGoalType() throws Exception {
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 0.0);
    }

    /**
     * Test optimize with invalid arguments: invalid bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidBounds() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
    }

    /**
     * Test optimize with convergence checker.
     */
    @Test
    public void testOptimizeConvergence() throws Exception {
        final GoalType goal = GoalTypeMultistart.MINIMIZE;
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean test(PointValuePair pointValuePair) {
                if (pointValuePair == null) {
                    return true;
                }
                return Math.abs(pointValuePair.getValue()) <= 1e-6;
            }
        });
        final PointValuePair result = optimizer.optimize(function, goal, -5.0, 5.0);
        Assert.assertNotNull(result);
        Assert.assertTrue(Math.abs(result.getValue()) <= 1e-6);
    }

    /**
     * Test optimize with maximum evaluations.
     */
    @Test
    public void testOptimizeMaxEvaluations() throws Exception {
        final int maxEvaluations = 10;
        optimizer = new MultiStartUnivariateRealOptimizer<>(new FibonacciMinimizer(), 10, new RandomDataGenerator()) {
            @Override
            public int getMaxEvaluations() {
                return maxEvaluations;
            }
        };
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -5.0;
        final double max = 5.0;
        optimizer.setMaxEvaluations(maxEvaluations);
        final PointValuePair result = optimizer.optimize(function, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(maxEvaluations, optimizer.getEvaluations());
    }

    /**
     * Test getOptima.
     */
    @Test
    public void testGetOptima() throws Exception {
        final int starts = 5;
        optimizer = new MultiStartUnivariateRealOptimizer<>(new FibonacciMinimizer(), starts, new RandomDataGenerator());
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -5.0;
        final double max = 5.0;
        optimizer.optimize(function, goal, min, max);
        final PointValuePair[] optima = optimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(starts, optima.length);
    }

    /**
     * Test getOptima with no optima.
     */
    @Test
    public void testGetOptimaNoOptima() throws Exception {
        final int starts = 5;
        optimizer = new MultiStartUnivariateRealOptimizer<>(new FibonacciMinimizer(), starts, new RandomDataGenerator()) {
            @Override
            public BaseUnivariateRealOptimizer<UnivariateRealFunction> getDelegate() {
                throw new ConvergenceException(LocalizedFormats.NO_CONVERGENCE, null);
            }
        };
        final PointValuePair[] optima = optimizer.getOptima();
        Assert.assertNull(optima);
    }

    /**
     * Test optimize with goal type equal to max.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeMax() throws Exception {
        optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0);
    }
}