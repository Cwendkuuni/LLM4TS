package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructorWithValues() {
        Complex complex = new Complex(2.5, 3.5);
        assertEquals(2.5, complex.re, 0.001);
        assertEquals(3.5, complex.im, 0.001);
    }

    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.5);
        assertEquals("2.5 + 3.5i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.plus(b);
        assertEquals(3.0, a.re, 0.001);
        assertEquals(5.0, a.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 5.0);
        Complex b = new Complex(1.0, 2.0);
        a.minus(b);
        assertEquals(2.0, a.re, 0.001);
        assertEquals(3.0, a.im, 0.001);
    }

    @Test
    public void testTimesWithComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.times(b);
        assertEquals(-4.0, a.re, 0.001);
        assertEquals(7.0, a.im, 0.001);
    }

    @Test
    public void testTimesWithDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 1.5;
        a.times(alpha);
        assertEquals(3.0, a.re, 0.001);
        assertEquals(4.5, a.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.001);
        assertEquals(-3.0, complex.im, 0.001);
    }

    @Test
    public void testStaticPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.plus(a, b);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(5.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiplyWithDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 1.5;
        Complex result = Complex.multiply(a, d);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(4.5, result.im, 0.001);
    }

    @Test
    public void testStaticMultiplyWithComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-4.0, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }
}