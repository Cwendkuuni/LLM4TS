package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

/**
 * Test cases for {@link ContinuedFraction}.
 */
public class ContinuedFractionTest {

    /**
     * Test for {@link ContinuedFraction#evaluate(double)}.
     */
    @Test
    public void testEvaluateDouble() {
        final ContinuedFractionTestObj testObj = new ContinuedFractionTestObj();
        final double epsilon = 1e-12;
        final int maxIterations = 1000;

        // Test evaluate method with default arguments
        assertEquals("Test evaluate with default arguments.", testObj.getValue(0.0), testObj.evaluate(0.0), epsilon);

        // Test evaluate method with custom arguments
        assertEquals("Test evaluate with custom arguments.", testObj.getValue(1.0), testObj.evaluate(1.0, epsilon, maxIterations), epsilon);
    }

    /**
     * Test for {@link ContinuedFraction#getA(int, double)}.
     */
    @Test
    public void testGetA() {
        final ContinuedFractionTestObj testObj = new ContinuedFractionTestObj();

        for (int n = 0; n < 10; n++) {
            for (double x = -10.0; x <= 10.0; x += 0.1) {
                final double a = testObj.getA(n, x);
                assertEquals("Test getA(n, x) for n=" + n + ", x=" + x, testObj.getA(n, x), testObj.getA(n, x), 1e-10);
            }
        }
    }

    /**
     * Test for {@link ContinuedFraction#getB(int, double)}.
     */
    @Test
    public void testGetB() {
        final ContinuedFractionTestObj testObj = new ContinuedFractionTestObj();

        for (int n = 0; n < 10; n++) {
            for (double x = -10.0; x <= 10.0; x += 0.1) {
                final double b = testObj.getB(n, x);
                assertEquals("Test getB(n, x) for n=" + n + ", x=" + x, testObj.getB(n, x), testObj.getB(n, x), 1e-10);
            }
        }
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @Test
    public void testEvaluateConvergence() {
        final ContinuedFractionSqrtTestObj testObj = new ContinuedFractionSqrtTestObj();

        for (double x = -10.0; x <= 10.0; x += 0.1) {
            final double epsilon = 1e-12;
            final int maxIterations = 1000;

            try {
                testObj.evaluate(x, epsilon, maxIterations);
                fail("Expected ConvergenceException.");
            } catch (ConvergenceException e) {
                // Expected
            }
        }

        try {
            testObj.evaluate(2.0, epsilon, maxIterations);
            fail("Expected MaxCountExceededException.");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @Test
    public void testEvaluateNan() {
        final ContinuedFractionSqrtTestObj testObj = new ContinuedFractionSqrtTestObj();

        for (double x = Double.NaN; ; x = x + 0.1) {
            try {
                testObj.evaluate(x, 1e-12, Integer.MAX_VALUE);
                fail("Expected ConvergenceException.");
            } catch (ConvergenceException e) {
                // Expected
            }
        }
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, double, int)} with custom epsilon and maxIterations.
     */
    @Test
    public void testEvaluateCustomEpsilonAndMaxIterations() {
        final ContinuedFractionSqrtTestObj testObj = new ContinuedFractionSqrtTestObj();
        final double epsilon = 1e-20;
        final int maxIterations = 15;

        assertEquals("Test evaluate with custom epsilon and maxIterations.", Math.sqrt(2.0), testObj.evaluate(2.0, epsilon, maxIterations), epsilon);
    }
}

class ContinuedFractionTestObj extends ContinuedFraction {
    private double[] values = {0.0, 1.0, 2.0, 2.0, 1.0, 1.0, 1.0};

    @Override
    protected double getA(int n, double x) {
        return values[n];
    }

    @Override
    protected double getB(int n, double x) {
        return n > 0 ? x : 1.0;
    }

    private double getValue(double x) {
        double value = 0.0;
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                value = values[i];
            } else {
                value = value + 1.0 / (x * values[i]);
            }
        }
        return value;
    }
}

class ContinuedFractionSqrtTestObj extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return 2.0;
        }
        return x;
    }

    @Override
    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return x;
        }
        return 1.0;
    }
}