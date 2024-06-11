package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MaxEval;
import org.apache.commons.math.optimization.SimpleGoalType;
import org.apache.commons.math.optimization.SimpleUnivariateFunction;
import org.apache.commons.math.optimization.UnivariateOptimizer;
import org.apache.commons.math.optimization.nonlinear.GoalType;
import org.junit.Test;

import java.util.Random;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() throws Exception {
        UnivariateRealFunction testFunction = new SimpleUnivariateFunction("Test Function", x -> Math.pow(x, 2));
        GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE);
        double min = 0.0;
        double max = 10.0;
        int maxIterations = 50;
        int maxEvaluations = 100;
        int starts = 5;
        RandomGenerator generator = new Random();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciSearchOptimizer(1.0E-6, maxIterations, maxEvaluations),
                starts,
                generator);

        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals(starts, optima.length);
        assertEquals(starts, optimaValues.length);

        for (int i = 0; i < starts; i++) {
            assertTrue(Double.isNaN(optima[i]) || Double.isFinite(optima[i]) || optima[i] == min);
            assertTrue(Double.isNaN(optimaValues[i]) || Double.isFinite(optimaValues[i]));
        }

        double bestOptimum = optimizer.optimize(testFunction, goalType, min, max);

        for (int i = 0; i < starts; i++) {
            assertEquals(bestOptimum, optima[i], 1e-6);
            assertEquals(testFunction.value(optima[i]), optimaValues[i], 1e-6);
        }

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();

        assertArrayEquals(new double[]{bestOptimum}, optima, 1e-6);
        assertArrayEquals(new double[]{testFunction.value(bestOptimum)}, optimaValues, 1e-6);

        assertNull(optimizer.getOptima());
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeBeforeInitialization() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                null, 3, new Random());
        optimizer.optimize(new SimpleUnivariateFunction("Test Function", x -> Math.pow(x, 2)),
                GoalType.MINIMIZE, 0.0, 10.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        UnivariateRealFunction testFunction = new SimpleUnivariateFunction("Test Function", x -> {
            throw new ConvergenceException("Test Convergence Exception");
        });
        GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE);
        double min = 0.0;
        double max = 10.0;
        int maxIterations = 50;
        int maxEvaluations = 100;
        int starts = 5;
        RandomGenerator generator = new Random();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciSearchOptimizer(1.0E-6, maxIterations, maxEvaluations),
                starts,
                generator);

        optimizer.optimize(testFunction, goalType, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction testFunction = new SimpleUnivariateFunction("Test Function", x -> {
            throw new FunctionEvaluationException("Test Function Evaluation Exception");
        });
        GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE);
        double min = 0.0;
        double max = 10.0;
        int maxIterations = 50;
        int maxEvaluations = 100;
        int starts = 5;
        RandomGenerator generator = new Random();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciSearchOptimizer(1.0E-6, maxIterations, maxEvaluations),
                starts,
                generator);

        optimizer.optimize(testFunction, goalType, min, max);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalStateException() throws Exception {
        UnivariateRealFunction testFunction = new SimpleUnivariateFunction("Test Function", x -> Math.pow(x, 2));
        GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE);
        double min = 0.0;
        double max = 10.0;
        int maxIterations = 50;
        int maxEvaluations = 100;
        int starts = -1;
        RandomGenerator generator = new Random();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciSearchOptimizer(1.0E-6, maxIterations, maxEvaluations),
                starts,
                generator);

        optimizer.optimize(testFunction, goalType, min, max);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalStateExceptionNullOptima() throws Exception {
        UnivariateRealFunction testFunction = new SimpleUnivariateFunction("Test Function", x -> Math.pow(x, 2));
        GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE);
        double min = 0.0;
        double max = 10.0;
        int maxIterations = 50;
        int maxEvaluations = 100;
        int starts = 5;
        RandomGenerator generator = new Random();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciSearchOptimizer(1.0E-6, maxIterations, maxEvaluations),
                starts,
                generator);

        optimizer.optimize(testFunction, goalType, min, max);
        optimizer.setOptima(null);
        optimizer.optimize(testFunction, goalType, min, max);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalStateExceptionNullOptimaValues() throws Exception {
        UnivariateRealFunction testFunction = new SimpleUnivariateFunction("Test Function", x -> Math.pow(x, 2));
        GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE);
        double min = 0.0;
        double max = 10.0;
        int maxIterations = 50;
        int maxEvaluations = 100;
        int starts = 5;
        RandomGenerator generator = new Random();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciSearchOptimizer(1.0E-6, maxIterations, maxEvaluations),
                starts,
                generator);

        optimizer.optimize(testFunction, goalType, min, max);
        optimizer.setOptimaValues(null);
        optimizer.optimize(testFunction, goalType, min, max);
    }
}

class SimpleUnivariateFunction implements UnivariateRealFunction {
    private String name;
    private Function1D func;

    public SimpleUnivariateFunction(String name, Function1D func) {
        this.name = name;
        this.func = func;
    }

    @Override
    public double value(double x) {
        return func.applyAsDouble(x);
    }

    @Override
    public String toString() {
        return String.format("%s(x)", name);
    }
}

class SimpleUnivariateFibonacciSearchOptimizer implements UnivariateRealOptimizer {
    private double absoluteAccuracy;
    private int maxIterations;
    private int maxEvaluations;

    public SimpleUnivariateFibonacciSearchOptimizer(double absoluteAccuracy, int maxIterations,
                                                    int maxEvaluations) {
        this.absoluteAccuracy = absoluteAccuracy;
        this.maxIterations = maxIterations;
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public double getFunctionValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAbsoluteAccuracy() {
        return absoluteAccuracy;
    }

    @Override
    public int getIterationCount() {
        return 0;
    }

    @Override
    public int getMaximalIterationCount() {
        return maxIterations;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public double getRelativeAccuracy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetAbsoluteAccuracy() {
        // empty
    }

    @Override
    public void resetMaximalIterationCount() {
        // empty
    }

    @Override
    public void resetRelativeAccuracy() {
        // empty
    }

    @Override
    public void setAbsoluteAccuracy(double accuracy) {
        this.absoluteAccuracy = accuracy;
    }

    @Override
    public void setMaximalIterationCount(int count) {
        this.maxIterations = count;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public void setRelativeAccuracy(double accuracy) {
        // empty
    }

    @Override
    public void minimize(UnivariateRealFunction function, double lowerBound,
                         double upperBound, GoalType goalType, double tolerance, int maxIterations,
                         int maxEvaluations, double stepFactor, RandomGenerator randomGenerator)
            throws ConvergenceException, FunctionEvaluationException {
        // empty
    }

    @Override
    public void minimize(UnivariateRealFunction function, double lowerBound, double upperBound,
                         GoalType goalType, double tolerance)
            throws ConvergenceException, FunctionEvaluationException {
        // empty
    }

    @Override
    public void minimize(UnivariateRealFunction function, double lowerBound, double upperBound, GoalType goalType)
            throws ConvergenceException, FunctionEvaluationException {
        // empty
    }

    @Override
    public void maximize(UnivariateRealFunction function, double lowerBound, double upperBound,
                         GoalType goalType, double tolerance)
            throws ConvergenceException, FunctionEvaluationException {
        // empty
    }

    @Override
    public void maximize(UnivariateRealFunction function, double lowerBound, double upperBound, GoalType goalType)
            throws ConvergenceException, FunctionEvaluationException {
        // empty
    }

    @Override
    public void maximize(UnivariateRealFunction function, double lowerBound, double upperBound)
            throws ConvergenceException, FunctionEvaluationException {
        // empty
    }
}