package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;
    private double[] expectedOptima;
    private double[] expectedOptimaValues;

    @Before
    public void setUp() {
        function = new MyFunction();
        optimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearchRealOptimizer(), 10, new RandomDataGenerator());
        expectedOptima = new double[10];
        expectedOptimaValues = new double[10];
    }

    @Test
    public void testOptimize() throws Exception {
        for (int i = 0; i < 10; ++i) {
            double optimum = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            expectedOptima[i] = function.value(optimum);
            expectedOptimaValues[i] = optimum;
        }
        double[] actualOptima = optimizer.getOptima();
        double[] actualOptimaValues = optimizer.getOptimaValues();
        assertArrayEquals(expectedOptima, actualOptima, 0.001);
        assertArrayEquals(expectedOptimaValues, actualOptimaValues, 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoOptimumComputed() {
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }

            @Override
            public int getDimension() {
                return 1;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.getOptima();
    }

    @Test
    public void testMultipleStartsForSimpleFunction() throws Exception {
        FibonacciSearchRealOptimizer simpleOptimizer = new FibonacciSearchRealOptimizer();
        double[] simpleOptima = new double[10];
        double[] simpleOptimaValues = new double[10];
        for (int i = 0; i < 10; ++i) {
            simpleOptima[i] = simpleOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            simpleOptimaValues[i] = function.value(simpleOptima[i]);
        }
        optimizer = new MultiStartUnivariateRealOptimizer(simpleOptimizer, 10, new RandomDataGenerator());
        double[] multipleOptima = optimizer.getOptima();
        double[] multipleOptimaValues = optimizer.getOptimaValues();
        for (int i = 0; i < 10; ++i) {
            assertArrayEquals(new PointValuePair(simpleOptima[i], simpleOptimaValues[i]),
                              new PointValuePair(multipleOptima[i], multipleOptimaValues[i]), 0.001);
        }
    }

    @Test
    public void testMaxIterations() throws Exception {
        optimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearchRealOptimizer(), 10, new RandomDataGenerator());
        optimizer.setMaximalIterationCount(10);
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        optimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearchRealOptimizer(), 10, new RandomDataGenerator());
        optimizer.setMaxEvaluations(100);
        try {
            optimizer.optimize(new MyFunctionWithEvalLimit(), GoalType.MINIMIZE, 0.0, 1.0);
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.E, -x);
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    private static class MyFunctionWithEvalLimit implements UnivariateRealFunction {
        private int maxEvaluations = 10;

        @Override
        public double value(double x) {
            maxEvaluations --;
            return Math.pow(Math.E, -x);
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }
    }

}