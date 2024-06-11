package org.apache.commons.cli.optimization; // Make sure to import all necessary packages

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;

        RandomGenerator generator = org.apache.commons.math3.MathUtils.createNisanGenerator();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(), 5, generator);

        try {
            double optimum = optimizer.optimize(function, goalType, min, max);
            PointValuePair pvp = optimizer.getLastOptimum();
            double[] optima = optimizer.getOptima();
            double[] optimaValues = optimizer.getOptimaValues();

            assertEquals(optimum, optima[0], 0.001);
            assertEquals(pvp.getValue(), optimaValues[0], 0.001);
            assertArrayEquals(new double[]{optimum}, optima, 0.001);
            assertArrayEquals(new double[]{optimaValues[0]}, optimaValues, 0.001);

            for (int i = 1; i < optima.length; i++) {
                assertNotEquals(optima[i], optima[0], 0.001);
                assertNotEquals(optimaValues[i], optimaValues[0], 0.001);
                assertTrue(Double.isNaN(optima[i]) || Double.isNaN(optimaValues[i]));
            }

            assertTrue(optimizer.getIterationCount() > 5);
            assertTrue(optimizer.getEvaluations() > 5);

        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultiStartUnivariateRealOptimizerWithMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;

        RandomGenerator generator = org.apache.commons.math3.MathUtils.createNisanGenerator();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(), 5, generator) {
            @Override
            public int getMaxIterations() {
                return 10;
            }
        };

        try {
            double optimum = optimizer.optimize(function, goalType, min, max);
            PointValuePair pvp = optimizer.getLastOptimum();
            double[] optima = optimizer.getOptima();
            double[] optimaValues = optimizer.getOptimaValues();

            assertEquals(optimum, optima[0], 0.001);
            assertEquals(pvp.getValue(), optimaValues[0], 0.001);
            assertArrayEquals(new double[]{optimum}, optima, 0.001);
            assertArrayEquals(new double[]{optimaValues[0]}, optimaValues, 0.001);

            for (int i = 1; i < optima.length; i++) {
                assertNotEquals(optima[i], optima[0], 0.001);
                assertNotEquals(optimaValues[i], optimaValues[0], 0.001);
                assertTrue(Double.isNaN(optima[i]) || Double.isNaN(optimaValues[i]));
            }

            assertTrue(optimizer.getIterationCount() <= 10);
            assertTrue(optimizer.getEvaluations() > 5);

        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultiStartUnivariateRealOptimizerWithMaxEvaluations() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;

        RandomGenerator generator = org.apache.commons.math3.MathUtils.createNisanGenerator();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(), 5, generator) {
            @Override
            public int getMaxEvaluations() {
                return 10;
            }
        };

        try {
            double optimum = optimizer.optimize(function, goalType, min, max);
            PointValuePair pvp = optimizer.getLastOptimum();
            double[] optima = optimizer.getOptima();
            double[] optimaValues = optimizer.getOptimaValues();

            assertEquals(optimum, optima[0], 0.001);
            assertEquals(pvp.getValue(), optimaValues[0], 0.001);
            assertArrayEquals(new double[]{optimum}, optima, 0.001);
            assertArrayEquals(new double[]{optimaValues[0]}, optimaValues, 0.001);

            for (int i = 1; i < optima.length; i++) {
                assertNotEquals(optima[i], optima[0], 0.001);
                assertNotEquals(optimaValues[i], optimaValues[0], 0.001);
                assertTrue(Double.isNaN(optima[i]) || Double.isNaN(optimaValues[i]));
            }

            assertTrue(optimizer.getIterationCount() > 0);
            assertTrue(optimizer.getEvaluations() <= 10);

        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}