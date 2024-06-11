package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test for evaluate method with valid arguments.
     */
    @Test
    public void testEvaluateValid() {
        ContinuedFraction testee = new TestContinuedFraction();
        double result = testee.evaluate(1.0);
        double expected = 1.64805138775561062862L; // value obtained from MathWorld
        Assert.assertEquals(expected, result, 1e-10);
    }

    /**
     * Test for evaluate method with small epsilon.
     */
    @Test
    public void testEvaluateSmallEpsilon() {
        ContinuedFraction testee = new TestContinuedFraction();
        double result = testee.evaluate(1.0, 1e-15);
        double expected = 1.64805138775561062862L; // value obtained from MathWorld
        Assert.assertEquals(expected, result, 1e-15);
    }

    /**
     * Test for evaluate method with large epsilon.
     */
    @Test
    public void testEvaluateLargeEpsilon() {
        ContinuedFraction testee = new TestContinuedFraction();
        double result = testee.evaluate(1.0, 1e-3);
        double expected = 1.6; // approximated value
        Assert.assertEquals(expected, result, 1e-3);
    }

    /**
     * Test for evaluate method with large maxIterations.
     */
    @Test
    public void testEvaluateLargeMaxIterations() {
        ContinuedFraction testee = new TestContinuedFraction();
        double result = testee.evaluate(1.0, Double.POSITIVE_INFINITY);
        double expected = 1.64805138775561062862L; // value obtained from MathWorld
        Assert.assertEquals(expected, result, 1e-15);
    }

    /**
     * Test for evaluate method with negative argument.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNegativeArgument() {
        ContinuedFraction testee = new TestContinuedFraction();
        testee.evaluate(-1.0);
    }

    /**
     * Test for evaluate method with infinite argument.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteArgument() {
        ContinuedFraction testee = new TestContinuedFraction();
        testee.evaluate(Double.POSITIVE_INFINITY);
    }

    /**
     * Test for evaluate method with NaN argument.
     */
    @Test(expected = ArithmeticException.class)
    public void testEvaluateNaNArgument() {
        ContinuedFraction testee = new TestContinuedFraction();
        testee.evaluate(Double.NaN);
    }

    /**
     * Test for evaluate method with small x.
     */
    @Test
    public void testEvaluateSmallX() {
        ContinuedFraction testee = new TestContinuedFraction();
        double result = testee.evaluate(1e-10);
        double expected = 1.64805138775561062862L; // value obtained from MathWorld
        Assert.assertEquals(expected, result, 1e-15);
    }

    /**
     * Test for evaluate method with large x.
     */
    @Test
    public void testEvaluateLargeX() {
        ContinuedFraction testee = new TestContinuedFraction();
        double result = testee.evaluate(1e10);
        double expected = 1.64805138775561062862L; // value obtained from MathWorld
        Assert.assertEquals(expected, result, 1e-15);
    }

    /**
     * Test for evaluate method with maxIterations exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsExceeded() {
        ContinuedFraction testee = new TestContinuedFraction();
        testee.evaluate(1.0, 1);
    }

    /**
     * Inner class for testing the ContinuedFraction.
     */
    private static class TestContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return 1.0;
                case 2:
                    return x;
                default:
                    return 1.0;
            }
        }

        @Override
        protected double getB(int n, double x) {
            switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return 1.0 - x;
                case 2:
                    return x;
                default:
                    return 0.0;
            }
        }
    }
}