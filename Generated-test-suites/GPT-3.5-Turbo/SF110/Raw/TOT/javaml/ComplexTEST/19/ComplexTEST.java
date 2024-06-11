package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex complex = new Complex(2.5, 3.7);
        assertEquals(2.5, complex.re, 0.001);
        assertEquals(3.7, complex.im, 0.001);
    }

    @Test
    public void testDefaultComplexConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.7);
        String expectedString = "2.5 + 3.7i";
        assertEquals(expectedString, complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expectedAbs = 5.0;
        assertEquals(expectedAbs, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, -0.5);
        complex1.plus(complex2);
        assertEquals(3.7, complex1.re, 0.001);
        assertEquals(3.2, complex1.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, -0.5);
        complex1.minus(complex2);
        assertEquals(1.3, complex1.re, 0.001);
        assertEquals(4.2, complex1.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -2.0);
        complex1.times(complex2);
        assertEquals(8.0, complex1.re, 0.001);
        assertEquals(-1.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesAlpha() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 2.5;
        complex.times(alpha);
        assertEquals(5.0, complex.re, 0.001);
        assertEquals(7.5, complex.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.5, 3.7);
        complex.conjugate();
        assertEquals(2.5, complex.re, 0.001);
        assertEquals(-3.7, complex.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, -0.5);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(3.7, result.re, 0.001);
        assertEquals(3.2, result.im, 0.001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex complex = new Complex(2.5, 3.7);
        double d = 2.0;
        Complex result = Complex.multiply(complex, d);
        assertEquals(5.0, result.re, 0.001);
        assertEquals(7.4, result.im, 0.001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -2.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(8.0, result.re, 0.001);
        assertEquals(-1.0, result.im, 0.001);
    }
}