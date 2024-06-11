package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredErrorFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test multi-start optimization with {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Test function: x^2 */
    private final static UnivariateFunction TEST_FUNCTION = new SquaredErrorFunction(0d);

    /** Test optimizer with 2 starts */
    @Test
    public void testMultiStartOptimizerTwoStarts() {
        // Create optimizer
        final UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizer, 2, new WelfordCongruentialGenerator(1314L));

        // Optimize
        final double result = multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, -10d, 10d);

        // Check result
        final double expectedMin = -1.0;
        Assert.assertEquals(expectedMin, result, 1e-6);

        // Get optima
        final double[] optima = multiStartOptimizer.getOptima();

        // Check optima
        Assert.assertEquals(2, optima.length);
        final double optima1 = optima[0];
        final double optima2 = optima[1];
        Assert.assertTrue(FunctionUtils.isClose(expectedMin, optima1, 1e-6) || FunctionUtils.isClose(expectedMin, optima2, 1e-6));
        Assert.assertFalse(FunctionUtils.isClose(optima1, optima2, 1e-6));
    }

    /** Test optimizer with 10 starts */
    @Test
    public void testMultiStartOptimizerTenStarts() {
        // Create optimizer
        final UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizer, 10, new WelfordCongruentialGenerator(1314L));

        // Optimize
        final double result = multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, -10d, 10d);

        // Check result
        final double expectedMin = -1.0;
        Assert.assertEquals(expectedMin, result, 1e-6);

        // Get optima
        final double[] optima = multiStartOptimizer.getOptima();

        // Check optima
        Assert.assertEquals(10, optima.length);
        for (final double optimaElement : optima) {
            Assert.assertTrue(FunctionUtils.isClose(expectedMin, optimaElement, 1e-6));
        }

        // Get optima values
        final double[] optimaValues = multiStartOptimizer.getOptimaValues();

        // Check optima values
        Assert.assertEquals(10, optimaValues.length);
        for (final double optimaValue : optimaValues) {
            Assert.assertEquals(expectedMin, optimaValue, 1e-6);
        }
    }

    /** Test optimizer with invalid number of starts */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartOptimizerInvalidNumberOfStarts() {
        // Create optimizer
        final UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizer, 0, new WelfordCongruentialGenerator(1314L));

        // Optimize
        multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, -10d, 10d);
    }

    /** Test optimizer with max iterations and max evaluations */
    @Test
    public void testMultiStartOptimizerMaxIterationsAndMaxEvaluations() {
        // Create optimizer
        final UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizer, 2, new WelfordCongruentialGenerator(1314L));
        multiStartOptimizer.setMaxIterations(5);
        multiStartOptimizer.setMaxEvaluations(10);

        // Optimize
        final double result = multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, -10d, 10d);

        // Check result
        final double expectedMin = -1.0;
        Assert.assertEquals(expectedMin, result, 1e-6);

        // Get optima
        final double[] optima = multiStartOptimizer.getOptima();

        // Check optima
        Assert.assertEquals(2, optima.length);
        final double optima1 = optima[0];
        final double optima2 = optima[1];
        Assert.assertTrue(FunctionUtils.isClose(expectedMin, optima1, 1e-6) || FunctionUtils.isClose(expectedMin, optima2, 1e-6));
        Assert.assertFalse(FunctionUtils.isClose(optima1, optima2, 1e-6));
    }

    /** Test optimizer with max iterations and max evaluations reached */
    @Test
    public void testMultiStartOptimizerMaxIterationsAndMaxEvaluationsReached() {
        // Create optimizer
        final UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizer, 2, new WelfordCongruentialGenerator(1314L));
        multiStartOptimizer.setMaxIterations(1);
        multiStartOptimizer.setMaxEvaluations(2);

        // Optimize
        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, -10d, 10d);
            Assert.fail();
        } catch (final ConvergenceException ignored) {
        }

        // Check result
        Assert.assertFalse(multiStartOptimizer.isConverged());

        // Get optima
        final double[] optima = multiStartOptimizer.getOptima();

        // Check optima
        Assert.assertEquals(2, optima.length);
        Assert.assertTrue(Double.isNaN(optima[0]));
        Assert.assertTrue(Double.isNaN(optima[1]));

        // Get optima values
        final double[] optimaValues = multiStartOptimizer.getOptimaValues();

        // Check optima values
        Assert.assertEquals(2, optimaValues.length);
        for (final double optimaValue : optimaValues) {
            Assert.assertTrue(Double.isNaN(optimaValue));
        }
    }
}