package net.sf.javaml.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexTest {
    private Complex complex;

    @Before
    public void setUp() {
        complex = new Complex(1.0, 2.0);
    }

    @After
    public void tearDown() {
        complex = null;
    }

    @Test
    public void testToString() {
        String expected = "1.0 + 2.0i";
        String actual = complex.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAbs() {
        double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
        double actual = complex.abs();
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testPlus() {
        Complex b = new Complex(3.0, 4.0);
        complex.plus(b);
        double expectedRe = 1.0 + 3.0;
        double expectedIm = 2.0 + 4.0;
        assertEquals(expectedRe, complex.re, 0.00001);
        assertEquals(expectedIm, complex.im, 0.00001);
    }

    @Test
    public void testMinus() {
        Complex b = new Complex(3.0, 4.0);
        complex.minus(b);
        double expectedRe = 1.0 - 3.0;
        double expectedIm = 2.0 - 4.0;
        assertEquals(expectedRe, complex.re, 0.00001);
        assertEquals(expectedIm, complex.im, 0.00001);
    }

    @Test
    public void testTimesWithComplex() {
        Complex b = new Complex(3.0, 4.0);
        complex.times(b);
        double expectedRe = 1.0 * 3.0 - 2.0 * 4.0;
        double expectedIm = 1.0 * 4.0 + 2.0 * 3.0;
        assertEquals(expectedRe, complex.re, 0.00001);
        assertEquals(expectedIm, complex.im, 0.00001);
    }

    @Test
    public void testTimesWithDouble() {
        double alpha = 2.0;
        complex.times(alpha);
        double expectedRe = 1.0 * 2.0;
        double expectedIm = 2.0 * 2.0;
        assertEquals(expectedRe, complex.re, 0.00001);
        assertEquals(expectedIm, complex.im, 0.00001);
    }

    @Test
    public void testConjugate() {
        complex.conjugate();
        double expectedIm = -2.0;
        assertEquals(expectedIm, complex.im, 0.00001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.plus(a, b);
        double expectedRe = 1.0 + 3.0;
        double expectedIm = 2.0 + 4.0;
        assertEquals(expectedRe, result.re, 0.00001);
        assertEquals(expectedIm, result.im, 0.00001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex a = new Complex(1.0, 2.0);
        double d = 2.0;
        Complex result = Complex.multiply(a, d);
        double expectedRe = 1.0 * 2.0;
        double expectedIm = 2.0 * 2.0;
        assertEquals(expectedRe, result.re, 0.00001);
        assertEquals(expectedIm, result.im, 0.00001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(a, b);
        double expectedRe = 1.0 * 3.0 - 2.0 * 4.0;
        double expectedIm = 1.0 * 4.0 + 2.0 * 3.0;
        assertEquals(expectedRe, result.re, 0.00001);
        assertEquals(expectedIm, result.im, 0.00001);
    }
}