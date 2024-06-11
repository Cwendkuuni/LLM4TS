package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.plus(complex2);
        assertEquals(3.0, complex1.re, 0.001);
        assertEquals(5.0, complex1.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.minus(complex2);
        assertEquals(1.0, complex1.re, 0.001);
        assertEquals(1.0, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.times(complex2);
        assertEquals(-4.0, complex1.re, 0.001);
        assertEquals(7.0, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(2.0);
        assertEquals(4.0, complex.re, 0.001);
        assertEquals(6.0, complex.im, 0.001);
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
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(5.0, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex complex = new Complex(2.5, 3.5);
        Complex result = Complex.multiply(complex, 2.0);
        assertEquals(5.0, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-4.0, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }
}