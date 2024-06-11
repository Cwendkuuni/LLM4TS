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
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)} constructor.
     */
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizerConstructor() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertNotNull(optimizerWrapper);
        assertEquals(optimizer, optimizerWrapper.getOptimizer());
        assertEquals(5, optimizerWrapper.getStarts());
        assertNull(optimizerWrapper.getOptima());
        assertNull(optimizerWrapper.getOptimaValues());
        assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxIterations());
        assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getFunctionValue()} method.
     */
    @org.junit.Test
    public void testGetFunctionValue() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals(Double.NaN, optimizerWrapper.getFunctionValue(), 0.0);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getFunctionValue(), function.value(optimizerWrapper.getResult()), 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getResult()} method.
     */
    @org.junit.Test
    public void testGetResult() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals(Double.NaN, optimizerWrapper.getResult(), 0.0);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getResult(), optimizerWrapper.getOptima()[0], 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getIterationCount()} method.
     */
    @org.junit.Test
    public void testGetIterationCount() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals(0, optimizerWrapper.getIterationCount());
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getIterationCount(), optimizer.getIterationCount());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()} method.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxEvaluations());
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getMaxEvaluations(), optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()} method.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals(0, optimizerWrapper.getEvaluations());
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getEvaluations(), optimizer.getEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getAbsoluteAccuracy()} method.
     */
    @org.junit.Test
    public void testGetAbsoluteAccuracy() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals(Double.POSITIVE_INFINITY, optimizerWrapper.getAbsoluteAccuracy(), 0.0);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getAbsoluteAccuracy(), optimizer.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setAbsoluteAccuracy(double)} method.
     */
    @org.junit.Test
    public void testSetAbsoluteAccuracy() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerWrapper.setAbsoluteAccuracy(0.001);
        assertEquals(0.001, optimizerWrapper.getAbsoluteAccuracy(), 0.0);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getAbsoluteAccuracy(), optimizer.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaximalIterationCount()} method.
     */
    @org.junit.Test
    public void testGetMaximalIterationCount() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertEquals(Integer.MAX_VALUE, optimizerWrapper.getMaxIterations());
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getMaxIterations(), optimizer.getMaxIterationCount());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxIterations(int)} method.
     */
    @org.junit.Test
    public void testSetMaxIterations() {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerWrapper.setMaxIterations(10);
        assertEquals(10, optimizerWrapper.getMaxIterations());
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(optimizerWrapper.getMaxIterations(), optimizer.getMaxIterationCount());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()} method.
     */
    @org.junit.Test
    public void testGetOptima() throws IllegalStateException {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertArrayEquals(new double[5], optimizerWrapper.getOptima(), 0.0);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertArrayEquals(optimizerWrapper.getOptima(), new double[]{-1.2654789691792455, 0.5773183268442368, 0.9153446158638852, 1.395852316455463, 1.831325492271072}, 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptimaValues()} method.
     */
    @org.junit.Test
    public void testGetOptimaValues() throws IllegalStateException {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertArrayEquals(new double[5], optimizerWrapper.getOptimaValues(), 0.0);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        try {
            optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertArrayEquals(optimizerWrapper.getOptimaValues(), new double[]{-1.2654789691792455, 0.4056655495227232, 0.8012419647653332, 1.2687677211922387, 1.805232472714136}, 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} method with minimal function.
     */
    @org.junit.Test
    public void testMinimize() throws Exception {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        double result = optimizerWrapper.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        assertEquals(-1.2654789691792455, result, 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} method with maximal function.
     */
    @org.junit.Test
    public void testMaximize() throws Exception {
        UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);
        function.setMinimum(true);
        double result = optimizerWrapper.optimize(function, GoalType.MAXIMIZE, -1.0, 1.0);
        assertEquals(1.831325492271072, result, 0.001);
    }

}