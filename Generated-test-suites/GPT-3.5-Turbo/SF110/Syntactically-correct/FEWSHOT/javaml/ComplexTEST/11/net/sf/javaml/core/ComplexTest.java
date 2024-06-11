package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testToString() {
        Complex complex = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.plus(complex2);
        assertEquals(4.0, complex1.re, 0.001);
        assertEquals(6.0, complex1.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(5.0, 7.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.minus(complex2);
        assertEquals(2.0, complex1.re, 0.001);
        assertEquals(3.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.times(complex2);
        assertEquals(-7.0, complex1.re, 0.001);
        assertEquals(22.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(1.5);
        assertEquals(3.0, complex.re, 0.001);
        assertEquals(4.5, complex.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.001);
        assertEquals(-3.0, complex.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex, 1.5);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(4.5, result.im, 0.001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-7.0, result.re, 0.001);
        assertEquals(22.0, result.im, 0.001);
    }
}