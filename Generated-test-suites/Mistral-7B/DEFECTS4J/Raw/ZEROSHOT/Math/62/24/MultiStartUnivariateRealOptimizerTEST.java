package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;

import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);

        assertNotNull(optimizerWrapper);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setConvergenceChecker(ConvergenceChecker)}.
     */
    @org.junit.Test
    public void testSetConvergenceChecker() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);
        optimizerWrapper.setConvergenceChecker(convergenceChecker);
        assertSame(convergenceChecker, optimizerWrapper.getConvergenceChecker());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getConvergenceChecker()}.
     */
    @org.junit.Test
    public void testGetConvergenceChecker() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);
        optimizerWrapper.setConvergenceChecker(convergenceChecker);

        ConvergenceChecker<UnivariateRealPointValuePair> result = optimizerWrapper.getConvergenceChecker();
        assertSame(convergenceChecker, result);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        int maxEvaluations = 100;
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);
        optimizerWrapper.setMaxEvaluations(maxEvaluations);

        int result = optimizerWrapper.getMaxEvaluations();
        assertEquals(maxEvaluations, result);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        int maxEvaluations = 100;
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);
        optimizerWrapper.setMaxEvaluations(maxEvaluations);

        int result = optimizerWrapper.getEvaluations();
        assertEquals(0, result);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)}.
     */
    @org.junit.Test
    public void testSetMaxEvaluations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        int maxEvaluations = 100;
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);

        optimizerWrapper.setMaxEvaluations(maxEvaluations);
        optimizerWrapper.setMaxEvaluations(maxEvaluations);

        int result = optimizerWrapper.getMaxEvaluations();
        assertEquals(maxEvaluations, result);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @org.junit.Test
    public void testGetOptima() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        int maxEvaluations = 100;
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);
        optimizerWrapper.setMaxEvaluations(maxEvaluations);

        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        UnivariateRealPointValuePair[] optima = optimizerWrapper.optimize(function, GoalType.MINIMIZE, 0, 5).getOptima();
        UnivariateRealPointValuePair[] result = optimizerWrapper.getOptima();

        assertArrayEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i].getPoint().doubleValue(), 1e-12);
            assertNull(result[i].getValue());
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @org.junit.Test
    public void testOptimize() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        int maxEvaluations = 100;
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);
        optimizerWrapper.setMaxEvaluations(maxEvaluations);

        double min = 0;
        double max = 5;
        GoalType goal = GoalType.MINIMIZE;

        UnivariateRealPointValuePair result = optimizerWrapper.optimize(function, goal, min, max);

        assertEquals(0.0, result.getPoint().doubleValue(), 1e-12);
        assertEquals(0.0, result.getValue().doubleValue(), 1e-12);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @org.junit.Test
    public void testOptimizeWithStartValue() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer(function);
        int maxEvaluations = 100;
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwister();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, generator);
        optimizerWrapper.setMaxEvaluations(maxEvaluations);

        double min = 0;
        double max = 5;
        GoalType goal = GoalType.MINIMIZE;
        double startValue = 3.0;

        UnivariateRealPointValuePair result = optimizerWrapper.optimize(function, goal, min, max, startValue);

        assertEquals(2.0, result.getPoint().doubleValue(), 1e-12);
        assertEquals(4.0, result.getValue().doubleValue(), 1e-12);
    }

}