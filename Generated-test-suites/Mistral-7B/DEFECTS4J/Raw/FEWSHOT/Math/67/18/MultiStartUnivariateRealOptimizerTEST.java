package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.UnivariateOptimizer;
import org.apache.commons.math.optim.goals.GoalType;
import org.apache.commons.math.optim.nonlinear.optimize.AbstractUnivariateOptimizerTest;
import org.apache.commons.math.optim.nonlinear.optimize.UnivariateOptimizerFactory;
import org.apache.commons.math.optim.univariate.BrentOptimizer;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest extends AbstractUnivariateOptimizerTest {
    @Test
    public void testMultiStart() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                UnivariateOptimizerFactory.createBrent(100), 5, RandomGenerator.DEFAULT);

        double[] results = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);

        PointValuePair best = optimizer.getOptima()[0];
        assertArrayEquals(new double[]{best.getPoint(), best.getValue()}, results, 0.001);

        double[] allOptima = optimizer.getOptima();
        assertArrayEquals(5, allOptima.length);
        assertTrue(Arrays.stream(allOptima).anyMatch(d -> Double.isNaN(d)));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoOptimumComputedYet() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                UnivariateOptimizerFactory.createBrent(100), 5, RandomGenerator.DEFAULT);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        optimizer.getOptima();
    }

    @Test
    public void testMaxIterations() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                UnivariateOptimizerFactory.createBrent(1), 5, RandomGenerator.DEFAULT);
        optimizer.setMaximalIterationCount(0);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                UnivariateOptimizerFactory.createBrent(100), 5, RandomGenerator.DEFAULT);
        optimizer.setMaxEvaluations(0);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testOptimaIsNull() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                UnivariateOptimizerFactory.createBrent(100), 5, RandomGenerator.DEFAULT);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertNull(optimizer.getOptima());
    }

    @Test
    public void testOptimaValuesIsNull() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                UnivariateOptimizerFactory.createBrent(100), 5, RandomGenerator.DEFAULT);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertNull(optimizer.getOptimaValues());
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }
}