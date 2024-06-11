package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c = new Complex(2.0, 3.0);
        String expected = "2.0 + 3.0i";
        String result = c.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        double expected = 5.0;
        double result = c.abs();
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(6.0, 8.0);
        a.plus(b);
        assertEquals(expected.re, a.re, 0.00001);
        assertEquals(expected.im, a.im, 0.00001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(4.0, 5.0);
        Complex b = new Complex(2.0, 3.0);
        Complex expected = new Complex(2.0, 2.0);
        a.minus(b);
        assertEquals(expected.re, a.re, 0.00001);
        assertEquals(expected.im, a.im, 0.00001);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(-7.0, 22.0);
        a.times(b);
        assertEquals(expected.re, a.re, 0.00001);
        assertEquals(expected.im, a.im, 0.00001);
    }

    @Test
    public void testTimesDouble() {
        Complex c = new Complex(2.0, 3.0);
        double alpha = 2.0;
        Complex expected = new Complex(4.0, 6.0);
        c.times(alpha);
        assertEquals(expected.re, c.re, 0.00001);
        assertEquals(expected.im, c.im, 0.00001);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        Complex expected = new Complex(2.0, -3.0);
        c.conjugate();
        assertEquals(expected.re, c.re, 0.00001);
        assertEquals(expected.im, c.im, 0.00001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(6.0, 8.0);
        Complex result = Complex.plus(a, b);
        assertEquals(expected.re, result.re, 0.00001);
        assertEquals(expected.im, result.im, 0.00001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex expected = new Complex(4.0, 6.0);
        Complex result = Complex.multiply(a, d);
        assertEquals(expected.re, result.re, 0.00001);
        assertEquals(expected.im, result.im, 0.00001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(-7.0, 22.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(expected.re, result.re, 0.00001);
        assertEquals(expected.im, result.im, 0.00001);
    }

}