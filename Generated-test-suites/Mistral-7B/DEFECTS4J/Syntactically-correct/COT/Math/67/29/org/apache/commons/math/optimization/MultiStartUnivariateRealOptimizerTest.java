package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(Math.sin(x)) - E.E10.doubleValue();
            }
        };
        optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexOptimizer(), 10, new Random());
    }

    @Test
    public void testGetFunctionValue() {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertEquals(function.value(0.0), optimizerWrapper.getFunctionValue(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        double optimum = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertEquals(function.value(optimum), optimizer.getFunctionValue(), 1e-9);
    }

    @Test
    public void testGetResult() {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertEquals(0.0, optimizerWrapper.getResult(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        double optimum = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertEquals(optimum, optimizer.getResult(), 1e-9);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertEquals(Double.NaN, optimizerWrapper.getAbsoluteAccuracy(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.setAbsoluteAccuracy(1e-5);
        Assert.assertEquals(1e-5, optimizer.getAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testGetIterationCount() {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertEquals(0, optimizerWrapper.getIterationCount());

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertGreaterThan(optimizer.getIterationCount(), 0);
    }

    @Test
    public void testGetMaxIterations() {
        // Basic test
        MultiStartUnivariateRealOptimizer optimizerWrapper = (MultiStartUnivariateRealOptimizer) optimizer;
        Assert.assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxIterations());

        // Test setting and getting
        optimizerWrapper.setMaxIterations(100);
        Assert.assertEquals(100, optimizerWrapper.getMaxIterations());
    }

    @Test
    public void testGetMaxEvaluations() {
        // Basic test
        MultiStartUnivariateRealOptimizer optimizerWrapper = (MultiStartUnivariateRealOptimizer) optimizer;
        Assert.assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxEvaluations());

        // Test setting and getting
        optimizerWrapper.setMaxEvaluations(1000);
        Assert.assertEquals(1000, optimizerWrapper.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Basic test
        MultiStartUnivariateRealOptimizer optimizerWrapper = (MultiStartUnivariateRealOptimizer) optimizer;
        Assert.assertEquals(0, optimizerWrapper.getEvaluations());

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertGreaterThan(optimizerWrapper.getEvaluations(), 0);
    }

    @Test
    public void testGetRelativeAccuracy() {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertEquals(Double.NaN, optimizerWrapper.getRelativeAccuracy(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.setRelativeAccuracy(1e-5);
        Assert.assertEquals(1e-5, optimizer.getRelativeAccuracy(), 1e-9);
    }

    @Test
    public void testResetAbsoluteAccuracy() {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertEquals(Double.NaN, optimizerWrapper.getAbsoluteAccuracy(), 1e-9);

        optimizerWrapper.resetAbsoluteAccuracy();
        Assert.assertEquals(Double.NaN, optimizerWrapper.getAbsoluteAccuracy(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        optimizer.resetAbsoluteAccuracy();
        Assert.assertEquals(Double.NaN, optimizer.getAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testResetMaxIterations() {
        // Basic test
        MultiStartUnivariateRealOptimizer optimizerWrapper = (MultiStartUnivariateRealOptimizer) optimizer;
        Assert.assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxIterations());

        optimizerWrapper.resetMaxIterations();
        Assert.assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxIterations());

        // Test setting and getting
        optimizerWrapper.setMaxIterations(100);
        optimizerWrapper.resetMaxIterations();
        Assert.assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxIterations());
    }

    @Test
    public void testResetRelativeAccuracy() {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertEquals(Double.NaN, optimizerWrapper.getRelativeAccuracy(), 1e-9);

        optimizerWrapper.resetRelativeAccuracy();
        Assert.assertEquals(Double.NaN, optimizerWrapper.getRelativeAccuracy(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        optimizer.resetRelativeAccuracy();
        Assert.assertEquals(Double.NaN, optimizer.getRelativeAccuracy(), 1e-9);
    }

    @Test
    public void testGetOptima() throws IllegalStateException {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertArrayEquals(new double[0], optimizerWrapper.getOptima(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        double[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(new double[10], optima, 1e-9);
    }

    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        // Basic test
        UnivariateRealOptimizer optimizerWrapper = optimizer;
        Assert.assertArrayEquals(new double[0], optimizerWrapper.getOptimaValues(), 1e-9);

        // Test with MultiStartUnivariateRealOptimizer
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        double[] optimaValues = optimizer.getOptimaValues();
        Assert.assertArrayEquals(new double[10], optimaValues, 1e-9);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaWithoutOptimize() throws IllegalStateException {
        optimizer.getOptima();
    }

    @Test
    public void testOptimizeBasic() throws ConvergenceException, FunctionEvaluationException {
        // Test with MultiStartUnivariateRealOptimizer
        double optimum = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        double expected = Precision.findBestValue(function, 0.0, 10.0, 1e-5);
        Assert.assertEquals(expected, optimum, 1e-9);
    }

    @Test
    public void testOptimizeWithMultipleStarts() throws ConvergenceException, FunctionEvaluationException {
        // Test with MultiStartUnivariateRealOptimizer, multiple starts
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        double expectedMin = Precision.findBestValue(function, 0.0, 10.0, 1e-5);
        double expected = Double.POSITIVE_INFINITY;
        for (int i = 0; i < optima.length; i++) {
            if (Double.isNaN(optima[i])) {
                continue;
            }
            double currentMin = function.value(optima[i]);
            if (currentMin < expectedMin) {
                expected = currentMin;
            }
        }
        Assert.assertEquals(expectedMin, expected, 1e-9);
    }

    @Test
    public void testOptimizeWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        // Test with MultiStartUnivariateRealOptimizer, max iterations
        MultiStartUnivariateRealOptimizer optimizerWrapper = (MultiStartUnivariateRealOptimizer) optimizer;
        optimizerWrapper.setMaxIterations(1);
        double optimum = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertFalse("Expected optimization to fail with max iterations", Double.isFinite(optimum));
    }

    @Test
    public void testOptimizeWithMaxEvaluations() throws ConvergenceException, FunctionEvaluationException {
        // Test with MultiStartUnivariateRealOptimizer, max evaluations
        MultiStartUnivariateRealOptimizer optimizerWrapper = (MultiStartUnivariateRealOptimizer) optimizer;
        optimizerWrapper.setMaxEvaluations(1);
        double optimum = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertFalse("Expected optimization to fail with max evaluations", Double.isFinite(optimum));
    }
}