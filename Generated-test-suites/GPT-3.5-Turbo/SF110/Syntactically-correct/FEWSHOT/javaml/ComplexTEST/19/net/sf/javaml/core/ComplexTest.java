package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        String expected = "3.0 + 4.0i";
        assertEquals(expected, complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        assertEquals(expected, complex.abs(), 0.0001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.plus(complex2);
        Complex expected = new Complex(5.0, 7.0);
        assertEquals(expected.re, complex1.re, 0.0001);
        assertEquals(expected.im, complex1.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.minus(complex2);
        Complex expected = new Complex(1.0, 1.0);
        assertEquals(expected.re, complex1.re, 0.0001);
        assertEquals(expected.im, complex1.im, 0.0001);
    }

    @Test
    public void testTimesWithComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.times(complex2);
        Complex expected = new Complex(-6.0, 17.0);
        assertEquals(expected.re, complex1.re, 0.0001);
        assertEquals(expected.im, complex1.im, 0.0001);
    }

    @Test
    public void testTimesWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.0);
        Complex expected = new Complex(6.0, 8.0);
        assertEquals(expected.re, complex.re, 0.0001);
        assertEquals(expected.im, complex.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        Complex expected = new Complex(3.0, -4.0);
        assertEquals(expected.re, complex.re, 0.0001);
        assertEquals(expected.im, complex.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.plus(complex1, complex2);
        Complex expected = new Complex(5.0, 7.0);
        assertEquals(expected.re, result.re, 0.0001);
        assertEquals(expected.im, result.im, 0.0001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex, 2.0);
        Complex expected = new Complex(6.0, 8.0);
        assertEquals(expected.re, result.re, 0.0001);
        assertEquals(expected.im, result.im, 0.0001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex1, complex2);
        Complex expected = new Complex(-6.0, 17.0);
        assertEquals(expected.re, result.re, 0.0001);
        assertEquals(expected.im, result.im, 0.0001);
    }

}