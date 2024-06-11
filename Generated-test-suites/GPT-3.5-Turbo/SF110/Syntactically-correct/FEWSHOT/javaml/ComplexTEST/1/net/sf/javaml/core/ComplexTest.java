package net.sf.javaml.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c1 = new Complex(2.5, 3.5);
        assertEquals("2.5 + 3.5i", c1.toString());

        Complex c2 = new Complex(-1.5, -2.5);
        assertEquals("-1.5 + -2.5i", c2.toString());

        Complex c3 = new Complex(0.0, 0.0);
        assertEquals("0.0 + 0.0i", c3.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(3.0, 4.0);
        assertEquals(5.0, c1.abs(), 0.0001);

        Complex c2 = new Complex(-3.0, -4.0);
        assertEquals(5.0, c2.abs(), 0.0001);

        Complex c3 = new Complex(0.0, 0.0);
        assertEquals(0.0, c3.abs(), 0.0001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        c1.plus(c2);
        assertEquals(3.0, c1.re, 0.0001);
        assertEquals(5.0, c1.im, 0.0001);

        Complex c3 = new Complex(-3.0, -4.0);
        Complex c4 = new Complex(-2.0, -3.0);
        c3.plus(c4);
        assertEquals(-5.0, c3.re, 0.0001);
        assertEquals(-7.0, c3.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        c1.minus(c2);
        assertEquals(1.0, c1.re, 0.0001);
        assertEquals(1.0, c1.im, 0.0001);

        Complex c3 = new Complex(-3.0, -4.0);
        Complex c4 = new Complex(-2.0, -3.0);
        c3.minus(c4);
        assertEquals(-1.0, c3.re, 0.0001);
        assertEquals(-1.0, c3.im, 0.0001);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        c1.times(c2);
        assertEquals(-4.0, c1.re, 0.0001);
        assertEquals(7.0, c1.im, 0.0001);

        Complex c3 = new Complex(-3.0, -4.0);
        Complex c4 = new Complex(-2.0, -3.0);
        c3.times(c4);
        assertEquals(-5.0, c3.re, 0.0001);
        assertEquals(18.0, c3.im, 0.0001);
    }

    @Test
    public void testTimesWithAlpha() {
        Complex c1 = new Complex(2.0, 3.0);
        c1.times(2.0);
        assertEquals(4.0, c1.re, 0.0001);
        assertEquals(6.0, c1.im, 0.0001);

        Complex c2 = new Complex(-3.0, -4.0);
        c2.times(0.5);
        assertEquals(-1.5, c2.re, 0.0001);
        assertEquals(-2.0, c2.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(2.0, 3.0);
        c1.conjugate();
        assertEquals(2.0, c1.re, 0.0001);
        assertEquals(-3.0, c1.im, 0.0001);

        Complex c2 = new Complex(-3.0, -4.0);
        c2.conjugate();
        assertEquals(-3.0, c2.re, 0.0001);
        assertEquals(4.0, c2.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        Complex result = Complex.plus(c1, c2);
        assertEquals(3.0, result.re, 0.0001);
        assertEquals(5.0, result.im, 0.0001);

        Complex c3 = new Complex(-3.0, -4.0);
        Complex c4 = new Complex(-2.0, -3.0);
        Complex result2 = Complex.plus(c3, c4);
        assertEquals(-5.0, result2.re, 0.0001);
        assertEquals(-7.0, result2.im, 0.0001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(c1, 2.0);
        assertEquals(4.0, result.re, 0.0001);
        assertEquals(6.0, result.im, 0.0001);

        Complex c2 = new Complex(-3.0, -4.0);
        Complex result2 = Complex.multiply(c2, 0.5);
        assertEquals(-1.5, result2.re, 0.0001);
        assertEquals(-2.0, result2.im, 0.0001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(c1, c2);
        assertEquals(-4.0, result.re, 0.0001);
        assertEquals(7.0, result.im, 0.0001);

        Complex c3 = new Complex(-3.0, -4.0);
        Complex c4 = new Complex(-2.0, -3.0);
        Complex result2 = Complex.multiply(c3, c4);
        assertEquals(-5.0, result2.re, 0.0001);
        assertEquals(18.0, result2.im, 0.0001);
    }
}