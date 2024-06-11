package org.apache.commons.cli;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MaxEval;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.optimizers.AbstractUnivariateOptimizer;
import org.apache.commons.math.optimization.optimizers.BrentOptimizer;
import org.apache.commons.math.optimization.optimizers.SimpleLineSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimization() {
        UnivariateRealFunction function = new AbstractUnivariateOptimizer.UnivariateFunctionAdapter() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 10, new Random());

        try {
            double x = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.assertEquals(2.0, x, 0.001);
            double[] optima = optimizer.getOptima();
            double[] optimaValues = optimizer.getOptimaValues();
            Assert.assertEquals(10, optima.length);
            for (int i = 0; i < optima.length; i++) {
                Assert.assertNotEquals(Double.NaN, optima[i]);
                Assert.assertEquals(2.0, optima[i], 0.001);
                Assert.assertEquals(Math.pow(optima[i] - 2.0, 2.0), optimaValues[i], 0.001);
            }
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMultiStartMaximization() {
        UnivariateRealFunction function = new AbstractUnivariateOptimizer.UnivariateFunctionAdapter() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2.0, 2.0);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 10, new Random());

        try {
            double x = optimizer.optimize(function, GoalType.MAXIMIZE, -10.0, 10.0);
            Assert.assertEquals(2.0, x, 0.001);
            double[] optima = optimizer.getOptima();
            double[] optimaValues = optimizer.getOptimaValues();
            Assert.assertEquals(10, optima.length);
            for (int i = 0; i < optima.length; i++) {
                Assert.assertNotEquals(Double.NaN, optima[i]);
                Assert.assertEquals(2.0, optima[i], 0.001);
                Assert.assertEquals(-Math.pow(optima[i] - 2.0, 2.0), optimaValues[i], 0.001);
            }
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMultiStartNoConvergence() {
        UnivariateRealFunction function = new AbstractUnivariateOptimizer.UnivariateFunctionAdapter() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 10, new Random());

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail("No ConvergenceException was thrown");
        } catch (ConvergenceException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException was thrown: " + e.getMessage());
        }

        try {
            double[] optima = optimizer.getOptima();
            Assert.assertArrayEquals(new double[10], optima);
            for (int i = 0; i < optima.length; i++) {
                Assert.assertEquals(Double.NaN, optima[i]);
            }
        } catch (IllegalStateException e) {
            Assert.fail("IllegalStateException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMultiStartNoMaxEvaluations() {
        UnivariateRealFunction function = new AbstractUnivariateOptimizer.UnivariateFunctionAdapter() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 1, new Random());

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0, 0.0);
            Assert.fail("No ConvergenceException was thrown");
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (ConvergenceException e) {
            Assert.fail("ConvergenceException was thrown: " + e.getMessage());
        }

        try {
            double[] optima = optimizer.getOptima();
            Assert.assertArrayEquals(new double[1], optima);
            Assert.assertEquals(Double.NaN, optima[0]);
        } catch (IllegalStateException e) {
            Assert.fail("IllegalStateException was thrown: " + e.getMessage());
        }

        try {
            optimizer.setMaxEvaluations(MaxEval.UNLIMITED);
            double x = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0, 0.0);
            Assert.assertEquals(0.0, x, 0.001);
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (ConvergenceException e) {
            Assert.fail("ConvergenceException was thrown: " + e.getMessage());
        }

        try {
            double[] optima = optimizer.getOptima();
            Assert.assertArrayEquals(new double[11], optima);
            for (int i = 0; i < optima.length; i++) {
                if (i < 10) {
                    Assert.assertNotEquals(Double.NaN, optima[i]);
                    Assert.assertEquals(0.0, optima[i], 0.001);
                } else {
                    Assert.assertEquals(Double.NaN, optima[i]);
                }
            }
        } catch (IllegalStateException e) {
            Assert.fail("IllegalStateException was thrown: " + e.getMessage());
        }
    }
}