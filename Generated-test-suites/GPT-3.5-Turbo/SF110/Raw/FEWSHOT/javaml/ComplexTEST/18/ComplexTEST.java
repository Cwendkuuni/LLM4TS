package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComplexTest {
    @Test
    public void testConstructor() {
        Complex complex = new Complex(3.5, 2.5);
        assertEquals(3.5, complex.re, 0.0001);
        assertEquals(2.5, complex.im, 0.0001);
    }
    
    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.0001);
        assertEquals(0.0, complex.im, 0.0001);
    }
    
    @Test
    public void testToString() {
        Complex complex = new Complex(3.5, 2.5);
        String expected = "3.5 + 2.5i";
        assertEquals(expected, complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(3.0, 2.0);
        Complex b = new Complex(1.5, -0.5);
        Complex expected = new Complex(4.5, 1.5);
        a.plus(b);
        assertEquals(expected.re, a.re, 0.0001);
        assertEquals(expected.im, a.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 2.0);
        Complex b = new Complex(1.5, -0.5);
        Complex expected = new Complex(1.5, 2.5);
        a.minus(b);
        assertEquals(expected.re, a.re, 0.0001);
        assertEquals(expected.im, a.im, 0.0001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(3.0, 2.0);
        Complex b = new Complex(1.5, -0.5);
        Complex expected = new Complex(4.5, 4.0);
        a.times(b);
        assertEquals(expected.re, a.re, 0.0001);
        assertEquals(expected.im, a.im, 0.0001);
    }
    
    @Test
    public void testTimesAlpha() {
        Complex complex = new Complex(3.0, 2.0);
        double alpha = 2.0;
        Complex expected = new Complex(6.0, 4.0);
        complex.times(alpha);
        assertEquals(expected.re, complex.re, 0.0001);
        assertEquals(expected.im, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 2.0);
        Complex expected = new Complex(3.0, -2.0);
        complex.conjugate();
        assertEquals(expected.re, complex.re, 0.0001);
        assertEquals(expected.im, complex.im, 0.0001);
    }
    
    @Test
    public void testStaticPlus() {
        Complex a = new Complex(3.0, 2.0);
        Complex b = new Complex(1.5, -0.5);
        Complex expected = new Complex(4.5, 1.5);
        Complex result = Complex.plus(a, b);
        assertEquals(expected.re, result.re, 0.0001);
        assertEquals(expected.im, result.im, 0.0001);
    }
    
    @Test
    public void testStaticMultiply_double() {
        Complex a = new Complex(3.0, 2.0);
        double d = 2.0;
        Complex expected = new Complex(6.0, 4.0);
        Complex result = Complex.multiply(a, d);
        assertEquals(expected.re, result.re, 0.0001);
        assertEquals(expected.im, result.im, 0.0001);
    }
    
    @Test
    public void testStaticMultiply_complex() {
        Complex a = new Complex(3.0, 2.0);
        Complex b = new Complex(1.5, -0.5);
        Complex expected = new Complex(4.5, 4.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(expected.re, result.re, 0.0001);
        assertEquals(expected.im, result.im, 0.0001);
    }
}