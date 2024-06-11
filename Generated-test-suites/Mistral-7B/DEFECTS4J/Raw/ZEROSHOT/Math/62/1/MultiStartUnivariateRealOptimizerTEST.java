package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test function for optimization.
     */
    static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            return Math.pow(x, 2) + Math.sin(Math.PI * x);
        }

        @Override
        public double derivative(double x) throws FunctionEvaluationException {
            return 2 * x + Math.cos(Math.PI * x);
        }
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        UnivariateOptimizer optimizer = new FibonacciSearchUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<TestFunction> optimizerMS =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MyRandomGenerator());
        Assert.assertNotNull(optimizerMS);
    }

    /**
     * Test optimization with multiple starts.
     */
    @Test
    public void testOptimization() throws Exception {
        UnivariateOptimizer optimizer = new FibonacciSearchUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<TestFunction> optimizerMS =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MyRandomGenerator());
        TestFunction function = new TestFunction();

        PointValuePair result = optimizerMS.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertFalse(Double.isNaN(result.getValue()));

        PointValuePair[] optima = optimizerMS.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(5, optima.length);
        for (PointValuePair p : optima) {
            if (p == null) {
                continue;
            }
            Assert.assertNotNull(p.getPoint());
            Assert.assertFalse(Double.isNaN(p.getValue()));
        }

        // Check that the best point is the one with the smallest objective value
        double smallestValue = Double.POSITIVE_INFINITY;
        PointValuePair bestPoint = null;
        for (PointValuePair p : optima) {
            if (p == null) {
                continue;
            }
            double value = p.getValue();
            if (value < smallestValue) {
                smallestValue = value;
                bestPoint = p;
            }
        }
        Assert.assertNotNull(bestPoint);
        Assert.assertFalse(Double.isNaN(bestPoint.getValue()));

        // Check that the other points have a bigger objective value
        for (PointValuePair p : optima) {
            if (p == null || p.equals(bestPoint)) {
                continue;
            }
            double value = p.getValue();
            Assert.assertTrue(value > smallestValue);
        }
    }

    /**
     * Test optimization without convergence.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        UnivariateOptimizer optimizer = new FibonacciSearchUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<TestFunction> optimizerMS =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MyRandomGenerator());
        TestFunction function = new TestFunction();

        optimizerMS.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
    }

    /**
     * Custom random generator for tests.
     */
    static class MyRandomGenerator implements RandomGenerator {
        @Override
        public double nextGaussian() {
            return 0.0;
        }

        @Override
        public double nextUniform() {
            return 0.5;
        }

        @Override
        public double nextUniform(double min, double max) {
            return 0.5 * (min + max) + 0.5 * (min - max);
        }
    }

}