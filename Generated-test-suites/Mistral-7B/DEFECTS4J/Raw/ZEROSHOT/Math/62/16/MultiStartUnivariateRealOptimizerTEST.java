package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test function for optimization.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    /**
     * Underlying classical optimizer.
     */
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    /**
     * Multi-start optimizer.
     */
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    /**
     * Random generator for multi-start.
     */
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() {
        TestFunction testFunction = new TestFunction();
        optimizer = new BrentOptimizer(testFunction);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, randomGenerator);
        randomGenerator = org.apache.commons.math3.random.MersenneTwister.getDefault();
    }

    /**
     * Test optimization with minimum goal.
     *
     * @throws FunctionEvaluationException If an error occurs during optimization.
     * @throws ConvergenceException       If optimization does not converge.
     * @throws MathIllegalStateException  If optimization has not been called.
     */
    @Test
    public void testMinimize() throws FunctionEvaluationException, ConvergenceException, MathIllegalStateException {
        double min = -3.0;
        double max = 3.0;
        PointValuePair pointValuePair = multiStartOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, min, max);
        Assert.assertNotNull(pointValuePair.getPoint());
        Assert.assertEquals(LocalizedFormats.OPTIMUM_FOUND, pointValuePair.getValue(), -2.0, 1e-6);
        Assert.assertArrayEquals(getExpectedOptima(), multiStartOptimizer.getOptima());
    }

    /**
     * Test optimization with maximum goal.
     *
     * @throws FunctionEvaluationException If an error occurs during optimization.
     * @throws ConvergenceException       If optimization does not converge.
     * @throws MathIllegalStateException  If optimization has not been called.
     */
    @Test
    public void testMaximize() throws FunctionEvaluationException, ConvergenceException, MathIllegalStateException {
        double min = -3.0;
        double max = 3.0;
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, randomGenerator);
        PointValuePair pointValuePair = multiStartOptimizer.optimize(new TestFunction(), GoalType.MAXIMIZE, min, max);
        Assert.assertNotNull(pointValuePair.getPoint());
        Assert.assertEquals(LocalizedFormats.OPTIMUM_FOUND, pointValuePair.getValue(), 2.0, 1e-6);
        Assert.assertArrayEquals(getExpectedOptima(), multiStartOptimizer.getOptima());
    }

    /**
     * Test optimization with custom bounds.
     *
     * @throws FunctionEvaluationException If an error occurs during optimization.
     * @throws ConvergenceException       If optimization does not converge.
     * @throws MathIllegalStateException  If optimization has not been called.
     */
    @Test
    public void testCustomBounds() throws FunctionEvaluationException, ConvergenceException, MathIllegalStateException {
        SimpleBounds bounds = new SimpleBounds(1.0, 2.0);
        PointValuePair pointValuePair = multiStartOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, bounds.getMin(), bounds.getMax());
        Assert.assertNotNull(pointValuePair.getPoint());
        Assert.assertEquals(LocalizedFormats.OPTIMUM_FOUND, pointValuePair.getValue(), -1.0, 1e-6);
        Assert.assertArrayEquals(getExpectedOptima(), multiStartOptimizer.getOptima());
    }

    /**
     * Test optimization with initial guess.
     *
     * @throws FunctionEvaluationException If an error occurs during optimization.
     * @throws ConvergenceException       If optimization does not converge.
     * @throws MathIllegalStateException  If optimization has not been called.
     */
    @Test
    public void testInitialGuess() throws FunctionEvaluationException, ConvergenceException, MathIllegalStateException {
        double min = -3.0;
        double max = 3.0;
        InitialGuess initialGuess = new InitialGuess(1.5);
        PointValuePair pointValuePair = multiStartOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, min, max, initialGuess);
        Assert.assertNotNull(pointValuePair.getPoint());
        Assert.assertEquals(LocalizedFormats.OPTIMUM_FOUND, pointValuePair.getValue(), -1.5, 1e-6);
        Assert.assertArrayEquals(getExpectedOptima(), multiStartOptimizer.getOptima());
    }

    /**
     * Test optimization with custom point value pair function.
     *
     * @throws FunctionEvaluationException If an error occurs during optimization.
     * @throws ConvergenceException       If optimization does not converge.
     * @throws MathIllegalStateException  If optimization has not been called.
     */
    @Test
    public void testCustomPointValuePairFunction() throws FunctionEvaluationException, ConvergenceException, MathIllegalStateException {
        UnivariateRealFunction testFunction = new TestFunction();
        PointValuePairFunction pointValuePairFunction = new PointValuePairFunction() {
            @Override
            public double value(double x) {
                return testFunction.value(x) + 1.0;
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, randomGenerator);
        PointValuePair pointValuePair = multiStartOptimizer.optimize(pointValuePairFunction, GoalType.MINIMIZE, -3.0, 3.0);
        Assert.assertNotNull(pointValuePair.getPoint());
        Assert.assertEquals(LocalizedFormats.OPTIMUM_FOUND, pointValuePair.getValue(), -1.0, 1e-6);
        Assert.assertArrayEquals(getExpectedOptima(), multiStartOptimizer.getOptima());
    }

    /**
     * Test optimization with no convergence.
     *
     * @throws FunctionEvaluationException If an error occurs during optimization.
     * @throws ConvergenceException       If optimization does not converge.
     * @throws MathIllegalStateException  If optimization has not been called.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws FunctionEvaluationException, ConvergenceException, MathIllegalStateException {
        double min = -3.0;
        double max = 3.0;
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 0, randomGenerator);
        multiStartOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, min, max);
    }

    /**
     * Get expected optima.
     *
     * @return expected optima.
     */
    private UnivariateRealPointValuePair[] getExpectedOptima() {
        return new UnivariateRealPointValuePair[]{
            new UnivariateRealPointValuePair(0.0, -2.0),
            new UnivariateRealPointValuePair(1.0, -1.0),
            new UnivariateRealPointValuePair(2.0, 0.0),
            new UnivariateRealPointValuePair(1.5, 1.0),
            new UnivariateRealPointValuePair(3.0, 3.0),
            null,
            null,
            null,
            null,
            null};
    }
}