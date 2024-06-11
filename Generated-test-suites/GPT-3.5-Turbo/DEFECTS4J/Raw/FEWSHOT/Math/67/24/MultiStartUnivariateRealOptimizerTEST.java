package org.apache.commons.math.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.random.MersenneTwister;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;
    private double min;
    private double max;
    private double[] optima;
    private int starts;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerStub(), 5, new MersenneTwister());
        function = new UnivariateRealFunctionStub();
        min = 0;
        max = 10;
        optima = new double[] {5.0, 3.0, 8.0, 2.5, 6.5};
        starts = 5;
    }

    @After
    public void tearDown() {
        optimizer = null;
        function = null;
        min = 0;
        max = 0;
        optima = null;
        starts = 0;
    }

    @Test
    public void testGetOptima() throws Exception {
        // run optimizer
        optimizer.optimize(function, GoalType.MAXIMIZE, min, max);

        // get optima
        double[] actualOptima = optimizer.getOptima();

        // assert optima
        for (int i = 0; i < starts; i++) {
            assertEquals(optima[i], actualOptima[i], 0.00001);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetOptimaBeforeOptimize() throws Exception {
        // get optima before optimize
        optimizer.getOptima();
    }

    @Test
    public void testGetOptimaValues() throws Exception {
        // run optimizer
        optimizer.optimize(function, GoalType.MAXIMIZE, min, max);

        // get optima values
        double[] actualOptimaValues = optimizer.getOptimaValues();

        // assert optima values
        for (int i = 0; i < starts; i++) {
            assertEquals(optima[i] * 2, actualOptimaValues[i], 0.00001);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetOptimaValuesBeforeOptimize() throws Exception {
        // get optima values before optimize
        optimizer.getOptimaValues();
    }

    private class UnivariateRealOptimizerStub implements UnivariateRealOptimizer {
        public double getFunctionValue() {
            return 0;
        }

        public double getResult() {
            return 0;
        }

        public double getAbsoluteAccuracy() {
            return 0;
        }

        public int getIterationCount() {
            return 0;
        }

        public int getMaximalIterationCount() {
            return 0;
        }

        public int getMaxEvaluations() {
            return 0;
        }

        public int getEvaluations() {
            return 0;
        }

        public double getRelativeAccuracy() {
            return 0;
        }

        public void resetAbsoluteAccuracy() {
        }

        public void resetMaximalIterationCount() {
        }

        public void resetRelativeAccuracy() {
        }

        public void setAbsoluteAccuracy(double accuracy) {
        }

        public void setMaximalIterationCount(int count) {
        }

        public void setMaxEvaluations(int maxEvaluations) {
        }

        public void setRelativeAccuracy(double accuracy) {
        }

        public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max) throws FunctionEvaluationException, ConvergenceException {
            return 0;
        }

        public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue) throws FunctionEvaluationException, ConvergenceException {
            return 0;
        }
    }

    private class UnivariateRealFunctionStub implements UnivariateRealFunction {
        public double value(double x) {
            return x * 2;
        }
    }
}