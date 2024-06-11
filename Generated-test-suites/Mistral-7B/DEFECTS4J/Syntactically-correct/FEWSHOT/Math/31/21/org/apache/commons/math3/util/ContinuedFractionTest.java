package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

public class ContinuedFractionTest {

    /**
     * Test for evaluation of {@code sine} continued fraction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testSine() {
        ContinuedFraction cf = new SineContinuedFraction();
        assertEquals(Math.sin(1.0), cf.evaluate(1.0, 1e-6), 1e-6);
        assertEquals(Math.sin(0.5), cf.evaluate(0.5, 1e-6), 1e-6);
        assertEquals(Math.sin(Math.PI / 2), cf.evaluate(Math.PI / 2, 1e-6), 1e-6);
        assertEquals(Math.sin(Math.PI), cf.evaluate(Math.PI, 1e-6), 1e-6);
        assertEquals(Math.sin(Math.PI * 1.5), cf.evaluate(Math.PI * 1.5, 1e-6), 1e-6);

        // Test with BigDecimal input
        BigDecimal x = new BigDecimal("1.000000000000000123456789");
        BigDecimal result = new BigDecimal("0.00001469553638128551");
        assertEquals(result, cf.evaluate(x.doubleValue(), new BigDecimal("1e-6")));
    }

    /**
     * Test for evaluation of {@code cosine} continued fraction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testCosine() {
        ContinuedFraction cf = new CosineContinuedFraction();
        assertEquals(Math.cos(1.0), cf.evaluate(1.0, 1e-6), 1e-6);
        assertEquals(Math.cos(0.5), cf.evaluate(0.5, 1e-6), 1e-6);
        assertEquals(Math.cos(Math.PI / 2), cf.evaluate(Math.PI / 2, 1e-6), 1e-6);
        assertEquals(Math.cos(Math.PI), cf.evaluate(Math.PI, 1e-6), 1e-6);
        assertEquals(Math.cos(Math.PI * 1.5), cf.evaluate(Math.PI * 1.5, 1e-6), 1e-6);

        // Test with BigDecimal input
        BigDecimal x = new BigDecimal("1.000000000000000123456789");
        BigDecimal result = new BigDecimal("0.999998931967237");
        assertEquals(result, cf.evaluate(x.doubleValue(), new BigDecimal("1e-6")));
    }

    /**
     * Test for evaluation of {@code tangent} continued fraction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testTangent() {
        ContinuedFraction cf = new TangentContinuedFraction();
        assertEquals(Math.tan(1.0), cf.evaluate(1.0, 1e-6), 1e-6);
        assertEquals(Math.tan(0.5), cf.evaluate(0.5, 1e-6), 1e-6);
        assertEquals(Math.tan(Math.PI / 4), cf.evaluate(Math.PI / 4, 1e-6), 1e-6);
        assertEquals(Math.tan(Math.PI / 2), cf.evaluate(Math.PI / 2, 1e-6), 1e-6);
        assertEquals(Math.tan(Math.PI), cf.evaluate(Math.PI, 1e-6), 1e-6);

        // Test with BigDecimal input
        BigDecimal x = new BigDecimal("1.000000000000000123456789");
        BigDecimal result = new BigDecimal("1.55740772471342255699");
        assertEquals(result, cf.evaluate(x.doubleValue(), new BigDecimal("1e-6")));
    }

    /**
     * Test for evaluation of {@code cotangent} continued fraction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testCotangent() {
        ContinuedFraction cf = new CotangentContinuedFraction();
        assertEquals(1.0 / Math.tan(1.0), cf.evaluate(1.0, 1e-6), 1e-6);
        assertEquals(1.0 / Math.tan(0.5), cf.evaluate(0.5, 1e-6), 1e-6);
        assertEquals(1.0 / Math.tan(Math.PI / 4), cf.evaluate(Math.PI / 4, 1e-6), 1e-6);
        assertEquals(1.0 / Math.tan(Math.PI / 2), cf.evaluate(Math.PI / 2, 1e-6), 1e-6);
        assertEquals(-1.0 / Math.tan(Math.PI), cf.evaluate(Math.PI, 1e-6), 1e-6);

        // Test with BigDecimal input
        BigDecimal x = new BigDecimal("1.000000000000000123456789");
        BigDecimal result = new BigDecimal("0.999989271063505");
        assertEquals(result, cf.evaluate(x.doubleValue(), new BigDecimal("1e-6")));
    }

    /**
     * Test for evaluation of {@code gamma} continued fraction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGamma() {
        ContinuedFraction cf = new GammaContinuedFraction();
        assertEquals(Math.E, cf.evaluate(1.0, 1e-6), 1e-6);
        assertEquals(BesselFunctions.gamma(2), cf.evaluate(2.0, 1e-6), 1e-6);
        assertEquals(BesselFunctions.gamma(3), cf.evaluate(3.0, 1e-6), 1e-6);
        assertEquals(BesselFunctions.gamma(1.5), cf.evaluate(1.5, 1e-6), 1e-6);
        assertEquals(BesselFunctions.gamma(0.5), cf.evaluate(0.5, 1e-6), 1e-6);

        // Test with BigDecimal input
        BigDecimal x = new BigDecimal("2.500000000000000123456789");
        BigDecimal result = new BigDecimal("1.32353892356195885654");
        assertEquals(result, cf.evaluate(x.doubleValue(), new BigDecimal("1e-6")));
    }

    /**
     * Test for evaluation of {@code logarithm base 2} continued fraction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testLog2() {
        ContinuedFraction cf = new Log2ContinuedFraction();
        assertEquals(Math.log(2.0) / Math.log(2), cf.evaluate(2.0, 1e-6), 1e-6);
        assertEquals(Math.log(0.5) / Math.log(2), cf.evaluate(0.5, 1e-6), 1e-6);
        assertEquals(Math.log(Math.E) / Math.log(2), cf.evaluate(Math.E, 1e-6), 1e-6);
        assertEquals(Math.log(1 + Math.SQRT2) / Math.log(2), cf.evaluate(1 + Math.SQRT2, 1e-6), 1e-6);

        // Test with BigDecimal input
        BigDecimal x = new BigDecimal("2.718281828459045123456789");
        BigDecimal result = new BigDecimal("1.44269504088891101530");
        assertEquals(result, cf.evaluate(x.doubleValue(), new BigDecimal("1e-6")));
    }

    /**
     * Test for evaluation of {@code logarithm base e} continued fraction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testLogE() {
        ContinuedFraction cf = new LogEContinuedFraction();
        assertEquals(Math.log(Math.E), cf.evaluate(Math.E, 1e-6), 1e-6);
        assertEquals(Math.log(2.0), cf.evaluate(2.0, 1e-6), 1e-6);
        assertEquals(Math.log(Math.PI), cf.evaluate(Math.PI, 1e-6), 1e-6);

        // Test with BigDecimal input
        BigDecimal x = new BigDecimal("1.718281828459045123456789");
        BigDecimal result = new BigDecimal("0.57721566490153286061");
        assertEquals(result, cf.evaluate(x.doubleValue(), new BigDecimal("1e-6")));
    }

    /**
     * Test for {@code evaluate} method with invalid inputs.
     * @throws Exception if an error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateInvalidInputs() {
        ContinuedFraction cf = new SineContinuedFraction();
        cf.evaluate(-1.0, 1e-6);
    }

    /**
     * Test for {@code evaluate} method with {@code ConvergenceException}.
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new GammaContinuedFraction();
        cf.evaluate(0.0, 1e-6);
    }

    /**
     * Test for {@code evaluate} method with {@code MaxCountExceededException}.
     * @throws Exception if an error occurs.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new GammaContinuedFraction();
        cf.evaluate(2.0, 1, 10);
    }

}