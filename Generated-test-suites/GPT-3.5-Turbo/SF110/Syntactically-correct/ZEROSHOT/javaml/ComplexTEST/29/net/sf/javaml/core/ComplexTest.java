package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.7);
        String expected = "2.5 + 3.7i";
        String actual = complex.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(2.5, 3.7);
        double expected = Math.sqrt(2.5 * 2.5 + 3.7 * 3.7);
        double actual = complex.abs();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, 2.3);
        Complex expected = new Complex(2.5 + 1.2, 3.7 + 2.3);
        complex1.plus(complex2);
        assertEquals(expected.re, complex1.re, 0.001);
        assertEquals(expected.im, complex1.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, 2.3);
        Complex expected = new Complex(2.5 - 1.2, 3.7 - 2.3);
        complex1.minus(complex2);
        assertEquals(expected.re, complex1.re, 0.001);
        assertEquals(expected.im, complex1.im, 0.001);
    }

    @Test
    public void testTimes() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, 2.3);
        Complex expected = new Complex(2.5 * 1.2 - 3.7 * 2.3, 2.5 * 2.3 + 3.7 * 1.2);
        complex1.times(complex2);
        assertEquals(expected.re, complex1.re, 0.001);
        assertEquals(expected.im, complex1.im, 0.001);
    }

    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(2.5, 3.7);
        double alpha = 1.5;
        Complex expected = new Complex(2.5 * alpha, 3.7 * alpha);
        complex.times(alpha);
        assertEquals(expected.re, complex.re, 0.001);
        assertEquals(expected.im, complex.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.5, 3.7);
        Complex expected = new Complex(2.5, -3.7);
        complex.conjugate();
        assertEquals(expected.re, complex.re, 0.001);
        assertEquals(expected.im, complex.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, 2.3);
        Complex expected = new Complex(2.5 + 1.2, 3.7 + 2.3);
        Complex actual = Complex.plus(complex1, complex2);
        assertEquals(expected.re, actual.re, 0.001);
        assertEquals(expected.im, actual.im, 0.001);
    }

    @Test
    public void testMultiplyStaticWithDouble() {
        Complex complex = new Complex(2.5, 3.7);
        double d = 1.5;
        Complex expected = new Complex(2.5 * d, 3.7 * d);
        Complex actual = Complex.multiply(complex, d);
        assertEquals(expected.re, actual.re, 0.001);
        assertEquals(expected.im, actual.im, 0.001);
    }

    @Test
    public void testMultiplyStaticWithComplex() {
        Complex complex1 = new Complex(2.5, 3.7);
        Complex complex2 = new Complex(1.2, 2.3);
        Complex expected = new Complex(2.5 * 1.2 - 3.7 * 2.3, 2.5 * 2.3 + 3.7 * 1.2);
        Complex actual = Complex.multiply(complex1, complex2);
        assertEquals(expected.re, actual.re, 0.001);
        assertEquals(expected.im, actual.im, 0.001);
    }

}