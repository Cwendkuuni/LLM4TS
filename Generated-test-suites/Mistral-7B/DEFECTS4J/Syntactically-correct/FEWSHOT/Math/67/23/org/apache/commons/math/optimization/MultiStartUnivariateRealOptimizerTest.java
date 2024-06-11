package org.apache.commons.cli.optimization; // Change the package name if needed

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizer() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(), 10, new Random());

        double bestResult = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertNotNull(bestResult);

        double[] optima = optimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(10, optima.length);

        double[] optimaValues = optimizer.getOptimaValues();
        Assert.assertNotNull(optimaValues);
        Assert.assertEquals(10, optimaValues.length);

        for (int i = 0; i < optima.length; i++) {
            Assert.assertTrue(Math.abs(bestResult - optima[i]) < 0.001);
            Assert.assertTrue(Math.pow(optima[i], 2) >= Math.pow(bestResult, 2));
        }
    }
}

// Replace SimpleUnivariateRealOptimizer with the actual optimizer class you want to test multi-start on.