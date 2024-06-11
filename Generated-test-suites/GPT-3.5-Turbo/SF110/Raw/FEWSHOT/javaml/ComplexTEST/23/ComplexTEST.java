package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", complex.toString());
        complex = new Complex(-4.5, 0.0);
        assertEquals("-4.5 + 0.0i", complex.toString());
        complex = new Complex(0.0, -2.5);
        assertEquals("0.0 + -2.5i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.00001);
        complex = new Complex(-2.0, 2.0);
        assertEquals(2.82842, complex.abs(), 0.00001);
        complex = new Complex(0.0, 0.0);
        assertEquals(0.0, complex.abs(), 0.00001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.plus(b);
        assertEquals(3.0, a.re, 0.00001);
        assertEquals(5.0, a.im, 0.00001);
        a = new Complex(-4.0, -5.0);
        b = new Complex(-1.0, -2.0);
        a.plus(b);
        assertEquals(-5.0, a.re, 0.00001);
        assertEquals(-7.0, a.im, 0.00001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.minus(b);
        assertEquals(1.0, a.re, 0.00001);
        assertEquals(1.0, a.im, 0.00001);
        a = new Complex(-4.0, -5.0);
        b = new Complex(-1.0, -2.0);
        a.minus(b);
        assertEquals(-3.0, a.re, 0.00001);
        assertEquals(-3.0, a.im, 0.00001);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.times(b);
        assertEquals(-4.0, a.re, 0.00001);
        assertEquals(7.0, a.im, 0.00001);
        a = new Complex(-4.0, -5.0);
        b = new Complex(-1.0, -2.0);
        a.times(b);
        assertEquals(-6.0, a.re, 0.00001);
        assertEquals(3.0, a.im, 0.00001);
    }

    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 2.5;
        a.times(alpha);
        assertEquals(5.0, a.re, 0.00001);
        assertEquals(7.5, a.im, 0.00001);
        a = new Complex(-4.0, -5.0);
        alpha = -0.5;
        a.times(alpha);
        assertEquals(2.0, a.re, 0.00001);
        assertEquals(2.5, a.im, 0.00001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.00001);
        assertEquals(-3.0, complex.im, 0.00001);
        complex = new Complex(-4.0, -5.0);
        complex.conjugate();
        assertEquals(-4.0, complex.re, 0.00001);
        assertEquals(5.0, complex.im, 0.00001);
    }

    @Test
    public void testPlusComplexComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.plus(a, b);
        assertEquals(3.0, result.re, 0.00001);
        assertEquals(5.0, result.im, 0.00001);
        a = new Complex(-4.0, -5.0);
        b = new Complex(-1.0, -2.0);
        result = Complex.plus(a, b);
        assertEquals(-5.0, result.re, 0.00001);
        assertEquals(-7.0, result.im, 0.00001);
    }

    @Test
    public void testMultiplyComplexDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.5;
        Complex result = Complex.multiply(a, d);
        assertEquals(5.0, result.re, 0.00001);
        assertEquals(7.5, result.im, 0.00001);
        a = new Complex(-4.0, -5.0);
        d = -0.5;
        result = Complex.multiply(a, d);
        assertEquals(2.0, result.re, 0.00001);
        assertEquals(2.5, result.im, 0.00001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-4.0, result.re, 0.00001);
        assertEquals(7.0, result.im, 0.00001);
        a = new Complex(-4.0, -5.0);
        b = new Complex(-1.0, -2.0);
        result = Complex.multiply(a, b);
        assertEquals(-6.0, result.re, 0.00001);
        assertEquals(3.0, result.im, 0.00001);
    }
}