package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.UniformRealDistribution;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {
    /**
     * Test function for optimization.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI * x) * Math.exp(-x * x);
        }

        @Override
        public double derivative(double x) {
            return value(x) * (1.0 - 2.0 * x);
        }
    }

    /**
     * Test method for optimization with different number of starts.
     */
    @Test
    public void testOptimization() {
        final TestFunction testFunction = new TestFunction();
        final UnivariateOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());

        // Optimize using the given multi-start optimizer.
        try {
            final PointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getPoint());
            Assert.assertEquals(0.0, testFunction.value(result.getPoint()), 1e-6);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        // Check all the optima found during the last call to optimize.
        final PointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertTrue(optima.length > 0);
        for (PointValuePair optimum : optima) {
            Assert.assertNotNull(optimum.getPoint());
            Assert.assertEquals(0.0, testFunction.value(optimum.getPoint()), 1e-6);
        }
    }

    /**
     * Test method for optimization with negative number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfStarts() {
        final TestFunction testFunction = new TestFunction();
        final UnivariateOptimizer optimizer = new BrentOptimizer();
        new MultiStartUnivariateRealOptimizer<>(optimizer, -1, new RandomDataGenerator());
    }

    /**
     * Test method for optimization with zero number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroNumberOfStarts() {
        final TestFunction testFunction = new TestFunction();
        final UnivariateOptimizer optimizer = new BrentOptimizer();
        new MultiStartUnivariateRealOptimizer<>(optimizer, 0, new RandomDataGenerator());
    }

    /**
     * Test method for optimization with null optimizer.
     */
    @Test(expected = NullPointerException.class)
    public void testNullOptimizer() {
        new MultiStartUnivariateRealOptimizer<>(null, 5, new RandomDataGenerator());
    }

    /**
     * Test method for optimization with null random generator.
     */
    @Test(expected = NullPointerException.class)
    public void testNullRandomGenerator() {
        final UnivariateOptimizer optimizer = new BrentOptimizer();
        new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
    }

    /**
     * Test method for optimization with out-of-bounds starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOutOfBoundsStarts() {
        final TestFunction testFunction = new TestFunction();
        final UnivariateOptimizer optimizer = new BrentOptimizer();
        new MultiStartUnivariateRealOptimizer<>(optimizer, 0, new RandomDataGenerator());
        new MultiStartUnivariateRealOptimizer<>(optimizer, Long.MAX_VALUE, new RandomDataGenerator());
    }

    /**
     * Test method for optimization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        final TestFunction testFunction = new TestFunction();
        final UnivariateOptimizer optimizer = new BrentOptimizer();
        new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator()) {
            @Override
            public GoalType getGoalType() {
                return null;
            }
        };
    }

    /**
     * Test method for optimization with function without derivative.
     */
    @Test
    public void testNoDerivative() {
        final TestFunction testFunction = new TestFunction() {
            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException();
            }
        };
        final UnivariateOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());

        try {
            multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test method for optimization with null function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        new MultiStartUnivariateRealOptimizer<>(null, 5, new RandomDataGenerator());
    }
}