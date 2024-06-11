package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethodOptimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimize method for a simple univariate function.
     */
    @org.junit.Test
    public void testOptimizeSimpleFunction() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{-1.0, 2.0, -1.0});
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethodOptimizer(100), 10, new MersenneTwister());
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        double result;
        double[] optima;
        double[] optimaValues;

        result = optimizer.optimize(f, goalType, min, max);
        assertEquals(result, -2.0, 1e-13);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertNotNull(optima);
        assertNotNull(optimaValues);
        assertEquals(optima.length, 11);
        assertEquals(optimaValues.length, 11);

        for (int i = 0; i < optima.length; i++) {
            assertEquals(Double.isNaN(optima[i]), (i == 0));
            assertEquals(Double.isNaN(optimaValues[i]), (i == 0));
            if (!Double.isNaN(optima[i])) {
                assertEquals(goalType == GoalType.MINIMIZE ? -2.0 : 2.0, optimaValues[i], 1e-13);
            }
        }

        new AveragedStartingPointInitializer().setOptimizer(optimizer);
        result = optimizer.optimize(f, goalType, min, max, 2.0);
        assertEquals(result, -1.5, 1e-13);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertEquals(optima.length, 12);
        assertEquals(optimaValues.length, 12);

        for (int i = 0; i < optima.length; i++) {
            assertEquals(Double.isNaN(optima[i]), (i < 11));
            if (!Double.isNaN(optima[i])) {
                assertEquals(goalType == GoalType.MINIMIZE ? -1.5 : 1.5, optimaValues[i], 1e-13);
            }
        }

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertEquals(optima[0], result, 1e-13);
        assertEquals(optimaValues[0], goalType == GoalType.MINIMIZE ? -1.5 : 1.5, 1e-13);
    }

    /**
     * Test optimize method for a univariate function with multiple local minima.
     */
    @org.junit.Test
    public void testOptimizeMultipleLocalMinimaFunction() {
        UnivariateRealFunction f = new PolynomialFunction(5, new double[]{1.0, 0.0, -2.0, 1.0, -1.0});
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethodOptimizer(100), 10, new MersenneTwister());
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        double result;
        double[] optima;
        double[] optimaValues;

        result = optimizer.optimize(f, goalType, min, max);
        assertEquals(result, -1.0, 1e-13);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertNotNull(optima);
        assertNotNull(optimaValues);
        assertEquals(optima.length, 11);
        assertEquals(optimaValues.length, 11);

        for (int i = 0; i < optima.length; i++) {
            assertEquals(Double.isNaN(optima[i]), (i == 0));
            assertEquals(Double.isNaN(optimaValues[i]), (i == 0));

            if (!Double.isNaN(optima[i])) {
                assertEquals(goalType == GoalType.MINIMIZE ? -1.0 : 1.0, optimaValues[i], 1e-13);
            }
        }

        new AveragedStartingPointInitializer().setOptimizer(optimizer);
        result = optimizer.optimize(f, goalType, min, max, 2.0);
        assertEquals(result, -0.788314154568826, 1e-13);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertEquals(optima.length, 12);
        assertEquals(optimaValues.length, 12);

        for (int i = 0; i < optima.length; i++) {
            assertEquals(Double.isNaN(optima[i]), (i < 11));
            if (!Double.isNaN(optima[i])) {
                assertEquals(goalType == GoalType.MINIMIZE ? -0.788314154568826 : 0.788314154568826, optimaValues[i], 1e-13);
            }
        }

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertEquals(optima[0], result, 1e-13);
        assertEquals(optimaValues[0], goalType == GoalType.MINIMIZE ? -0.788314154568826 : 0.788314154568826, 1e-13);
    }

    /**
     * Test optimize method with different goal types.
     */
    @org.junit.Test
    public void testOptimizeDifferentGoalTypes() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{0.0, -2.0, 2.0});
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethodOptimizer(100), 10, new MersenneTwister());
        double min = -5.0;
        double max = 5.0;

        // Test minimization
        GoalType goalTypeMin = GoalType.MINIMIZE;
        double resultMin;
        double[] optimaMin;
        double[] optimaValuesMin;

        resultMin = optimizer.optimize(f, goalTypeMin, min, max);
        assertEquals(resultMin, -2.0, 1e-13);

        optimaMin = optimizer.getOptima();
        optimaValuesMin = optimizer.getOptimaValues();

        assertEquals(optimaMin.length, 11);
        assertEquals(optimaValuesMin.length, 11);

        for (int i = 0; i < optimaMin.length; i++) {
            if (!Double.isNaN(optimaMin[i])) {
                assertEquals(goalTypeMin == GoalType.MINIMIZE ? -2.0 : 2.0, optimaValuesMin[i], 1e-13);
            }
        }

        // Test maximization
        GoalType goalTypeMax = GoalType.MAXIMIZE;
        double resultMax;
        double[] optimaMax;
        double[] optimaValuesMax;

        resultMax = optimizer.optimize(f, goalTypeMax, min, max);
        assertEquals(resultMax, 2.0, 1e-13);

        optimaMax = optimizer.getOptima();
        optimaValuesMax = optimizer.getOptimaValues();

        assertEquals(optimaMax.length, 11);
        assertEquals(optimaValuesMax.length, 11);

        for (int i = 0; i < optimaMax.length; i++) {
            if (!Double.isNaN(optimaMax[i])) {
                assertEquals(goalTypeMax == GoalType.MINIMIZE ? 2.0 : -2.0, optimaValuesMax[i], 1e-13);
            }
        }
    }

    /**
     * Test optimize method with different initial starting points.
     */
    @org.junit.Test
    public void testOptimizeDifferentStartingPoints() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{0.0, -2.0, 2.0});
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethodOptimizer(100), 10, new MersenneTwister());
        double min = -5.0;
        double max = 5.0;
        double initialPoint = 3.0;

        GoalType goalType = GoalType.MINIMIZE;
        double result;
        double[] optima;
        double[] optimaValues;

        result = optimizer.optimize(f, goalType, min, max, initialPoint);
        assertEquals(result, -1.547005383455715, 1e-13);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();

        assertEquals(optima.length, 11);
        assertEquals(optimaValues.length, 11);

        for (int i = 0; i < optima.length; i++) {
            if (!Double.isNaN(optima[i])) {
                assertEquals(goalType == GoalType.MINIMIZE ? -1.547005383455715 : 1.547005383455715, optimaValues[i], 1e-13);
            }
        }
    }

    /**
     * Test optimize method with a custom optimizer.
     */
    @org.junit.Test
    public void testOptimizeCustomOptimizer() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{-1.0, 2.0, -1.0});
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new MySimpleBoundingMethodOptimizer(100), 10, new MersenneTwister());
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        double result;
        double[] optima;
        double[] optimaValues;

        result = optimizer.optimize(f, goalType, min, max);
        assertEquals(result, -2.0, 1e-13);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();

        assertNotNull(optima);
        assertNotNull(optimaValues);
        assertEquals(optima.length, 11);
        assertEquals(optimaValues.length, 11);

        for (int i = 0; i < optima.length; i++) {
            assertEquals(Double.isNaN(optima[i]), (i == 0));
            assertEquals(Double.isNaN(optimaValues[i]), (i == 0));
            if (!Double.isNaN(optima[i])) {
                assertEquals(goalType == GoalType.MINIMIZE ? -2.0 : 2.0, optimaValues[i], 1e-13);
            }
        }

        new AveragedStartingPointInitializer().setOptimizer(optimizer);
        result = optimizer.optimize(f, goalType, min, max, 2.0);
        assertEquals(result, -1.5, 1e-13);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertEquals(optima.length, 12);
        assertEquals(optimaValues.length, 12);

        for (int i = 0; i < optima.length; i++) {
            assertEquals(Double.isNaN(optima[i]), (i < 11));
            if (!Double.isNaN(optima[i])) {
                assertEquals(goalType == GoalType.MINIMIZE ? -1.5 : 1.5, optimaValues[i], 1e-13);
            }
        }

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();
        assertEquals(optima[0], result, 1e-13);
        assertEquals(optimaValues[0], goalType == GoalType.MINIMIZE ? -1.5 : 1.5, 1e-13);
    }

    private static class MySimpleBoundingMethodOptimizer extends SimpleBoundingMethodOptimizer {

        public MySimpleBoundingMethodOptimizer(int maxIterations) {
            super(maxIterations);
        }

        @Override
        public PointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
                throws ConvergenceException, FunctionEvaluationException {
            double x = super.optimize(f, goalType, min, max).getPoint();
            return new PointValuePair(x + Math.random() * 1e-5, f.value(x));
        }

    }
}