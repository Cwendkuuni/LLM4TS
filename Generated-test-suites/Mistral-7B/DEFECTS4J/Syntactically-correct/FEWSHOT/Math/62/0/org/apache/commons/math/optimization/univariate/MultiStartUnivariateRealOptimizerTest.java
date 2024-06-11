package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.BesselFunctions;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci11;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingMethodOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartBesselFunction() {
        UnivariateFunction function = new BesselFunctions().besselJ(0);
        GoalType goalType = GoalType.MINIMIZE;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        int maxEvaluations = 50;
        int starts = 3;

        RandomGenerator rg = new MersenneTwister();

        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new SimpleBoundingMethod(), new Fibonacci11()),
                starts, rg);

        optimizer.setMaxEvaluations(maxEvaluations);

        UnivariateRealPointValuePair[] result = optimizer.optimize(function, goalType, lowerBound, upperBound, null);

        PointValuePair best = result[0];
        double bestValue = best.getValue();

        assertNull(result[1]); // check for null to ensure optimization did not fail
        assertTrue(best.isDefined());
        assertArrayEquals(new double[] { bestValue, best.getPoint().getValue() },
                new double[] { 0.0, 0.0 }, 1e-16);
    }

    @Test
    public void testMultiStartFibonacciFunction() {
        ObjectiveFunction function = new Fibonacci11();
        GoalType goalType = GoalType.MINIMIZE;
        double lowerBound = 0.0;
        double upperBound = 10.0;
        int maxEvaluations = 50;
        int starts = 3;

        RandomGenerator rg = new MersenneTwister();

        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new SimpleBoundingMethod(), new Fibonacci11()),
                starts, rg);

        optimizer.setMaxEvaluations(maxEvaluations);

        UnivariateRealPointValuePair[] result = optimizer.optimize(function, goalType, lowerBound, upperBound, null);

        PointValuePair best = result[0];
        double bestValue = best.getValue();

        assertNull(result[1]); // check for null to ensure optimization did not fail
        assertTrue(best.isDefined());
        assertArrayEquals(new double[] { bestValue, best.getPoint().getValue() },
                new double[] { 5.517554393410245, 5.5 }, 1e-16);
    }

    @Test(expected = ConvergenceException.class)
    public void testMultiStartNonConvergingFunction() {
        ObjectiveFunction function = new FunctionUtils.Sine();
        GoalType goalType = GoalType.MINIMIZE;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        int maxEvaluations = 50;
        int starts = 3;

        RandomGenerator rg = new MersenneTwister();

        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new SimpleBoundingMethod(), new FunctionUtils.Sine()),
                starts, rg);

        optimizer.setMaxEvaluations(maxEvaluations);

        optimizer.optimize(function, goalType, lowerBound, upperBound, null);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testMissingOptimizeCall() {
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new SimpleBoundingMethod(), new FunctionUtils.Sine()), 2, null);

        UnivariateRealPointValuePair[] result = optimizer.getOptima();
    }
}