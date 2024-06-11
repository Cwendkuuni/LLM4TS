package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testToString() {
        Complex c = new Complex(3.5, 2.7);
        assertEquals("3.5 + 2.7i", c.toString());
        c = new Complex(-1.2, -4.6);
        assertEquals("-1.2 + -4.6i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.0001);
        c = new Complex(-2.5, 1.5);
        assertEquals(2.915475, c.abs(), 0.0001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(2.5, 1.7);
        Complex b = new Complex(1.3, -3.2);
        a.plus(b);
        assertEquals(3.8, a.re, 0.0001);
        assertEquals(-1.5, a.im, 0.0001);
        a = new Complex(-1.0, 2.0);
        b = new Complex(0.0, 0.0);
        a.plus(b);
        assertEquals(-1.0, a.re, 0.0001);
        assertEquals(2.0, a.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(2.5, 1.7);
        Complex b = new Complex(1.3, -3.2);
        a.minus(b);
        assertEquals(1.2, a.re, 0.0001);
        assertEquals(4.9, a.im, 0.0001);
        a = new Complex(-1.0, 2.0);
        b = new Complex(0.0, 0.0);
        a.minus(b);
        assertEquals(-1.0, a.re, 0.0001);
        assertEquals(2.0, a.im, 0.0001);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.times(b);
        assertEquals(-4.0, a.re, 0.0001);
        assertEquals(7.0, a.im, 0.0001);
        a = new Complex(0.0, 0.0);
        b = new Complex(1.0, 2.0);
        a.times(b);
        assertEquals(0.0, a.re, 0.0001);
        assertEquals(0.0, a.im, 0.0001);
    }

    @Test
    public void testTimesDouble() {
        Complex c = new Complex(2.0, 3.0);
        c.times(2.5);
        assertEquals(5.0, c.re, 0.0001);
        assertEquals(7.5, c.im, 0.0001);
        c = new Complex(0.0, 0.0);
        c.times(-1.5);
        assertEquals(0.0, c.re, 0.0001);
        assertEquals(0.0, c.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        c.conjugate();
        assertEquals(2.0, c.re, 0.0001);
        assertEquals(-3.0, c.im, 0.0001);
        c = new Complex(0.0, 0.0);
        c.conjugate();
        assertEquals(0.0, c.re, 0.0001);
        assertEquals(0.0, c.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.5, 1.7);
        Complex b = new Complex(1.3, -3.2);
        Complex c = Complex.plus(a, b);
        assertEquals(3.8, c.re, 0.0001);
        assertEquals(-1.5, c.im, 0.0001);
        a = new Complex(-1.0, 2.0);
        b = new Complex(0.0, 0.0);
        c = Complex.plus(a, b);
        assertEquals(-1.0, c.re, 0.0001);
        assertEquals(2.0, c.im, 0.0001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = Complex.multiply(a, 2.5);
        assertEquals(5.0, b.re, 0.0001);
        assertEquals(7.5, b.im, 0.0001);
        a = new Complex(0.0, 0.0);
        b = Complex.multiply(a, -1.5);
        assertEquals(0.0, b.re, 0.0001);
        assertEquals(0.0, b.im, 0.0001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex c = Complex.multiply(a, b);
        assertEquals(-4.0, c.re, 0.0001);
        assertEquals(7.0, c.im, 0.0001);
        a = new Complex(0.0, 0.0);
        b = new Complex(1.0, 2.0);
        c = Complex.multiply(a, b);
        assertEquals(0.0, c.re, 0.0001);
        assertEquals(0.0, c.im, 0.0001);
    }
}