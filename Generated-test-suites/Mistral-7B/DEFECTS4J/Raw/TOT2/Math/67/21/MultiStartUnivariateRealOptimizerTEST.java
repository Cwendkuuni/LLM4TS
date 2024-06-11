package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1, 1, -1}, new double[]{0, 1, 0});
        optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexOptimizer(), 5, new MersenneTwisterFastRandom());
    }

    @Test
    public void testBasicFunctionality() {
        final double min = -5.0;
        final double max = 5.0;
        try {
            double result = optimizer.optimize(function, OptimizationSimpleGoalType.MINIMIZE, min, max);
            Assert.assertEquals(0.0, result, 1e-9);
        } catch (OptimizationException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEdgeCases() {
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;
        try {
            optimizer.optimize(function, OptimizationSimpleGoalType.MINIMIZE, min, max);
            Assert.fail("Expected ConvergenceException but no exception was thrown.");
        } catch (ConvergenceException e) {
            // Expected
        } finally {
            Assert.assertArrayNotEquals(new double[]{Double.NaN}, optimizer.getOptima());
            Assert.assertArrayNotEquals(new double[]{Double.NaN}, optimizer.getOptimaValues());
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        final UnivariateRealFunction brokenFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        optimizer.optimize(brokenFunction, OptimizationSimpleGoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        optimizer = new MultiStartUnivariateRealOptimizer(null, 5, new MersenneTwisterFastRandom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException1() {
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexOptimizer(), 0, new MersenneTwisterFastRandom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException2() {
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexOptimizer(), 5, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        optimizer.optimize(function, OptimizationSimpleGoalType.MINIMIZE, 0.0, 0.0);
    }

}