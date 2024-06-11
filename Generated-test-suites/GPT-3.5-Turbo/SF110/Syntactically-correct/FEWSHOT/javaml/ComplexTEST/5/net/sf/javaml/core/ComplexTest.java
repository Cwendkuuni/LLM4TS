package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
        
        c = new Complex(-3.5, 0);
        assertEquals("-3.5 + 0.0i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.0001);
        
        c = new Complex(0, 0);
        assertEquals(0.0, c.abs(), 0.0001);
        
        c = new Complex(-2.5, -6.5);
        assertEquals(7.089, c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(-2.0, 1.0);
        a.plus(b);
        assertEquals(1.0, a.re, 0.0001);
        assertEquals(5.0, a.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(-2.0, 1.0);
        a.minus(b);
        assertEquals(5.0, a.re, 0.0001);
        assertEquals(3.0, a.im, 0.0001);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(-2.0, 1.0);
        a.times(b);
        assertEquals(-14.0, a.re, 0.0001);
        assertEquals(-5.0, a.im, 0.0001);
    }

    @Test
    public void testTimesDouble() {
        Complex a = new Complex(3.0, 4.0);
        a.times(2.0);
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
        Complex b = new Complex(-2.0, 1.0);
        Complex c = Complex.plus(a, b);
        assertEquals(1.0, c.re, 0.0001);
        assertEquals(5.0, c.im, 0.0001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(3.0, 4.0);
        Complex c = Complex.multiply(a, 2.5);
        assertEquals(7.5, c.re, 0.0001);
        assertEquals(10.0, c.im, 0.0001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(-2.0, 1.0);
        Complex c = Complex.multiply(a, b);
        assertEquals(-14.0, c.re, 0.0001);
        assertEquals(-5.0, c.im, 0.0001);
    }
}