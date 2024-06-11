package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialGuidance.UniformInitialGuess;
import org.apache.commons.math3.optim.nonlinear.simple.QuadraticFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer_constructor() {
        UnivariateRealOptimizer optimizer = new NelderMeadSimplex();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new UniformInitialGuess(0.0, 1.0));
        Assert.assertEquals(multiStartOptimizer.getStarts(), 5);
    }

    @Test
    public void testMultiStartUnivariateRealOptimizer_getters() {
        UnivariateRealOptimizer optimizer = new NelderMeadSimplex();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new UniformInitialGuess(0.0, 1.0));

        Assert.assertEquals(multiStartOptimizer.getIterationCount(), 0);
        Assert.assertEquals(multiStartOptimizer.getMaxIterations(), Integer.MAX_VALUE);
        Assert.assertEquals(multiStartOptimizer.getMaxEvaluations(), Integer.MAX_VALUE);
        Assert.assertEquals(multiStartOptimizer.getEvaluations(), 0);

        multiStartOptimizer.setMaxIterations(10);
        Assert.assertEquals(multiStartOptimizer.getMaxIterations(), 10);

        multiStartOptimizer.setMaxEvaluations(20);
        Assert.assertEquals(multiStartOptimizer.getMaxEvaluations(), 20);
    }

    @Test
    public void testMultiStartUnivariateRealOptimizer_optimize() throws OptimizationException {
        UnivariateRealFunction function = new QuadraticFunction(1.0);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearch(), 10, new UniformInitialGuess(0.0, 1.0));
        PointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);

        Assert.assertFalse(Double.isNaN(result.getPoint()));
        Assert.assertEquals(multiStartOptimizer.getIterationCount(), 10, 0.0);
        Assert.assertEquals(multiStartOptimizer.getEvaluations(), multiStartOptimizer.getMaxEvaluations(), 0.0);

        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();

        Assert.assertEquals(optima.length, 10);
        Assert.assertEquals(optimaValues.length, 10);
        Assert.assertFalse(Arrays.stream(optima).anyMatch(d -> Double.isNaN(d)));
        Assert.assertFalse(Arrays.stream(optimaValues).anyMatch(d -> Double.isNaN(d)));

        Assert.assertTrue(Arrays.stream(optima).dropLast(1).allMatch(prev -> prev <= optima[0]));
        Assert.assertTrue(Arrays.stream(optimaValues).dropLast(1).allMatch(prev -> prev >= optimaValues[0]));
    }

    @Test(expected = IllegalStateException.class)
    public void testMultiStartUnivariateRealOptimizer_optimize_notCalled() throws OptimizationException {
        UnivariateRealFunction function = new QuadraticFunction(1.0);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearch(), 10, new UniformInitialGuess(0.0, 1.0));
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        multiStartOptimizer.getOptima();
    }
}