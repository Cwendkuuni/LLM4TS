package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.SimpleBounds;
import org.apache.commons.math.optimization.SimpleUnivariateFunction;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizer() throws Exception {
        UnivariateRealFunction func = new SimpleUnivariateFunction("x^2", x -> x * x);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciOptimizer(10, 0.001), 5, new java.util.Random());

        double[] results = optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);

        assertEquals(5, results.length);
        for (double result : results) {
            assertEquals(Math.sqrt(2.0), result, 0.001);
        }
    }

    @Test
    public void testMultiStartOptimizerNoConvergence() throws Exception {
        UnivariateRealFunction func = new SimpleUnivariateFunction("|x|", x -> Math.abs(x));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciOptimizer(10, 0.001), 5, new java.util.Random());

        assertThrows(OptimizationException.class, () -> {
            optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);
        });
    }

    @Test
    public void testGetOptima() throws Exception {
        UnivariateRealFunction func = new SimpleUnivariateFunction("x^2", x -> x * x);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciOptimizer(10, 0.001), 5, new java.util.Random());

        double[] results = optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);

        double[] optima = optimizer.getOptima();
        assertArrayEquals(results, optima);
    }

    @Test
    public void testGetOptimaValues() throws Exception {
        UnivariateRealFunction func = new SimpleUnivariateFunction("x^2", x -> x * x);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciOptimizer(10, 0.001), 5, new java.util.Random());

        double[] results = optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);

        double[] optimaValues = optimizer.getOptimaValues();
        for (int i = 0; i < results.length; i++) {
            assertEquals(Math.pow(Math.sqrt(2.0), 2.0), optimaValues[i], 0.001);
        }
    }

    @Test
    public void testSetMaxIterationsAndMaxEvaluations() throws Exception {
        UnivariateRealFunction func = new SimpleUnivariateFunction("x^2", x -> x * x);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciOptimizer(10, 0.001), 5, new java.util.Random());

        optimizer.setMaxIterations(5);
        optimizer.setMaxEvaluations(10);

        double[] results = optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(5, results.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaBeforeOptimize() throws Exception {
        UnivariateRealFunction func = new SimpleUnivariateFunction("x^2", x -> x * x);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateFibonacciOptimizer(10, 0.001), 5, new java.util.Random());

        optimizer.getOptima();
    }

}