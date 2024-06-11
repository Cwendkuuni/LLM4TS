package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.MultistartOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private RandomGenerator randomGenerator;

    @Before
    public void setup() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }
        };

        optimizer = new BrentOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        randomGenerator = new MersenneTwister();
    }

    @Test
    public void testFunctionality() {
        double optimum = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        Assert.assertEquals(1.0, optimum, 1e-6);
    }

    @Test
    public void testOptima() {
        double[] optima = multiStartOptimizer.getOptima();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, optima, 1e-6);

        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, optimaValues, 1e-6);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoOptimumComputedYet() {
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testMaxIterations() {
        MultiStartUnivariateRealOptimizer optimizerWithLimitedIterations = new MultiStartUnivariateRealOptimizer(optimizer, 10, randomGenerator);
        optimizerWithLimitedIterations.setMaximalIterationCount(10);

        try {
            optimizerWithLimitedIterations.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            Assert.fail();
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testMaxEvaluations() {
        MultiStartUnivariateRealOptimizer optimizerWithLimitedEvaluations = new MultiStartUnivariateRealOptimizer(optimizer, 10, randomGenerator);
        optimizerWithLimitedEvaluations.setMaxEvaluations(100);

        try {
            for (int i = 0; i < 15; i++) {
                optimizerWithLimitedEvaluations.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            }
            Assert.fail();
        } catch (MaxEval e) {
            // expected
        }
    }

    @Test
    public void testConvergenceException() {
        MultiStartUnivariateRealOptimizer optimizerWithLimitedIterations = new MultiStartUnivariateRealOptimizer(optimizer, 10, randomGenerator);
        optimizerWithLimitedIterations.setMaximalIterationCount(1);

        try {
            optimizerWithLimitedIterations.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            Assert.fail();
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testFunctionEvaluationException() {
        MultiStartUnivariateRealOptimizer optimizerWithLimitedIterations = new MultiStartUnivariateRealOptimizer(optimizer, 10, randomGenerator);
        UnivariateRealFunction functionWithException = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test Exception");
            }
        };

        try {
            optimizerWithLimitedIterations.optimize(functionWithException, GoalType.MINIMIZE, -5.0, 5.0);
            Assert.fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }
}