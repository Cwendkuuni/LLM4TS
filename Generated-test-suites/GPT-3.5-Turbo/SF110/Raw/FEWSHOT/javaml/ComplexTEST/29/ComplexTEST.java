package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testToString() {
        Complex complex = new Complex(3.5, 2.8);
        assertEquals("3.5 + 2.8i", complex.toString());
        
        complex = new Complex(0.0, 0.0);
        assertEquals("0.0 + 0.0i", complex.toString());
        
        complex = new Complex(-1.5, 0.0);
        assertEquals("-1.5 + 0.0i", complex.toString());
        
        complex = new Complex(0.0, -1.5);
        assertEquals("0.0 + -1.5i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
        
        complex = new Complex(0.0, 0.0);
        assertEquals(0.0, complex.abs(), 0.001);
        
        complex = new Complex(-2.5, 0.0);
        assertEquals(2.5, complex.abs(), 0.001);
        
        complex = new Complex(0.0, -2.5);
        assertEquals(2.5, complex.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(3.5, 2.8);
        Complex complex2 = new Complex(1.2, -0.9);
        
        complex1.plus(complex2);
        assertEquals(4.7, complex1.re, 0.001);
        assertEquals(1.9, complex1.im, 0.001);
        
        complex1 = new Complex(0.0, 0.0);
        complex2.plus(complex1);
        assertEquals(1.2, complex2.re, 0.001);
        assertEquals(-0.9, complex2.im, 0.001);
        
        complex1.plus(null);
        assertEquals(4.7, complex1.re, 0.001);
        assertEquals(1.9, complex1.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.5, 2.8);
        Complex complex2 = new Complex(1.2, -0.9);
        
        complex1.minus(complex2);
        assertEquals(2.3, complex1.re, 0.001);
        assertEquals(3.7, complex1.im, 0.001);
        
        complex1 = new Complex(0.0, 0.0);
        complex2.minus(complex1);
        assertEquals(1.2, complex2.re, 0.001);
        assertEquals(-0.9, complex2.im, 0.001);
        
        complex1.minus(null);
        assertEquals(2.3, complex1.re, 0.001);
        assertEquals(3.7, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(3.5, 2.8);
        Complex complex2 = new Complex(1.2, -0.9);
        
        complex1.times(complex2);
        assertEquals(8.53, complex1.re, 0.001);
        assertEquals(1.79, complex1.im, 0.001);
        
        complex1 = new Complex(0.0, 0.0);
        complex2.times(complex1);
        assertEquals(0.0, complex2.re, 0.001);
        assertEquals(0.0, complex2.im, 0.001);
        
        complex1.times(null);
        assertEquals(8.53, complex1.re, 0.001);
        assertEquals(1.79, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(3.5, 2.8);
        
        complex.times(2.5);
        assertEquals(8.75, complex.re, 0.001);
        assertEquals(7.0, complex.im, 0.001);
        
        complex = new Complex(0.0, 0.0);
        complex.times(2.5);
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.5, -2.8);
        
        complex.conjugate();
        assertEquals(3.5, complex.re, 0.001);
        assertEquals(2.8, complex.im, 0.001);
        
        complex = new Complex(0.0, 0.0);
        complex.conjugate();
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(3.5, 2.8);
        Complex complex2 = new Complex(1.2, -0.9);
        
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(4.7, result.re, 0.001);
        assertEquals(1.9, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplexDouble() {
        Complex complex1 = new Complex(3.5, 2.8);
        
        Complex result = Complex.multiply(complex1, 2.5);
        assertEquals(8.75, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplexComplex() {
        Complex complex1 = new Complex(3.5, 2.8);
        Complex complex2 = new Complex(1.2, -0.9);
        
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(8.53, result.re, 0.001);
        assertEquals(1.79, result.im, 0.001);
    }
}