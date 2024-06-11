package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalings.LinearScaling;
import org.apache.commons.math3.optim.nonlinear.scalings.SimpleScaling;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizerFactory;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer_minimize() throws Exception {
        UnivariateRealOptimizer optimizer = new UnivariateOptimizerFactory().create("SparseNelderMead");
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, -1}, 3);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double expectedResult = -2.0;

        double result = multiStartOptimizer.optimize(f, goalType, min, max);
        assertEquals(expectedResult, result, 1e-6);
        double[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult}, optima, 1e-6);
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult}, optimaValues, 1e-6);
        int totalIterations = multiStartOptimizer.getIterationCount();
        int totalEvaluations = multiStartOptimizer.getEvaluations();
        assertEquals(110, totalIterations);
        assertEquals(120, totalEvaluations);
    }

    @Test
    public void testMultiStartUnivariateRealOptimizer_maximize() throws Exception {
        UnivariateRealOptimizer optimizer = new UnivariateOptimizerFactory().create("BroydenFletcherGoldfarbShanno");
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, -1, 1}, 3);
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;
        double expectedResult = 2.0;

        double result = multiStartOptimizer.optimize(f, goalType, min, max);
        assertEquals(expectedResult, result, 1e-6);
        double[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult}, optima, 1e-6);
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult, expectedResult}, optimaValues, 1e-6);
        int totalIterations = multiStartOptimizer.getIterationCount();
        int totalEvaluations = multiStartOptimizer.getEvaluations();
        assertEquals(110, totalIterations);
        assertEquals(120, totalEvaluations);
    }

    @Test(expected = IllegalStateException.class)
    public void testMultiStartUnivariateRealOptimizer_noOptimumComputedYet() throws Exception {
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
        multiStartOptimizer.optimize(new PolynomialFunction(new double[]{1}, 1), GoalType.MINIMIZE, -1.0, 1.0);
        multiStartOptimizer.getOptima();
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testMultiStartUnivariateRealOptimizer_functionEvaluationException() throws Exception {
        UnivariateRealOptimizer optimizer = new UnivariateOptimizerFactory().create("GoldenSectionSearch");
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 1, null);

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1000000, 1, 1}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        multiStartOptimizer.optimize(f, goalType, min, max);
    }

    @Test(expected = ConvergenceException.class)
    public void testMultiStartUnivariateRealOptimizer_convergenceException() throws Exception {
        UnivariateRealOptimizer optimizer = new UnivariateOptimizerFactory().create("NelderMeadSimplex");
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 1, null);

        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, 1}, 2);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        multiStartOptimizer.setMaxIterations(0);
        multiStartOptimizer.optimize(f, goalType, min, max);
    }

    @Test
    public void testMultiStartUnivariateRealOptimizer_differentScaling() throws Exception {
        UnivariateRealOptimizer optimizer = new UnivariateOptimizerFactory().create("NelderMeadSimplex");
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, 1}, 2);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        multiStartOptimizer.setScaling(new SimpleScaling());
        double result1 = multiStartOptimizer.optimize(f, goalType, min, max);
        double[] optima1 = multiStartOptimizer.getOptima();
        double[] optimaValues1 = multiStartOptimizer.getOptimaValues();
        int totalIterations1 = multiStartOptimizer.getIterationCount();
        int totalEvaluations1 = multiStartOptimizer.getEvaluations();

        multiStartOptimizer.setScaling(new LinearScaling());
        double result2 = multiStartOptimizer.optimize(f, goalType, min, max);
        double[] optima2 = multiStartOptimizer.getOptima();
        double[] optimaValues2 = multiStartOptimizer.getOptimaValues();
        int totalIterations2 = multiStartOptimizer.getIterationCount();
        int totalEvaluations2 = multiStartOptimizer.getIterationCount();

        assertEquals(result1, result2, 1e-6);
        assertArrayEquals(optima1, optima2, 1e-6);
        assertArrayEquals(optimaValues1, optimaValues2, 1e-6);
        assertEquals(totalIterations1, totalIterations2);
        assertEquals(totalEvaluations1, totalEvaluations2);
    }
}