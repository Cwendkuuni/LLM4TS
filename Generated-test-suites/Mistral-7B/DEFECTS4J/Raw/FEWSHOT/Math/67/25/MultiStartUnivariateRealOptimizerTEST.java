package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.optim.GoalType;
import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.SimpleBounds;
import org.apache.commons.math.optim.UnivariateOptimizer;
import org.apache.commons.math.random.RandomDataGenerators;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test for multi-start optimization with a simple function, minimizing
     * @see MultiStartUnivariateRealOptimizer
     */
    @Test
    public void testMultiStartMinimization() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexMethod(),
                5,
                RandomDataGenerators.DEFAULT_RANDOM);

        double min = 0.0;
        double max = Math.PI;

        double[] expectedOptima = {
                Math.PI / 4,
                -Math.SQRT2 / 2,
                -Math.SQRT2 / 2,
                Math.PI / 4,
                Math.PI / 4};

        double[] expectedOptimaValues = {
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2};

        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        assertArrayEquals(expectedOptima, optimizer.getOptima(), 0.0001);
        assertArrayEquals(expectedOptimaValues, optimizer.getOptimaValues(), 0.0001);
        assertTrue(FunctionUtils.isEqual(result, expectedOptima[0], 0.0001));
    }

    /**
     * Test for multi-start optimization with a simple function, maximizing
     * @see MultiStartUnivariateRealOptimizer
     */
    @Test
    public void testMultiStartMaximization() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexMethod(),
                5,
                RandomDataGenerators.DEFAULT_RANDOM);

        double min = 0.0;
        double max = Math.PI;

        double[] expectedOptima = {
                -Math.PI / 4,
                Math.SQRT2 / 2,
                Math.SQRT2 / 2,
                -Math.PI / 4,
                -Math.PI / 4};

        double[] expectedOptimaValues = {
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2,
                Math.sqrt(2.0) / 2};

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, min, max);
        assertArrayEquals(expectedOptima, optimizer.getOptima(), 0.0001);
        assertArrayEquals(expectedOptimaValues, optimizer.getOptimaValues(), 0.0001);
        assertTrue(FunctionUtils.isEqual(result, expectedOptima[0], 0.0001));
    }

    /**
     * Test for multi-start optimization with a custom function and bounds, minimizing
     * @see MultiStartUnivariateRealOptimizer
     */
    @Test
    public void testMultiStartCustomMinimization() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 5 * Math.pow(Math.cos(x), 2);
            }
        };

        SimpleBounds bounds = new SimpleBounds(0.0, 2.0 * Math.PI);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BroydenFletcherGoldfarbShannoMinimizer(),
                5,
                RandomDataGenerators.DEFAULT_RANDOM);

        double min = 0.0;
        double max = 2.0 * Math.PI;

        double[] expectedOptima = {
                Math.PI / 4,
                Math.PI / 4,
                Math.PI / 4,
                Math.PI / 4,
                Math.PI / 4};

        double[] expectedOptimaValues = {
                25.0 + Math.sqrt(32.0) * Math.sqrt(2.0),
                25.0 + Math.sqrt(32.0) * Math.sqrt(2.0),
                25.0 + Math.sqrt(32.0) * Math.sqrt(2.0),
                25.0 + Math.sqrt(32.0) * Math.sqrt(2.0),
                25.0 + Math.sqrt(32.0) * Math.sqrt(2.0)};

        double result = optimizer.optimize(f, GoalType.MINIMIZE, bounds);
        assertArrayEquals(expectedOptima, optimizer.getOptima(), 0.0001);
        assertArrayEquals(expectedOptimaValues, optimizer.getOptimaValues(), 0.0001);
        assertTrue(FunctionUtils.isEqual(result, expectedOptima[0], 0.0001));
    }

    /**
     * Test for multi-start optimization with a custom function and bounds, maximizing
     * @see MultiStartUnivariateRealOptimizer
     */
    @Test
    public void testMultiStartCustomMaximization() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 5 * Math.pow(Math.cos(x), 2);
            }
        };

        SimpleBounds bounds = new SimpleBounds(0.0, 2.0 * Math.PI);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BroydenFletcherGoldfarbShannoMinimizer(),
                5,
                RandomDataGenerators.DEFAULT_RANDOM);

        double min = 0.0;
        double max = 2.0 * Math.PI;

        double[] expectedOptima = {
                -Math.PI / 4,
                -Math.PI / 4,
                -Math.PI / 4,
                -Math.PI / 4,
                -Math.PI / 4};

        double[] expectedOptimaValues = {
                -(25.0 + Math.sqrt(32.0) * Math.sqrt(2.0)),
                -(25.0 + Math.sqrt(32.0) * Math.sqrt(2.0)),
                -(25.0 + Math.sqrt(32.0) * Math.sqrt(2.0)),
                -(25.0 + Math.sqrt(32.0) * Math.sqrt(2.0)),
                -(25.0 + Math.sqrt(32.0) * Math.sqrt(2.0))};

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, bounds);
        assertArrayEquals(expectedOptima, optimizer.getOptima(), 0.0001);
        assertArrayEquals(expectedOptimaValues, optimizer.getOptimaValues(), 0.0001);
        assertTrue(FunctionUtils.isEqual(-result, expectedOptima[0], 0.0001));
    }

    /**
     * Test for multi-start optimization with a function that doesn't converge
     * @see MultiStartUnivariateRealOptimizer
     */
    @Test(expected = ConvergenceException.class)
    public void testMultiStartNoConvergence() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) / (1.0 - Math.abs(x));
            }
        };

        SimpleBounds bounds = new SimpleBounds(-2.0, 2.0);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BroydenFletcherGoldfarbShannoMinimizer(),
                5,
                RandomDataGenerators.DEFAULT_RANDOM);

        double min = 0.0;
        double max = 2.0;

        optimizer.optimize(f, GoalType.MINIMIZE, bounds);
    }
}