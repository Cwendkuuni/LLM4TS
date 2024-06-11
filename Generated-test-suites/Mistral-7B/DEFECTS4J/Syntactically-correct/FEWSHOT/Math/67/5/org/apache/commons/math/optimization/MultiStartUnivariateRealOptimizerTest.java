package org.apache.commons.cli.optimization; // make sure to import all necessary classes

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest {

    private final UnivariateRealFunction function;
    private final GoalType goalType;
    private final double min;
    private final double max;
    private final int starts;
    private final RandomGenerator generator;

    public MultiStartUnivariateRealOptimizerTest(UnivariateRealFunction function,
                                                GoalType goalType,
                                                double min,
                                                double max,
                                                int starts,
                                                RandomGenerator generator) {
        this.function = function;
        this.goalType = goalType;
        this.min = min;
        this.max = max;
        this.starts = starts;
        this.generator = generator;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new SimpleFunction(), GoalType.MINIMIZE, 0.0, 1.0, 3, new TestRandomGenerator()},
                {new SimpleFunction(), GoalType.MAXIMIZE, 0.0, 1.0, 3, new TestRandomGenerator()},
                {new SimpleFunction(), GoalType.MINIMIZE, -1.0, 1.0, 5, new TestRandomGenerator()},
                {new SimpleFunction(), GoalType.MAXIMIZE, -1.0, 1.0, 5, new TestRandomGenerator()},
                {new SimpleFunction(), GoalType.MINIMIZE, 0.0, 2.0, 5, new TestRandomGenerator()},
                {new AbsFunction(), GoalType.MINIMIZE, 0.0, 1.0, 3, new TestRandomGenerator()},
                {new AbsFunction(), GoalType.MAXIMIZE, 0.0, 1.0, 3, new TestRandomGenerator()},
                {new AbsFunction(), GoalType.MINIMIZE, -1.0, 1.0, 5, new TestRandomGenerator()},
                {new AbsFunction(), GoalType.MAXIMIZE, -1.0, 1.0, 5, new TestRandomGenerator()},
                {new AbsFunction(), GoalType.MINIMIZE, 0.0, 2.0, 5, new TestRandomGenerator()}
        });
    }

    @Test
    public void testOptimize() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new OptimizingFunction(function, goalType),
                starts,
                generator);
        double optimum = optimizer.optimize(function, goalType, min, max);

        if (goalType == GoalType.MINIMIZE) {
            assertEquals(min, optimum, 0.001);
        } else {
            assertEquals(max, optimum, 0.001);
        }

        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals(starts, optima.length);
        assertEquals(starts, optimaValues.length);

        for (int i = 0; i < starts; i++) {
            if (Double.isNaN(optima[i])) {
                fail("Optimum not found in run " + i);
            }
            if (goalType == GoalType.MINIMIZE) {
                assertEquals(min, optima[i], 0.001);
            } else {
                assertEquals(max, optima[i], 0.001);
            }
            assertEquals(goalType == GoalType.MINIMIZE ? min : max, optimaValues[i], 0.001);
        }
    }
}

interface UnivariateRealFunction {
    double value(double x) throws FunctionEvaluationException;
}

class SimpleFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return x * x;
    }
}

class AbsFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.abs(x);
    }
}

class OptimizingFunction implements UnivariateRealOptimizer {
    private final UnivariateRealFunction function;
    private final GoalType goalType;

    public OptimizingFunction(UnivariateRealFunction function, GoalType goalType) {
        this.function = function;
        this.goalType = goalType;
    }

    @Override
    public double getFunctionValue() {
        return function.value(getResult());
    }

    @Override
    public double getResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAbsoluteAccuracy() {
        return 0.001;
    }

    @Override
    public int getIterationCount() {
        return 0;
    }

    @Override
    public int getMaximalIterationCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEvaluations() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public double getRelativeAccuracy() {
        return 0.0;
    }

    @Override
    public void resetAbsoluteAccuracy() {
        // No-op
    }

    @Override
    public void resetMaximalIterationCount() {
        // No-op
    }

    @Override
    public void resetRelativeAccuracy() {
        // No-op
    }

    @Override
    public void setAbsoluteAccuracy(double accuracy) {
        // No-op
    }

    @Override
    public void setMaximalIterationCount(int count) {
        // No-op
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        // No-op
    }

    @Override
    public void setRelativeAccuracy(double accuracy) {
        // No-op
    }

    @Override
    public void optimize(UnivariateRealFunction f, GoalType goalType,
                         double min, double max) throws ConvergenceException,
            FunctionEvaluationException {
        double x = min;
        double bestValue = function.value(x);
        double bestX = x;

        for (int i = 0; i < 1000; i++) {
            double newX = x + Math.random() * (max - min) + min;
            double newValue = function.value(newX);

            if (goalType == GoalType.MINIMIZE) {
                if (newValue < bestValue) {
                    bestValue = newValue;
                    bestX = newX;
                }
            } else {
                if (newValue > bestValue) {
                    bestValue = newValue;
                    bestX = newX;
                }
            }

            x = newX;
        }

        setResult(bestX);
    }
}

class TestRandomGenerator implements RandomGenerator {
    @Override
    public double nextDouble() {
        return Math.random();
    }
}

enum GoalType {
    MINIMIZE,
    MAXIMIZE
}

class OptimizationException extends Exception {
    public OptimizationException(String message, int numStarts) {
        super(message + " (" + numStarts + " starts)");
    }
}