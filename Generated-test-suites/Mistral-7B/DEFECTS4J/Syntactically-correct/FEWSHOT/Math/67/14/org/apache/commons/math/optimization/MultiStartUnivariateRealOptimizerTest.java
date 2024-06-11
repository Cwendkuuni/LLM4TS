package org.apache.commons.cli.optimization; // Change the package name if necessary

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.optim.GoalType;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction func = new PolynomialFunction(new double[]{-1, 4}, new double[]{0.0});
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new NichtesMethod(), 10, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });

        try {
            double bestOptimum = optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);
            assertNotNull(bestOptimum);
            System.out.println("Best optimum found: " + bestOptimum);

            double[] allOptima = optimizer.getOptima();
            double[] allOptimaValues = optimizer.getOptimaValues();
            assertEquals(10, allOptima.length);
            assertEquals(10, allOptimaValues.length);

            for (int i = 0; i < allOptima.length; i++) {
                assertFalse(Double.isNaN(allOptima[i]));
                assertFalse(Double.isNaN(allOptimaValues[i]));
                assertTrue(func.value(allOptima[i]) <= func.value(bestOptimum));
            }

            System.out.println("Optima found during multi-start:");
            for (int i = 0; i < allOptima.length; i++) {
                System.out.println("Optimum " + (i + 1) + ": " + allOptima[i] + " => " + func.value(allOptima[i]));
            }

        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}