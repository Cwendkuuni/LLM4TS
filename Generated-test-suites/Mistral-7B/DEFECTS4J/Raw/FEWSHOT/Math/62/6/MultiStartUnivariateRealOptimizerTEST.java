package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizer() {
        // Test with a simple function and minimization goal.
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1d, 1d, -1d}, new double[]{0d, 1d, 1d});
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new MinimizerRegulaFalsi(function), 10, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });

        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d);
        assertNotNull(result);
        assertEquals(0d, result.getX(), 1e-6);
        assertEquals(-1d, result.getValue(), 1e-6);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(10, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            assertNotNull(pair);
            assertEquals(0d, pair.getX(), 1e-6);
            assertEquals(-1d, pair.getValue(), 1e-6);
        }
    }

    @Test
    public void testMaximization() {
        // Test with a simple function and maximization goal.
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1d, -1d, 1d}, new double[]{0d, 1d, 0d});
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new MaximizerBrent(function), 10, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });

        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, 0d, 1d);
        assertNotNull(result);
        assertEquals(1d, result.getX(), 1e-6);
        assertEquals(1d, result.getValue(), 1e-6);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(10, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            assertNotNull(pair);
            assertEquals(1d, pair.getX(), 1e-6);
            assertEquals(1d, pair.getValue(), 1e-6);
        }
    }

    @Test
    public void testConvergenceException() {
        // Test with a function that does not converge.
        UnivariateRealFunction function = new PolynomialFunction(new double[]{0d, -1d, 1d}, new double[]{1d, 0d, 1d});
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new MinimizerRegulaFalsi(function), 10, new RandomGenerator(){
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(10, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            assertNull(pair);
        }
    }
}