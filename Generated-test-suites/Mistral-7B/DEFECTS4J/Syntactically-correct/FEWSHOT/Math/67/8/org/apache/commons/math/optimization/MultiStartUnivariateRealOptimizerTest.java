package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        int starts = 5;
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwisterFastRandom();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new org.apache.commons.math3.optim.SimpleBoundsMinimizer(), starts, generator);

        try {
            double bestPoint = optimizer.optimize(function, goalType, min, max);
            double[] optima = optimizer.getOptima();
            double[] optimaValues = optimizer.getOptimaValues();

            assertEquals(goalType, GoalType.MINIMIZE);
            assertTrue(Arrays.stream(optima).allMatch(d -> d >= min && d <= max));
            assertTrue(Arrays.stream(optimaValues).allMatch(d -> d >= 0.0));
            assertTrue(Arrays.stream(optimaValues).anyMatch(d -> d < 0.0));
            assertEquals(bestPoint, optima[0]);
            assertEquals(-Math.sqrt(Math.abs(optimaValues[0])), bestPoint, 0.001);

            // Check other optima points
            for (int i = 1; i < optima.length; i++) {
                assertEquals(goalType, GoalType.MINIMIZE);
                assertTrue(Arrays.stream(optima).allMatch(d -> d >= min && d <= max));
                assertTrue(Arrays.stream(optimaValues).allMatch(d -> d >= 0.0));
                assertNotEquals(bestPoint, optima[i]);
                double diff = Math.abs(bestPoint - optima[i]);
                assertTrue(diff > 0.0);
                assertTrue(diff < 1.0);
            }
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}