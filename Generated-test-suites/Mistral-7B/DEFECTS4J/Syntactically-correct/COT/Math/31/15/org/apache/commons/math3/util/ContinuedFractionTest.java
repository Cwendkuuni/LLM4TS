package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

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
        double result = continuedFraction.getA(0, 0.0);
        assertEquals("getA should return expected value for (n, x) = (0, 0.0)", 0.0, result, 1e-9);

        result = continuedFraction.getA(1, 0.0);
        assertEquals("getA should return expected value for (n, x) = (1, 0.0)", 1.0, result, 1e-9);

        try {
            continuedFraction.getA(-1, 0.0);
            fail("getA should throw an exception for negative n");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            continuedFraction.getA(0, Double.NaN);
            fail("getA should throw an exception for NaN x");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            continuedFraction.getA(0, Double.POSITIVE_INFINITY);
            fail("getA should throw an exception for infinite x");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link ContinuedFraction#getB(int, double)}.
     */
    @org.junit.Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double result = continuedFraction.getB(0, 0.0);
        assertEquals("getB should return expected value for (n, x) = (0, 0.0)", 1.0, result, 1e-9);

        result = continuedFraction.getB(1, 0.0);
        assertEquals("getB should return expected value for (n, x) = (1, 0.0)", 1.0, result, 1e-9);

        try {
            continuedFraction.getB(-1, 0.0);
            fail("getB should throw an exception for negative n");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            continuedFraction.getB(0, Double.NaN);
            fail("getB should throw an exception for NaN x");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            continuedFraction.getB(0, Double.POSITIVE_INFINITY);
            fail("getB should throw an exception for infinite x");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double)}.
     */
    @org.junit.Test
    public void testEvaluateDefault() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double result = continuedFraction.evaluate(0.0);
        assertEquals("evaluate should return expected value for x = 0.0", 1.0, result, 1e-9);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double)}.
     */
    @org.junit.Test
    public void testEvaluate() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double result = continuedFraction.evaluate(0.5, 1e-9);
        assertEquals("evaluate should return expected value for (x, epsilon) = (0.5, 1e-9)", 1.6480512295189683, result, 1e-9);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @org.junit.Test
    public void testEvaluateIterations() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double result = continuedFraction.evaluate(0.5, 1e-9, 10000);
        assertEquals("evaluate should return expected value for (x, epsilon, maxIterations) = (0.5, 1e-9, 10000)", 1.6480512295189683, result, 1e-9);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.5, 1e-18, 3);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.5, 1e-9, 1);
    }

}

class TestContinuedFraction extends ContinuedFraction {
    // Empty implementation for testing
}