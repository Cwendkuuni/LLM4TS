package org.apache.commons.math.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        UnivariateRealOptimizer optimizer = new UnivariateRealOptimizer() {
            public double optimize(UnivariateRealFunction f, GoalType goalType,
                                   double min, double max)
                    throws ConvergenceException, FunctionEvaluationException {
                return f.value((min + max) / 2);
            }

            public double optimize(UnivariateRealFunction f, GoalType goalType,
                                   double min, double max, double startValue)
                    throws ConvergenceException, FunctionEvaluationException {
                return f.value(startValue);
            }

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
        };

        int starts = 5;
        RandomGenerator generator = new JDKRandomGenerator();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        double min = -10;
        double max = 10;

        double result = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, min, max);

        assertTrue(result >= min);
        assertTrue(result <= max);

        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();

        assertNotNull(optima);
        assertNotNull(optimaValues);
        assertEquals(starts, optima.length);
        assertEquals(starts, optimaValues.length);
    }
}