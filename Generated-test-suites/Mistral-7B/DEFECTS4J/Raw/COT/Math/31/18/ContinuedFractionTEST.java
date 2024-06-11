package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {
    /**
     * Test evaluate method with default parameters.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.evaluate(2.0);
        double expected = 1.4142135623730955;
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test evaluate method with custom epsilon.
     */
    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.evaluate(2.0, 1e-11);
        double expected = 1.4142137410144062;
        assertEquals(expected, result, 1e-11);
    }

    /**
     * Test evaluate method with custom maxIterations.
     */
    @Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.evaluate(2.0, Double.POSITIVE_INFINITY, 10);
        double expected = 1.4142135623730955;
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test evaluate method with infinite value.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfinite() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.evaluate(Double.POSITIVE_INFINITY);
    }

    /**
     * Test evaluate method with NaN value.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaN() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.evaluate(Double.NaN);
    }

    /**
     * Test evaluate method with maxIterations exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsExceeded() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.evaluate(2.0, Double.POSITIVE_INFINITY, 1);
    }

    /**
     * Subclass of ContinuedFraction for testing purposes.
     */
    private static class ContinuedFractionSqrt2 extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n % 2 == 0) {
                return 1.0;
            } else {
                return 2.0 * x;
            }
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }
}