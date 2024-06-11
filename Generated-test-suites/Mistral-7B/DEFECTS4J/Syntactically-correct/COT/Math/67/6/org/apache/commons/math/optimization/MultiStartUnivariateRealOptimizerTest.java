package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        UnivariateRealOptimizer optimizer = new Minimizer(new Fibonacci());
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals("Wrong number of starts", 5, multiStartOptimizer.getStarts());
        assertEquals("Wrong optimizer", optimizer, multiStartOptimizer.getOptimizer());
    }

    /**
     * Test getters.
     */
    @org.junit.Test
    public void testGetters() {
        UnivariateRealOptimizer optimizer = new Minimizer(new Fibonacci());
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());

        assertEquals("Wrong total iterations", 0, multiStartOptimizer.getIterationCount());
        assertEquals("Wrong total evaluations", 0, multiStartOptimizer.getEvaluations());
        assertEquals("Wrong max iterations", Integer.MAX_VALUE, multiStartOptimizer.getMaxIterations());
        assertEquals("Wrong max evaluations", Integer.MAX_VALUE, multiStartOptimizer.getMaxEvaluations());

        multiStartOptimizer.setMaxIterations(10);
        multiStartOptimizer.setMaxEvaluations(100);
        assertEquals("Wrong max iterations", 10, multiStartOptimizer.getMaxIterations());
        assertEquals("Wrong max evaluations", 100, multiStartOptimizer.getMaxEvaluations());
    }

    /**
     * Test optimize method.
     */
    @org.junit.Test
    public void testOptimizeMethod() throws ConvergenceException, FunctionEvaluationException, IllegalStateException {
        UnivariateRealFunction function = new PolynomialFunction(-1, 1, 2);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 5;
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new Minimizer(function), 3, new MersenneTwister());
        double result = multiStartOptimizer.optimize(function, goalType, min, max);
        assertEquals("Wrong result", 2.0, result, 0.001);

        double[] optima = multiStartOptimizer.getOptima();
        assertEquals("Wrong number of optima", 3, optima.length);
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertEquals("Wrong number of optima values", 3, optimaValues.length);
        for (int i = 0; i < optima.length; i++) {
            assertEquals("Wrong optima, index " + i, 2.0, optima[i], 0.001);
            assertEquals("Wrong optima value, index " + i, function.value(optima[i]), optimaValues[i], 0.001);
        }
    }

    /**
     * Test optimize method with convergence exception.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testOptimizeMethodWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new Fibonacci();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new Minimizer(function), 3, new MersenneTwister());
        multiStartOptimizer.optimize(function, goalType, min, max);
    }

    /**
     * Test optimize method with function evaluation exception.
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testOptimizeMethodWithFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionWithEvaluationException();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new Minimizer(function), 3, new MersenneTwister());
        multiStartOptimizer.optimize(function, goalType, min, max);
    }

    /**
     * Test optimize method with illegal state exception.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testOptimizeMethodWithIllegalStateException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new Minimizer(new Fibonacci());
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(function, 0, new MersenneTwister());
        multiStartOptimizer.optimize(function, goalType, min, max);
    }
}