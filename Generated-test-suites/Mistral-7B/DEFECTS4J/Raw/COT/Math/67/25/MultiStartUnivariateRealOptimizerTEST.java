package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;
    private RandomGenerator random;

    @Before
    public void setup() {
        function = new MyUnivariateRealFunction();
        random = new TestRandomGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer(
                new FixedPointUnivariateRealOptimizer(0.001), 10, random);
    }

    /**
     * Test method getFunctionValue()
     */
    @Test
    public void testGetFunctionValue() {
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            double expected = function.value(optimizer.getResult());
            Assert.assertEquals(expected, optimizer.getFunctionValue(), 0.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getResult()
     */
    @Test
    public void testGetResult() {
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            double expected = optimizer.getResult();
            Assert.assertTrue(function.value(expected) < 1.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getAbsoluteAccuracy()
     */
    @Test
    public void testGetAbsoluteAccuracy() {
        try {
            optimizer.setAbsoluteAccuracy(0.001);
            Assert.assertEquals(0.001, optimizer.getAbsoluteAccuracy(), 0.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getIterationCount()
     */
    @Test
    public void testGetIterationCount() {
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.assertTrue(optimizer.getIterationCount() > 0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getMaximalIterationCount()
     */
    @Test
    public void testGetMaximalIterationCount() {
        try {
            optimizer.setMaximalIterationCount(100);
            Assert.assertEquals(100, optimizer.getMaximalIterationCount());
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getMaxEvaluations()
     */
    @Test
    public void testGetMaxEvaluations() {
        try {
            optimizer.setMaxEvaluations(1000);
            Assert.assertEquals(1000, optimizer.getMaxEvaluations());
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getEvaluations()
     */
    @Test
    public void testGetEvaluations() {
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.assertTrue(optimizer.getEvaluations() > 0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getRelativeAccuracy()
     */
    @Test
    public void testGetRelativeAccuracy() {
        try {
            optimizer.setRelativeAccuracy(0.001);
            Assert.assertEquals(0.001, optimizer.getRelativeAccuracy(), 0.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method resetAbsoluteAccuracy()
     */
    @Test
    public void testResetAbsoluteAccuracy() {
        try {
            optimizer.setAbsoluteAccuracy(0.001);
            optimizer.resetAbsoluteAccuracy();
            Assert.assertEquals(Double.POSITIVE_INFINITY, optimizer.getAbsoluteAccuracy(), 0.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method resetMaximalIterationCount()
     */
    @Test
    public void testResetMaximalIterationCount() {
        try {
            optimizer.setMaximalIterationCount(100);
            optimizer.resetMaximalIterationCount();
            Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method resetRelativeAccuracy()
     */
    @Test
    public void testResetRelativeAccuracy() {
        try {
            optimizer.setRelativeAccuracy(0.001);
            optimizer.resetRelativeAccuracy();
            Assert.assertEquals(Double.POSITIVE_INFINITY, optimizer.getRelativeAccuracy(), 0.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method setAbsoluteAccuracy()
     */
    @Test
    public void testSetAbsoluteAccuracy() {
        try {
            optimizer.setAbsoluteAccuracy(0.001);
            Assert.assertEquals(0.001, optimizer.getAbsoluteAccuracy(), 0.0);

            optimizer.setAbsoluteAccuracy(0.0);
            Assert.assertEquals(Double.POSITIVE_INFINITY, optimizer.getAbsoluteAccuracy(), 0.0);

            optimizer.setAbsoluteAccuracy(-0.001);
            Assert.assertEquals(-0.001, optimizer.getAbsoluteAccuracy(), 0.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method setMaximalIterationCount()
     */
    @Test
    public void testSetMaximalIterationCount() {
        try {
            optimizer.setMaximalIterationCount(100);
            Assert.assertEquals(100, optimizer.getMaximalIterationCount());

            optimizer.setMaximalIterationCount(0);
            Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());

            optimizer.setMaximalIterationCount(-100);
            Assert.assertEquals(-100, optimizer.getMaximalIterationCount());
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method setMaxEvaluations()
     */
    @Test
    public void testSetMaxEvaluations() {
        try {
            optimizer.setMaxEvaluations(1000);
            Assert.assertEquals(1000, optimizer.getMaxEvaluations());

            optimizer.setMaxEvaluations(0);
            Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());

            optimizer.setMaxEvaluations(-1000);
            Assert.assertEquals(-1000, optimizer.getMaxEvaluations());
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method setRelativeAccuracy()
     */
    @Test
    public void testSetRelativeAccuracy() {
        try {
            optimizer.setRelativeAccuracy(0.001);
            Assert.assertEquals(0.001, optimizer.getRelativeAccuracy(), 0.0);

            optimizer.setRelativeAccuracy(0.0);
            Assert.assertEquals(Double.POSITIVE_INFINITY, optimizer.getRelativeAccuracy(), 0.0);

            optimizer.setRelativeAccuracy(-0.001);
            Assert.assertEquals(-0.001, optimizer.getRelativeAccuracy(), 0.0);
        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test method getOptima()
     */
    @Test
    public void testGetOptima() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] expectedOptima = new double[]{optimizer.getResult()};
        double[] actualOptima = optimizer.getOptima();
        Assert.assertArrayEquals(expectedOptima, actualOptima);
    }

    /**
     * Test method getOptimaValues()
     */
    @Test
    public void testGetOptimaValues() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] expectedOptimaValues = new double[]{function.value(optimizer.getResult())};
        double[] actualOptimaValues = optimizer.getOptimaValues();
        Assert.assertArrayEquals(expectedOptimaValues, actualOptimaValues);
    }

    /**
     * Test method optimize() with minimum goal
     */
    @Test
    public void testOptimizeMinimum() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double optimalValue = function.value(optimizer.getResult());
        Assert.assertTrue(optimalValue < 1.0);
    }

    /**
     * Test method optimize() with maximum goal
     */
    @Test
    public void testOptimizeMaximum() throws Exception {
        optimizer.optimize(new MyUnivariateRealFunctionMax(), GoalType.MAXIMIZE, 0.0, 1.0);
        double optimalValue = function.value(optimizer.getResult());
        Assert.assertTrue(optimalValue > 1.0);
    }

    /**
     * Test method optimize() with wrong goal
     */
    @Test(expected = IllegalStateException.class)
    public void testOptimizeWrongGoal() throws Exception {
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 1.0);
    }

    /**
     * Test method optimize() with null function
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullFunction() throws Exception {
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test method optimize() with wrong bounds
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWrongBounds() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, 2.0, 0.0);
    }

    /**
     * Test edge case for MultiStartUnivariateRealOptimizer with max iterations
     */
    @Test(expected = ConvergenceException.class)
    public void testEdgeCaseMaxIterations() throws Exception {
        optimizer.setMaximalIterationCount(0);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test edge case for MultiStartUnivariateRealOptimizer with max evaluations
     */
    @Test(expected = ConvergenceException.class)
    public void testEdgeCaseMaxEvaluations() throws Exception {
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test edge case for MultiStartUnivariateRealOptimizer with too many restarts
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeCaseTooManyRestarts() throws Exception {
        optimizer = new MultiStartUnivariateRealOptimizer(
                new FixedPointUnivariateRealOptimizer(0.001), 1000, random);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test edge case for MultiStartUnivariateRealOptimizer with random generation
     */
    @Test
    public void testEdgeCaseRandomGeneration() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] allOptima = optimizer.getOptima();
        double[] allOptimaValues = optimizer.getOptimaValues();
        for (int i = 0; i < optimizer.getStarts(); i++) {
            Assert.assertNotEquals(optimizer.getResult(), allOptima[i]);
            Assert.assertNotEquals(function.value(optimizer.getResult()), allOptimaValues[i]);
        }
        double minValue = Double.POSITIVE_INFINITY;
        for (int i = 0; i < optimizer.getStarts(); i++) {
            if (function.value(allOptima[i]) < minValue) {
                minValue = function.value(allOptima[i]);
            }
        }
        Assert.assertTrue(minValue < 1.0);
    }

    /**
     * Custom function for testing
     */
    private static class MyUnivariateRealFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(Math.PI * x), 2) + 1;
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(Math.PI * x) * Math.cos(Math.PI * x);
        }
    }

    /**
     * Custom function for testing maximum goal
     */
    private static class MyUnivariateRealFunctionMax implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return -Math.pow(Math.sin(Math.PI * x), 2) - 1;
        }

        @Override
        public double derivative(double x) {
            return -2 * Math.sin(Math.PI * x) * Math.cos(Math.PI * x);
        }
    }
}

class TestRandomGenerator implements RandomGenerator {

    @Override
    public double nextDouble() {
        return 0.5;
    }

    @Override
    public long nextLong() {
        return 0;
    }

    @Override
    public void setSeed(long seed) {
    }
}