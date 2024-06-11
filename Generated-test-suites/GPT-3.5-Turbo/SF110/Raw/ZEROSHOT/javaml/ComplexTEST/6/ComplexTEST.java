package net.sf.javaml.core;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c = new Complex(3.0, 4.0);
        String expected = "3.0 + 4.0i";
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
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 3.0);
        a.plus(b);
        assertEquals(5.0, a.re, 0.0001);
        assertEquals(7.0, a.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 3.0);
        a.minus(b);
        assertEquals(1.0, a.re, 0.0001);
        assertEquals(1.0, a.im, 0.0001);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 3.0);
        a.times(b);
        assertEquals(-6.0, a.re, 0.0001);
        assertEquals(17.0, a.im, 0.0001);
    }

    @Test
    public void testTimesDouble() {
        Complex a = new Complex(3.0, 4.0);
        double alpha = 2.0;
        a.times(alpha);
        assertEquals(6.0, a.re, 0.0001);
        assertEquals(8.0, a.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex a = new Complex(3.0, 4.0);
        a.conjugate();
        assertEquals(3.0, a.re, 0.0001);
        assertEquals(-4.0, a.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 3.0);
        Complex c = Complex.plus(a, b);
        assertEquals(5.0, c.re, 0.0001);
        assertEquals(7.0, c.im, 0.0001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(3.0, 4.0);
        double d = 2.0;
        Complex c = Complex.multiply(a, d);
        assertEquals(6.0, c.re, 0.0001);
        assertEquals(8.0, c.im, 0.0001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 3.0);
        Complex c = Complex.multiply(a, b);
        assertEquals(-6.0, c.re, 0.0001);
        assertEquals(17.0, c.im, 0.0001);
    }
}