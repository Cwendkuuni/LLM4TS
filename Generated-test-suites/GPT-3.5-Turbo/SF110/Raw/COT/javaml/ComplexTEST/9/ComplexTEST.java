package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c = new Complex(2.5, 3.0);
        String expected = "2.5 + 3.0i";
        String actual = c.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        double expected = 5.0;
        double actual = c.abs();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(2.5, 3.0);
        Complex c2 = new Complex(1.5, 2.0);
        c1.plus(c2);
        double expectedRe = 4.0;
        double expectedIm = 5.0;
        assertEquals(expectedRe, c1.re, 0.0001);
        assertEquals(expectedIm, c1.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(5.0, 7.0);
        Complex c2 = new Complex(2.0, 3.0);
        c1.minus(c2);
        double expectedRe = 3.0;
        double expectedIm = 4.0;
        assertEquals(expectedRe, c1.re, 0.0001);
        assertEquals(expectedIm, c1.im, 0.0001);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        c1.times(c2);
        double expectedRe = -4.0;
        double expectedIm = 7.0;
        assertEquals(expectedRe, c1.re, 0.0001);
        assertEquals(expectedIm, c1.im, 0.0001);
    }

    @Test
    public void testTimesWithAlpha() {
        Complex c = new Complex(2.0, 3.0);
        c.times(2.0);
        double expectedRe = 4.0;
        double expectedIm = 6.0;
        assertEquals(expectedRe, c.re, 0.0001);
        assertEquals(expectedIm, c.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        c.conjugate();
        double expectedRe = 2.0;
        double expectedIm = -3.0;
        assertEquals(expectedRe, c.re, 0.0001);
        assertEquals(expectedIm, c.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex c1 = new Complex(2.5, 3.0);
        Complex c2 = new Complex(1.5, 2.0);
        Complex result = Complex.plus(c1, c2);
        double expectedRe = 4.0;
        double expectedIm = 5.0;
        assertEquals(expectedRe, result.re, 0.0001);
        assertEquals(expectedIm, result.im, 0.0001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex c = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(c, 2.0);
        double expectedRe = 4.0;
        double expectedIm = 6.0;
        assertEquals(expectedRe, result.re, 0.0001);
        assertEquals(expectedIm, result.im, 0.0001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(c1, c2);
        double expectedRe = -4.0;
        double expectedIm = 7.0;
        assertEquals(expectedRe, result.re, 0.0001);
        assertEquals(expectedIm, result.im, 0.0001);
    }

}