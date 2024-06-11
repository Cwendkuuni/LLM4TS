package org.apache.commons.cli;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.optimizers.AbstractUnivariateOptimizer;
import org.apache.commons.math.optimization.optimizers.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction function = new AbstractUnivariateOptimizer.UnivariateFunctionAdapter() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 3 * Math.pow(x, 1.5) + 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 1.5 * Math.pow(x, 0.5);
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        int starts = 3;

        RandomGenerator randomGenerator = new org.apache.commons.math3.util.Doubles.UniformRandomGenerator();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new BrentOptimizer(), starts, randomGenerator);

        double optimum = optimizer.optimize(function, goalType, min, max);
        double[] allOptima = optimizer.getOptima();
        double[] allOptimaValues = optimizer.getOptimaValues();

        Assert.assertArrayEquals(new double[]{optimum, optimum, optimum}, allOptima, 0.001);
        Assert.assertArrayEquals(new double[]{function.value(optimum), function.value(optimum), function.value(optimum)}, allOptimaValues, 0.001);
    }
}