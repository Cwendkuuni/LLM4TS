package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergentIterationException;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.PowellPointDistribution2;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer class
 */
public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testConstructor() {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, generator);
        assertEquals(5, optimizerInstance.getStarts());
        assertEquals(optimizer, optimizerInstance.getOptimizer());
        assertEquals(generator, optimizerInstance.getGenerator());
        assertFalse(optimizerInstance.isOptimized());
    }

    @Test
    public void testGetters() throws ConvergenceException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, generator);
        optimizerInstance.setMaxIterations(10);
        optimizerInstance.setMaxEvaluations(15);
        optimizerInstance.optimize(new PolynomialFunction(2), MAXIMIZE, -1.0, 1.0);
        assertEquals(10, optimizerInstance.getMaxIterations());
        assertEquals(15, optimizerInstance.getMaxEvaluations());
        assertEquals(optimizerInstance.getIterationCount(), optimizerInstance.getTotalIterations());
        assertEquals(optimizerInstance.getEvaluations(), optimizerInstance.getTotalEvaluations());
        assertEquals(optimizerInstance.getOptimaValues().length, optimizerInstance.getOptima().length);
    }

    @Test
    public void testOptimize() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateFunction function = new PolynomialFunction(0.5);
        double optimizedValue = optimizerInstance.optimize(function, goalType, min, max);
        assertEquals(-0.5, optimizedValue, 0.001);
        verify(optimizer, times(5)).optimize(any(UnivariateFunction.class), goalType, any(SimpleBounds.class));
    }

    @Test
    public void testOptimizeMaximization() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MAXIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateFunction function = new PolynomialFunction(2);
        double optimizedValue = optimizerInstance.optimize(function, goalType, min, max);
        assertEquals(2.0, optimizedValue, 0.001);
        verify(optimizer, times(5)).optimize(any(UnivariateFunction.class), goalType, any(SimpleBounds.class));
    }

    @Test
    public void testOptimizeExceptionHandling() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateFunction function = new PolynomialFunction(-1.0) {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test FunctionEvaluationException");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> optimizerInstance.optimize(function, goalType, min, max));
    }

    @Test
    public void testOptimizeConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateFunction function = new PolynomialFunction(0.5) {
            @Override
            public double value(double x) {
                if (x > 0) {
                    throw new ConvergenceException("Test ConvergenceException");
                }
                return 0.5;
            }
        };
        assertThrows(ConvergenceException.class, () -> optimizerInstance.optimize(function, goalType, min, max));
    }

    @Test
    public void testOptimizeNonConvergentIterationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateFunction function = new PolynomialFunction(0.5) {
            @Override
            public double value(double x) {
                if (Math.abs(x - 1.0) < 0.01) {
                    throw new NonConvergentIterationException("Test NonConvergentIterationException");
                }
                return 0.5;
            }
        };
        assertThrows(NonConvergentIterationException.class, () -> optimizerInstance.optimize(function, goalType, min, max));
    }

    @Test
    public void testOptimizeMaxIterationsReached() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateFunction function = new PolynomialFunction(0.5);
        optimizerInstance.setMaxIterations(0);
        assertThrows(IllegalArgumentException.class, () -> optimizerInstance.optimize(function, goalType, min, max));
    }

    @Test
    public void testOptimizeMaxEvaluationsReached() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        UnivariateFunction function = new PolynomialFunction(0.5);
        optimizerInstance.setMaxEvaluations(0);
        assertThrows(IllegalArgumentException.class, () -> optimizerInstance.optimize(function, goalType, min, max));
    }

    @Test
    public void testGetOptima() throws IllegalStateException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        double[] optima = optimizerInstance.getOptima();
        assertArrayEquals(new double[5], optima);
    }

    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        double[] optimaValues = optimizerInstance.getOptimaValues();
        assertArrayEquals(new double[5], optimaValues);
    }

    @Test
    public void testSetGoalType() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        GoalType goalType = MINIMIZE;
        optimizerInstance.optimize(new PolynomialFunction(0.5), goalType, -1.0, 1.0);
        GoalType goalType2 = MAXIMIZE;
        optimizerInstance.setGoalType(goalType2);
        optimizerInstance.optimize(new PolynomialFunction(2), goalType2, -1.0, 1.0);
        assertFalse(optimizerInstance.isOptimized());
    }

    @Test
    public void testSetMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerInstance.setMaxIterations(10);
        optimizerInstance.optimize(new PolynomialFunction(0.5), MINIMIZE, -1.0, 1.0);
        assertEquals(10, optimizerInstance.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerInstance.setMaxEvaluations(15);
        optimizerInstance.optimize(new PolynomialFunction(0.5), MINIMIZE, -1.0, 1.0);
        assertEquals(15, optimizerInstance.getMaxEvaluations());
    }

    @Test
    public void testSetGenerator() {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        RandomGenerator generator = new MersenneTwister();
        optimizerInstance.setGenerator(generator);
        assertEquals(generator, optimizerInstance.getGenerator());
    }

    @Test
    public void testSetStarts() {
        UnivariateRealOptimizer optimizer = mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer optimizerInstance = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerInstance.setStarts(10);
        assertEquals(10, optimizerInstance.getStarts());
    }
}