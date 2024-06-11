package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.*;
import org.apache.commons.math3.special.*;
import org.apache.commons.math3.stat.Statistics;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** The optimizer to test. */
    private MultiStartUnivariateRealOptimizer optimizer;

    /** The test function. */
    private UnivariateRealFunction function;

    /** The goal type. */
    private GoalType goalType;

    /** The minimum and maximum bounds. */
    private double min, max;

    /** The number of starts. */
    private int starts;

    /** The maximum number of iterations allowed. */
    private int maxIterations;

    /** The maximum number of evaluations allowed. */
    private int maxEvaluations;

    /** The random generator. */
    private RandomGenerator generator;

    /** The optima and their values. */
    private double[] optima, optimaValues;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        function = new AckleyFunction();
        goalType = GoalType.MINIMIZE;
        min = -5.12;
        max = 5.12;
        starts = 10;
        maxIterations = 100;
        maxEvaluations = 500;
        generator = new UniformRealDistribution(0, 1);
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexSearch(new SimplexInitialGuess(2)), starts, generator);
        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setMaximalIterationCount(maxIterations);
    }

    /**
     * Tests multi-start minimization with a test function.
     */
    @Test
    public void testMultiStartMinimization() {
        try {
            optima = optimizer.optimize(function, goalType, min, max).getOptima();
            optimaValues = optimizer.optimize(function, goalType, min, max).getOptimaValues();
            assertEquals(starts, optima.length);
            assertEquals(starts, optimaValues.length);
            double[] expected = new double[starts];
            expected[0] = 0.0;
            expected[1] = 0.5;
            expected[2] = -0.236028544;
            expected[3] = 0.236028544;
            expected[4] = -0.141371603;
            expected[5] = 0.141371603;
            expected[6] = -0.1;
            expected[7] = 0.1;
            expected[8] = 0.123456789;
            expected[9] = 0.87654321;
            for (int i = 0; i < starts; ++i) {
                assertEquals(expected[i], optima[i], 1e-10);
                assertEquals(expected[i], optimaValues[i], 1e-10);
            }
            assertEquals(expected[0], optimizer.optimize(function, goalType, min, max), 1e-10);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Tests multi-start maximization with a test function.
     */
    @Test
    public void testMultiStartMaximization() {
        try {
            goalType = GoalType.MAXIMIZE;
            optima = optimizer.optimize(function, goalType, min, max).getOptima();
            optimaValues = optimizer.optimize(function, goalType, min, max).getOptimaValues();
            assertEquals(starts, optima.length);
            assertEquals(starts, optimaValues.length);
            double[] expected = new double[starts];
            expected[0] = 0.0;
            expected[1] = 0.5;
            expected[2] = 0.833261522;
            expected[3] = 0.236028544;
            expected[4] = 0.141371603;
            expected[5] = -0.141371603;
            expected[6] = 0.1;
            expected[7] = -0.1;
            expected[8] = 0.123456789;
            expected[9] = -0.87654321;
            for (int i = 0; i < starts; ++i) {
                assertEquals(expected[i], optima[i], 1e-10);
                assertEquals(expected[i], optimaValues[i], 1e-10);
            }
            assertEquals(expected[0], optimizer.optimize(function, goalType, min, max), 1e-10);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Tests convergence exception.
     */
    @Test(expected = OptimizationException.class)
    public void testConvergenceException() {
        goalType = GoalType.MINIMIZE;
        min = -1000;
        max = 1000;
        optimizer = new MultiStartUnivariateRealOptimizer(new BracketedBrentMethod(new BracketingBacktrackingInitialGuess(2)), starts, generator);
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests function evaluation exception.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        goalType = GoalType.MINIMIZE;
        min = -1000;
        max = 1000;
        function = new ThrowsFunction();
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexSearch(new SimplexInitialGuess(2)), starts, generator);
        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests math runtime exception.
     */
    @Test(expected = MathRuntimeException.class)
    public void testMathRuntimeException() {
        goalType = GoalType.MINIMIZE;
        min = -1000;
        max = 1000;
        function = new DivideByZeroFunction();
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexSearch(new SimplexInitialGuess(2)), starts, generator);
        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests null optimizer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullOptimizer() {
        optimizer = null;
        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexSearch(new SimplexInitialGuess(2)), starts, generator);
        function = null;
        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests null goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() {
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexSearch(new SimplexInitialGuess(2)), starts, generator);
        goalType = null;
        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests null bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullBounds() {
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimplexSearch(new SimplexInitialGuess(2)), starts, generator);
        min = null;
        max = null;
        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests test function.
     */
    private static class AckleyFunction implements UnivariateRealFunction {

        /**
         * Returns the value of the function for the given argument.
         * @param x the argument
         * @return the function value
         */
        public double value(double x) {
            final double a = 20.0;
            final double b = 0.01;
            return a * Math.pow( Math.abs(Math.sin(Math.PI * x)), 2)
                + a * Math.pow(1.0 - Math.cos(Math.PI * x / 2.0), 2)
                + b;
        }

        /**
         * Returns the derivative of the function for the given argument.
         * @param x the argument
         * @return the derivative
         */
        public double derivative(double x) {
            final double a = 40.0 * Math.pow(Math.abs(Math.sin(Math.PI * x)), 2)
                * Math.cos(Math.PI * x);
            final double b = 2.0 * (1.0 - Math.cos(Math.PI * x / 2.0))
                * Math.PI / 2.0;
            return a - b;
        }

    }

    /**
     * Returns the mean of the given double array.
     * @param values the array
     * @return the mean
     */
    private static double mean(double[] values) {
        return Statistics.mean(values);
    }

}