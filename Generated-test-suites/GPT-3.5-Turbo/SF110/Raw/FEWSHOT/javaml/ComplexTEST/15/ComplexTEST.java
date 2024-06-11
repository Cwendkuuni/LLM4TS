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
        assertEquals(5.0, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        a.plus(b);
        assertEquals(4.0, a.re, 0.0001);
        assertEquals(6.0, a.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        a.minus(b);
        assertEquals(-2.0, a.re, 0.0001);
        assertEquals(-2.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        a.times(b);
        assertEquals(-5.0, a.re, 0.0001);
        assertEquals(10.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(1.0, 2.0);
        a.times(2.0);
        assertEquals(2.0, a.re, 0.0001);
        assertEquals(4.0, a.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(1.0, 2.0);
        complex.conjugate();
        assertEquals(1.0, complex.im, 0.0001);
    }
    
    @Test
    public void testPlusComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.plus(a, b);
        assertEquals(4.0, result.re, 0.0001);
        assertEquals(6.0, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(a, 2.0);
        assertEquals(2.0, result.re, 0.0001);
        assertEquals(4.0, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-5.0, result.re, 0.0001);
        assertEquals(10.0, result.im, 0.0001);
    }
}