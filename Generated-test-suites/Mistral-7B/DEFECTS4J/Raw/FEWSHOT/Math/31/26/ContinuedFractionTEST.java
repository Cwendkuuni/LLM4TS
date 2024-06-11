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
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class ContinuedFractionTest {

    /**
     * Test evaluating Euler-Mascheroni constant continued fraction.
     */
    @Test
    public void testEulerMascheroni() {
        ContinuedFraction eulerMascheroni = new EulerMascheroniContinuedFraction();
        assertEquals(Math.E, eulerMascheroni.evaluate(1.0), 1e-14);
    }

    /**
     * Test evaluating square root of 2 continued fraction.
     */
    @Test
    public void testSqrt2() {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        assertEquals(Math.sqrt(2), sqrt2.evaluate(1.0), 1e-14);
    }

    /**
     * Test evaluating log base 2 of e continued fraction.
     */
    @Test
    public void testLogBase2e() {
        ContinuedFraction logBase2e = new LogBase2eContinuedFraction();
        assertEquals(Math.log(Math.E) / Math.log(2), logBase2e.evaluate(1.0), 1e-14);
    }

    /**
     * Test evaluating log base e of 2 continued fraction.
     */
    @Test
    public void testLogBaseE2() {
        ContinuedFraction logBaseE2 = new LogBaseE2ContinuedFraction();
        assertEquals(Math.log(2) / Math.log(Math.E), logBaseE2.evaluate(1.0), 1e-14);
    }

    /**
     * Test evaluating logarithmic integral continued fraction.
     */
    @Test
    public void testLogInt() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        assertEquals(E.EULER_GAMMA.getLogarithmicValue() + Math.log(Math.E), logInt.evaluate(1.0), 1e-14);
    }

    /**
     * Test evaluating logarithmic integral continued fraction with negative x.
     */
    @Test(expected = ConvergenceException.class)
    public void testLogIntNegative() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        logInt.evaluate(-1.0);
    }

    /**
     * Test evaluating logarithmic integral continued fraction with max iterations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testLogIntMaxIterations() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        logInt.evaluate(1.0, 0.0, 0);
    }

    /**
     * Test evaluating logarithmic integral continued fraction with small epsilon.
     */
    @Test(expected = ConvergenceException.class)
    public void testLogIntSmallEpsilon() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        logInt.evaluate(1.0, 1e-15, Integer.MAX_VALUE);
    }

    /**
     * Test getA and getB methods for Euler-Mascheroni constant continued fraction.
     */
    @Test
    public void testEulerMascheroniAAndB() {
        ContinuedFraction eulerMascheroni = new EulerMascheroniContinuedFraction();
        assertEquals(0.0, eulerMascheroni.getA(0, 1.0), 1e-14);
        assertEquals(1.0, eulerMascheroni.getB(0, 1.0), 1e-14);
        assertEquals(1.0, eulerMascheroni.getA(1, 1.0), 1e-14);
        assertEquals(-1.0, eulerMascheroni.getB(1, 1.0), 1e-14);
        assertEquals(1.0 / 2, eulerMascheroni.getA(2, 1.0), 1e-14);
        assertEquals(-1.0 / 2, eulerMascheroni.getB(2, 1.0), 1e-14);
        assertEquals(1.0 / 3, eulerMascheroni.getA(3, 1.0), 1e-14);
        assertEquals(-1.0 / 2, eulerMascheroni.getB(3, 1.0), 1e-14);
        assertEquals(1.0 / 4, eulerMascheroni.getA(4, 1.0), 1e-14);
        assertEquals(-1.0 / 2, eulerMascheroni.getB(4, 1.0), 1e-14);
        assertEquals(1.0 / 5, eulerMascheroni.getA(5, 1.0), 1e-14);
        assertEquals(-1.0 / 2, eulerMascheroni.getB(5, 1.0), 1e-14);
        assertEquals(1.0 / 6, eulerMascheroni.getA(6, 1.0), 1e-14);
        assertEquals(-1.0 / 2, eulerMascheroni.getB(6, 1.0), 1e-14);
        assertEquals(1.0 / 7, eulerMascheroni.getA(7, 1.0), 1e-14);
        assertEquals(-1.0 / 2, eulerMascheroni.getB(7, 1.0), 1e-14);
    }

    /**
     * Test getA and getB methods for log base 2 of e continued fraction.
     */
    @Test
    public void testLogBase2eAAndB() {
        ContinuedFraction logBase2e = new LogBase2eContinuedFraction();
        assertEquals(1.0, logBase2e.getA(0, 1.0), 1e-14);
        assertEquals(0.0, logBase2e.getB(0, 1.0), 1e-14);
        assertEquals(1.0 / 2, logBase2e.getA(1, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(1, 1.0), 1e-14);
        assertEquals(1.0 / 4, logBase2e.getA(2, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(2, 1.0), 1e-14);
        assertEquals(1.0 / 8, logBase2e.getA(3, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(3, 1.0), 1e-14);
        assertEquals(1.0 / 16, logBase2e.getA(4, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(4, 1.0), 1e-14);
        assertEquals(1.0 / 32, logBase2e.getA(5, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(5, 1.0), 1e-14);
        assertEquals(1.0 / 64, logBase2e.getA(6, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(6, 1.0), 1e-14);
        assertEquals(1.0 / 128, logBase2e.getA(7, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(7, 1.0), 1e-14);
    }

    /**
     * Test getA and getB methods for logarithmic integral continued fraction.
     */
    @Test
    public void testLogIntAAndB() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        assertEquals(1.0, logInt.getA(0, 1.0), 1e-14);
        assertEquals(1.0 / 2, logInt.getA(1, 1.0), 1e-14);
        assertEquals(1.0 / 3, logInt.getA(2, 1.0), 1e-14);
        assertEquals(1.0 / 4, logInt.getA(3, 1.0), 1e-14);
        assertEquals(1.0 / 5, logInt.getA(4, 1.0), 1e-14);
        assertEquals(1.0 / 6, logInt.getA(5, 1.0), 1e-14);
        assertEquals(1.0 / 7, logInt.getA(6, 1.0), 1e-14);
        assertEquals(1.0 / 8, logInt.getA(7, 1.0), 1e-14);
        assertEquals(1.0 / 9, logInt.getA(8, 1.0), 1e-14);
        assertEquals(1.0 / 10, logInt.getA(9, 1.0), 1e-14);
        assertEquals(1.0 / 11, logInt.getA(10, 1.0), 1e-14);
    }

    /**
     * Test getB method for Euler-Mascheroni constant continued fraction.
     */
    @Test
    public void testEulerMascheroniB() {
        ContinuedFraction eulerMascheroni = new EulerMascheroniContinuedFraction();
        assertEquals(1.0, eulerMascheroni.getB(0, 1.0), 1e-14);
        assertEquals(-1.0, eulerMascheroni.getB(1, 1.0), 1e-14);
        assertEquals(-1.0, eulerMascheroni.getB(2, 1.0), 1e-14);
        assertEquals(1.0, eulerMascheroni.getB(3, 1.0), 1e-14);
        assertEquals(-1.0, eulerMascheroni.getB(4, 1.0), 1e-14);
        assertEquals(1.0, eulerMascheroni.getB(5, 1.0), 1e-14);
        assertEquals(-1.0, eulerMascheroni.getB(6, 1.0), 1e-14);
        assertEquals(1.0, eulerMascheroni.getB(7, 1.0), 1e-14);
        assertEquals(-1.0, eulerMascheroni.getB(8, 1.0), 1e-14);
        assertEquals(1.0, eulerMascheroni.getB(9, 1.0), 1e-14);
        assertEquals(-1.0, eulerMascheroni.getB(10, 1.0), 1e-14);
    }

    /**
     * Test getB method for log base 2 of e continued fraction.
     */
    @Test
    public void testLogBase2eB() {
        ContinuedFraction logBase2e = new LogBase2eContinuedFraction();
        assertEquals(0.0, logBase2e.getB(0, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(1, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(2, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(3, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(4, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(5, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(6, 1.0), 1e-14);
        assertEquals(-1.0 / 2, logBase2e.getB(7, 1.0), 1e-14);
    }

    /**
     * Test getB method for logarithmic integral continued fraction.
     */
    @Test
    public void testLogIntB() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        assertEquals(1.0, logInt.getB(0, 1.0), 1e-14);
        assertEquals(1.0 / 2, logInt.getB(1, 1.0), 1e-14);
        assertEquals(1.0 / 3, logInt.getB(2, 1.0), 1e-14);
        assertEquals(1.0 / 4, logInt.getB(3, 1.0), 1e-14);
        assertEquals(1.0 / 5, logInt.getB(4, 1.0), 1e-14);
        assertEquals(1.0 / 6, logInt.getB(5, 1.0), 1e-14);
        assertEquals(1.0 / 7, logInt.getB(6, 1.0), 1e-14);
        assertEquals(1.0 / 8, logInt.getB(7, 1.0), 1e-14);
        assertEquals(1.0 / 9, logInt.getB(8, 1.0), 1e-14);
        assertEquals(1.0 / 10, logInt.getB(9, 1.0), 1e-14);
        assertEquals(1.0 / 11, logInt.getB(10, 1.0), 1e-14);
    }

    /**
     * Test getting max iterations.
     */
    @Test
    public void testGetMaxIterations() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        assertEquals(Integer.MAX_VALUE, logInt.getMaxIterations());
    }

    /**
     * Test setting max iterations.
     */
    @Test
    public void testSetMaxIterations() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        logInt.setMaxIterations(10);
        assertEquals(10, logInt.getMaxIterations());
    }

    /**
     * Test setting negative max iterations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIterations() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        logInt.setMaxIterations(-10);
    }

    /**
     * Test setting zero max iterations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroMaxIterations() {
        ContinuedFraction logInt = new LogIntContinuedFraction();
        logInt.setMaxIterations(0);
    }
}