package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test method for {@link ContinuedFraction#getA(int, double)}.
     */
    @org.junit.Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 0.5;
        for (int i = 0; i <= 5; i++) {
            double result = continuedFraction.getA(i, x);
            double expected = i == 0 ? 1.0 : (i % 2 == 0 ? 2.0 : -1.0);
            assertEquals(expected, result, 1e-10);
        }
    }

    /**
     * Test method for {@link ContinuedFraction#getB(int, double)}.
     */
    @org.junit.Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 0.5;
        for (int i = 0; i <= 5; i++) {
            double result = continuedFraction.getB(i, x);
            double expected = i == 0 ? 1.0 : (i % 2 == 0 ? 1.0 : 2.0);
            assertEquals(expected, result, 1e-10);
        }
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double)}.
     */
    @org.junit.Test
    public void testEvaluateDefault() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 0.5;
        double result = continuedFraction.evaluate(x);
        double expected = 1.648049444954;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double)}.
     */
    @org.junit.Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 0.5;
        double epsilon = 1e-9;
        double result = continuedFraction.evaluate(x, epsilon);
        double expected = 1.648049444954;
        assertEquals(expected, result, epsilon);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, int)}.
     */
    @org.junit.Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 0.5;
        int maxIterations = 5;
        try {
            double result = continuedFraction.evaluate(x, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with infinite result.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteResult() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 1.0;
        double epsilon = 1e-10;
        int maxIterations = 10;
        continuedFraction.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with NaN result.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateNaNResult() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 0.0;
        double epsilon = 1e-10;
        int maxIterations = 10;
        continuedFraction.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with max iterations.
     */
    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double x = 0.5;
        double epsilon = 1e-10;
        int maxIterations = 1;
        continuedFraction.evaluate(x, epsilon, maxIterations);
    }
}

class TestContinuedFraction extends ContinuedFraction {
    // Empty implementation.
}