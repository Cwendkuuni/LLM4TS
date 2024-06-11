package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwister;

import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;

/**
 * Test cases for the MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test the constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        assertNotNull(multiOptimizer);
        assertEquals(optimizer, multiOptimizer.getOptimizer());
        assertEquals(10, multiOptimizer.getStarts());
    }

    /**
     * Test the getFunctionValue method.
     */
    @org.junit.Test
    public void testGetFunctionValue() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertFalse("Expected ConvergenceException, but got " + result, false);
        } catch (ConvergenceException e) {
            // expected
        }
        try {
            double result = multiOptimizer.getFunctionValue();
            assertEquals(Double.NaN, result, 0.0);
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.setMaxIterations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNotNull(multiOptimizer.getOptimizer().getFunctionValue());
    }

    /**
     * Test the getResult method.
     */
    @org.junit.Test
    public void testGetResult() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertFalse("Expected ConvergenceException, but got " + result, false);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNull(multiOptimizer.getOptima());
        assertNull(multiOptimizer.getOptimaValues());
        assertNull(multiOptimizer.getResult());

        try {
            optimizer.setMaxIterations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNotNull(multiOptimizer.getOptima());
        assertNotNull(multiOptimizer.getOptimaValues());
        assertNotNull(multiOptimizer.getResult());
        assertEquals(1.0, multiOptimizer.getOptima().length, 0.0);
        assertEquals(1.0, multiOptimizer.getOptimaValues().length, 0.0);
        assertArrayEquals(-1.5, multiOptimizer.getOptima(), 0.01);
        assertArrayEquals(-1.5, multiOptimizer.getOptimaValues(), 0.01);
        assertEquals(-1.5, multiOptimizer.getResult(), 0.01);
    }

    /**
     * Test the getIterationCount method.
     */
    @org.junit.Test
    public void testGetIterationCount() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            optimizer.setMaxIterations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(0, multiOptimizer.getIterationCount());

        try {
            optimizer.setMaxIterations(10);
            optimizer.setMaxEvaluations(10);
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(10, multiOptimizer.getIterationCount());
    }

    /**
     * Test the getMaxEvaluations method.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            optimizer.setMaxIterations(1);
            optimizer.setMaxEvaluations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(1, multiOptimizer.getMaxEvaluations());

        try {
            optimizer.setMaxIterations(10);
            optimizer.setMaxEvaluations(10);
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(10, multiOptimizer.getMaxEvaluations());
    }

    /**
     * Test the getEvaluations method.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            optimizer.setMaxIterations(1);
            optimizer.setMaxEvaluations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(1, multiOptimizer.getEvaluations());

        try {
            optimizer.setMaxIterations(10);
            optimizer.setMaxEvaluations(10);
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(10, multiOptimizer.getEvaluations());
    }

    /**
     * Test the getMaxIterations method.
     */
    @org.junit.Test
    public void testGetMaxIterations() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            optimizer.setMaxIterations(1);
            optimizer.setMaxEvaluations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(Integer.MAX_VALUE, multiOptimizer.getMaxIterations());

        try {
            optimizer.setMaxIterations(10);
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertEquals(10, multiOptimizer.getMaxIterations());
    }

    /**
     * Test the getMaxIterations method with an invalid argument.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetMaxIterationsInvalidArgument() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        multiOptimizer.setMaxIterations(-1);
    }

    /**
     * Test the getOptima method.
     */
    @org.junit.Test
    public void testGetOptima() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            optimizer.setMaxIterations(1);
            optimizer.setMaxEvaluations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNull(multiOptimizer.getOptima());

        try {
            optimizer.setMaxIterations(10);
            optimizer.setMaxEvaluations(10);
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNotNull(multiOptimizer.getOptima());
        assertEquals(10, multiOptimizer.getOptima().length);
        assertArrayEquals(-1.5, multiOptimizer.getOptima(), 0.01);
    }

    /**
     * Test the getOptimaValues method.
     */
    @org.junit.Test
    public void testGetOptimaValues() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new int[]{1, 2, 1});
        try {
            optimizer.setMaxIterations(1);
            optimizer.setMaxEvaluations(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNull(multiOptimizer.getOptimaValues());

        try {
            optimizer.setMaxIterations(10);
            optimizer.setMaxEvaluations(10);
            double result = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(-1.5, result, 0.01);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNotNull(multiOptimizer.getOptimaValues());
        assertEquals(10, multiOptimizer.getOptimaValues().length);
        assertArrayEquals(-1.5, multiOptimizer.getOptimaValues(), 0.01);
    }

}