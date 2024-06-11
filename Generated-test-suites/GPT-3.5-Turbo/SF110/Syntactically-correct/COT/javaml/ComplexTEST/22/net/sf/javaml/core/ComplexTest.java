package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        String expected = "3.0 + 4.0i";
        String actual = complex.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        double actual = complex.abs();
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.plus(complex2);
        double expectedReal = 5.0;
        double expectedImag = 7.0;
        assertEquals(expectedReal, complex1.re, 0.0001);
        assertEquals(expectedImag, complex1.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.minus(complex2);
        double expectedReal = 1.0;
        double expectedImag = 1.0;
        assertEquals(expectedReal, complex1.re, 0.0001);
        assertEquals(expectedImag, complex1.im, 0.0001);
    }

    @Test
    public void testTimes() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.times(complex2);
        double expectedReal = -6.0;
        double expectedImag = 17.0;
        assertEquals(expectedReal, complex1.re, 0.0001);
        assertEquals(expectedImag, complex1.im, 0.0001);
    }

    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(3.0, 4.0);
        double alpha = 2.0;
        complex.times(alpha);
        double expectedReal = 6.0;
        double expectedImag = 8.0;
        assertEquals(expectedReal, complex.re, 0.0001);
        assertEquals(expectedImag, complex.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        double expectedImag = -4.0;
        assertEquals(expectedImag, complex.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.plus(complex1, complex2);
        double expectedReal = 5.0;
        double expectedImag = 7.0;
        assertEquals(expectedReal, result.re, 0.0001);
        assertEquals(expectedImag, result.im, 0.0001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        double d = 2.0;
        Complex result = Complex.multiply(complex, d);
        double expectedReal = 6.0;
        double expectedImag = 8.0;
        assertEquals(expectedReal, result.re, 0.0001);
        assertEquals(expectedImag, result.im, 0.0001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex1, complex2);
        double expectedReal = -6.0;
        double expectedImag = 17.0;
        assertEquals(expectedReal, result.re, 0.0001);
        assertEquals(expectedImag, result.im, 0.0001);
    }
}