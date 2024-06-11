package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}
 */
public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-5;
    private static final double MIN = -10;
    private static final double MAX = 10;

    /** Optimizer to be wrapped by MultiStartUnivariateRealOptimizer */
    private UnivariateOptimizer<UnivariateRealFunction> optimizer;

    /** Multi-start optimizer to be tested */
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;

    /** Test function */
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        optimizer = new FibonacciOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new TestRandomGenerator());
        testFunction = new TestFunction();
    }

    /**
     * Test that MultiStartUnivariateRealOptimizer returns the correct result
     * when minimizing a function.
     *
     * @throws FunctionEvaluationException if an error occurs during the optimization
     * process.
     * @throws ConvergenceException       if the optimization process does not converge.
     */
    @Test
    public void testMinimize() throws FunctionEvaluationException, ConvergenceException {
        PointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, MIN, MAX);
        Assert.assertEquals(1.61803398875, result.getPoint(), EPSILON);
        Assert.assertEquals(-2.1316220408081, result.getValue(), EPSILON);
    }

    /**
     * Test that MultiStartUnivariateRealOptimizer returns the correct result
     * when maximizing a function.
     *
     * @throws FunctionEvaluationException if an error occurs during the optimization
     * process.
     * @throws ConvergenceException       if the optimization process does not converge.
     */
    @Test
    public void testMaximize() throws FunctionEvaluationException, ConvergenceException {
        PointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MAXIMIZE, MIN, MAX);
        Assert.assertEquals(-1.61803398875, result.getPoint(), EPSILON);
        Assert.assertEquals(2.1316220408081, result.getValue(), EPSILON);
    }

    /**
     * Test that MultiStartUnivariateRealOptimizer correctly handles the case
     * with no convergence.
     *
     * @throws FunctionEvaluationException if an error occurs during the optimization
     * process.
     * @throws ConvergenceException       if the optimization process does not converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws FunctionEvaluationException, ConvergenceException {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 1, new TestRandomGenerator());
        PointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, MIN, MAX);
        Assert.assertNull(result);
    }

    /**
     * Test that MultiStartUnivariateRealOptimizer correctly returns all the optima
     * when the optimization process converges multiple times.
     *
     * @throws FunctionEvaluationException if an error occurs during the optimization
     * process.
     * @throws ConvergenceException       if the optimization process does not converge.
     */
    @Test
    public void testMultipleOptima() throws FunctionEvaluationException, ConvergenceException {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new TestRandomGenerator());
        PointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertArrayEquals(new double[]{1.61803398875, 3.23606797749, 5.38903602199},
                Arrays.stream(optima).mapToDouble(PointValuePair::getPoint).toArray(), EPSILON);
    }
}

class TestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(Math.E, -Math.pow(x, 2));
    }
}

class TestRandomGenerator implements RandomGenerator {
    @Override
    public double nextDouble() {
        return Math.random();
    }

    @Override
    public double nextGaussian() {
        return Math.randomGauss(0, 1);
    }
}