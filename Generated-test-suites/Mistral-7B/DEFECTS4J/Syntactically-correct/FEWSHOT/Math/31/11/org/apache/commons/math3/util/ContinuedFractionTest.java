package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test class for {@link ContinuedFraction}.
 */
public class ContinuedFractionTest {

    /**
     * Test of evaluate method, of class ContinuedFraction, with various
     * continued fractions and epsilon value.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvaluate() throws Exception {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        ContinuedFraction e = new EContinuedFraction();

        // Test with default epsilon
        double x = 2.0;
        double result = sqrt2.evaluate(x);
        assertEquals("sqrt2 evaluated incorrectly", 1.4142135623730955, result, 1e-8);

        x = Math.E;
        result = e.evaluate(x);
        assertEquals("e evaluated incorrectly", E.E, result, 1e-14);

        // Test with custom epsilon
        double customEpsilon = 1e-10;
        result = sqrt2.evaluate(x, customEpsilon);
        assertEquals("sqrt2 evaluated incorrectly with custom epsilon", 1.4142135623730955, result, customEpsilon);

        // Test with maxIterations
        int maxIterations = 10;
        result = sqrt2.evaluate(x, customEpsilon, maxIterations);
        assertEquals("sqrt2 evaluated incorrectly with maxIterations", 1.4142135623730955, result, customEpsilon);
    }

    /**
     * Test of getA and getB methods, of class ContinuedFraction, with various
     * inputs.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAAndB() throws Exception {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        ContinuedFraction e = new EContinuedFraction();

        // Test getA and getB for Sqrt2ContinuedFraction
        for (int i = 0; i < 10; i++) {
            double a = sqrt2.getA(i, 2.0);
            double b = sqrt2.getB(i, 2.0);
            if (i == 0) {
                assertEquals("A[0] should be 2.0", 2.0, a, 1e-8);
                assertEquals("B[0] should be 1.0", 1.0, b, 1e-8);
            } else {
                assertEquals("A[" + i + "] should be correct", (1.0 + Math.sqrt(5.0)) / 2.0, a, 1e-8);
                assertEquals("B[" + i + "] should be correct", (1.0 - Math.sqrt(5.0)) / 2.0, b, 1e-8);
            }
        }

        // Test getA and getB for EContinuedFraction
        for (int i = 0; i < 10; i++) {
            double a = e.getA(i, Math.E);
            double b = e.getB(i, Math.E);
            if (i == 0) {
                assertEquals("A[0] should be 2.0", 2.0, a, 1e-8);
                assertEquals("B[0] should be 2.0", 1.0, b, 1e-8);
            } else {
                assertEquals("A[" + i + "] should be correct", (Math.E + Math.E * i), a, 1e-14);
                assertEquals("B[" + i + "] should be correct", 1.0, b, 1e-14);
            }
        }
    }

    /**
     * Test of evaluate method, of class ContinuedFraction, with various
     * continued fractions and maxIterations.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvaluateWithMaxIterations() throws Exception {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        ContinuedFraction e = new EContinuedFraction();

        // Test with maxIterations
        int maxIterations = 5;
        double x = 2.0;
        double result = sqrt2.evaluate(x, 1e-8, maxIterations);
        assertEquals("sqrt2 evaluated incorrectly with maxIterations", 1.4142135623730955, result, 1e-8);

        x = Math.E;
        result = e.evaluate(x, 1e-14, maxIterations);
        assertEquals("e evaluated incorrectly with maxIterations", E.E, result, 1e-14);
    }

    /**
     * Test of evaluate method, of class ContinuedFraction, with various
     * continued fractions and infinite or NaN values for x.
     * @throws java.lang.Exception
     */
    @Test(expected = ConvergenceException.class)
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithInvalidX() throws Exception {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        ContinuedFraction e = new EContinuedFraction();

        // Test with negative x
        sqrt2.evaluate(-1.0, 1e-8, Integer.MAX_VALUE);

        // Test with zero x
        assertEquals("sqrt2 evaluated incorrectly with zero x", Double.NaN, sqrt2.evaluate(0.0, 1e-8, Integer.MAX_VALUE), 1e-8);

        // Test with infinite x
        double inf = Double.POSITIVE_INFINITY;
        e.evaluate(inf, 1e-14, Integer.MAX_VALUE);

        // Test with NaN x
        Double nan = Double.NaN;
        sqrt2.evaluate(nan, 1e-8, Integer.MAX_VALUE);

        // Test with maxIterations too small
        int maxIterations = 0;
        e.evaluate(Math.E, 1e-14, maxIterations);
    }

    /**
     * Test of getA method, of class ContinuedFraction, with negative n.
     * @throws java.lang.Exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetANegativeIndex() throws Exception {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        sqrt2.getA(-1, 2.0);
    }

    /**
     * Test of getB method, of class ContinuedFraction, with negative n.
     * @throws java.lang.Exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetBNegativeIndex() throws Exception {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        sqrt2.getB(-1, 2.0);
    }

    /**
     * Test of getA and getB methods, of class ContinuedFraction, with out-of-range n.
     * @throws java.lang.Exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetAAndBOutOfRange() throws Exception {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        sqrt2.getA(Integer.MAX_VALUE, 2.0);
        sqrt2.getB(Integer.MAX_VALUE, 2.0);
    }

}