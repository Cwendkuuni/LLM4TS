package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimization() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + 2 * Math.pow(x, 2) + Math.pow(x, 1) + 1;
            }
        };

        BaseUnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, neworg.apache.commons.math3.random.MersenneTwisterFastRandom());

        double min = -5.0;
        double max = 5.0;

        PointValuePair bestOptimum = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double bestValue = bestOptimum.getValue();

        assertEquals(-1.2624408521311654, bestValue, 0.001);

        // Check that all optima are sorted
        PointValuePair[] allOptima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{
                new PointValuePair(-0.4188693669537592, -1.2708926377383747),
                new PointValuePair(0.0320236298264177, -1.2708926377383747),
                new PointValuePair(-1.1108376143915542, -1.2708926377383747),
                new PointValuePair(-1.2624408521311654, -1.2708926377383747),
                new PointValuePair(-1.3345585163423915, -1.2708926377383747),
                null}, allOptima);
    }

    @Test
    public void testMultiStartMaximization() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + 2 * Math.pow(x, 2) + Math.pow(x, 1) + 1;
            }
        };

        BaseUnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, neworg.apache.commons.math3.random.MersenneTwisterFastRandom());

        double min = -5.0;
        double max = 5.0;

        PointValuePair bestOptimum = multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, min, max);
        double bestValue = bestOptimum.getValue();

        assertEquals(1.2708926377383747, bestValue, 0.001);

        // Check that all optima are sorted
        PointValuePair[] allOptima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{
                null,
                null,
                null,
                null,
                new PointValuePair(0.4188693669537592, 1.2708926377383747),
                new PointValuePair(1.1108376143915542, 1.2708926377383747)
        }, allOptima);
    }

    @Test
    public void testMaxEvaluationsReached() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + 2 * Math.pow(x, 2) + Math.pow(x, 1) + 1;
            }
        };

        BaseUnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, neworg.apache.commons.math3.random.MersenneTwisterFastRandom());
        multiStartOptimizer.setMaxEvaluations(10);

        double min = -5.0;
        double max = 5.0;

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
            fail("Expected MaxEvaluationsExceededException");
        } catch (MaxEvaluationsExceededException e) {
            //expected
        }

        // Check that no optima have been found
        PointValuePair[] allOptima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{}, allOptima);
    }

    @Test
    public void testNoConvergenceWithAnyStartPoint() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + 2 * Math.pow(x, 2) + Math.pow(x, 1) + 1;
            }
        };

        BaseUnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 1, neworg.apache.commons.math3.random.MersenneTwisterFastRandom());

        double min = -5.0;
        double max = 5.0;

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            //expected
        }

        // Check that no optima have been found
        PointValuePair[] allOptima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{}, allOptima);
    }
}