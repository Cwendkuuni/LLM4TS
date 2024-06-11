package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.7);
        assertEquals("2.5 + 3.7i", complex.toString());
        
        complex = new Complex(-1.0, 0.0);
        assertEquals("-1.0 + 0.0i", complex.toString());
        
        complex = new Complex(0.0, -2.8);
        assertEquals("0.0 + -2.8i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0001);
        
        complex = new Complex(-2.0, 2.0);
        assertEquals(2.8284, complex.abs(), 0.0001);
        
        complex = new Complex(-5.5, -3.8);
        assertEquals(6.6509, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.5);
        Complex b = new Complex(1.5, -2.0);
        
        a.plus(b);
        assertEquals(3.5, a.re, 0.0001);
        assertEquals(1.5, a.im, 0.0001);
        
        a = new Complex(-4.0, -1.0);
        b = new Complex(2.5, 2.0);
        
        a.plus(b);
        assertEquals(-1.5, a.re, 0.0001);
        assertEquals(1.0, a.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 4.5);
        Complex b = new Complex(2.0, 1.5);
        
        a.minus(b);
        assertEquals(1.0, a.re, 0.0001);
        assertEquals(3.0, a.im, 0.0001);
        
        a = new Complex(-5.0, 2.5);
        b = new Complex(1.5, -3.5);
        
        a.minus(b);
        assertEquals(-6.5, a.re, 0.0001);
        assertEquals(6.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimes() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, -2.0);
        
        a.times(b);
        assertEquals(8.0, a.re, 0.0001);
        assertEquals(-1.0, a.im, 0.0001);
        
        a = new Complex(-1.5, 2.5);
        b = new Complex(2.0, -1.0);
        
        a.times(b);
        assertEquals(0.5, a.re, 0.0001);
        assertEquals(-7.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(3.0, 2.0);
        complex.times(2.5);
        assertEquals(7.5, complex.re, 0.0001);
        assertEquals(5.0, complex.im, 0.0001);
        
        complex = new Complex(-1.5, -2.0);
        complex.times(-0.5);
        assertEquals(0.75, complex.re, 0.0001);
        assertEquals(1.0, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, -3.5);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.0001);
        assertEquals(3.5, complex.im, 0.0001);
        
        complex = new Complex(-1.0, 2.0);
        complex.conjugate();
        assertEquals(-1.0, complex.re, 0.0001);
        assertEquals(-2.0, complex.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, -2.0);
        
        Complex result = Complex.plus(a, b);
        assertEquals(3.0, result.re, 0.0001);
        assertEquals(1.0, result.im, 0.0001);
        
        a = new Complex(-1.5, -2.5);
        b = new Complex(2.0, -1.0);
        
        result = Complex.plus(a, b);
        assertEquals(0.5, result.re, 0.0001);
        assertEquals(-3.5, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyWithDouble() {
        Complex a = new Complex(1.5, -2.0);
        Complex result = Complex.multiply(a, 2.0);
        assertEquals(3.0, result.re, 0.0001);
        assertEquals(-4.0, result.im, 0.0001);
        
        a = new Complex(2.0, 3.5);
        result = Complex.multiply(a, -0.5);
        assertEquals(-1.0, result.re, 0.0001);
        assertEquals(-1.75, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyWithComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, -2.0);
        
        Complex result = Complex.multiply(a, b);
        assertEquals(8.0, result.re, 0.0001);
        assertEquals(-1.0, result.im, 0.0001);
        
        a = new Complex(-1.5, 2.5);
        b = new Complex(2.0, -1.0);
        
        result = Complex.multiply(a, b);
        assertEquals(0.5, result.re, 0.0001);
        assertEquals(-7.0, result.im, 0.0001);
    }
}