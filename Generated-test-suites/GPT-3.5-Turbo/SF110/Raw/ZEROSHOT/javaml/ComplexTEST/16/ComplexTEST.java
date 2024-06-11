package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c1 = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", c1.toString());
        
        Complex c2 = new Complex();
        assertEquals("0.0 + 0.0i", c2.toString());
    }
    
    @Test
    public void testAbs() {
        Complex c1 = new Complex(3.0, 4.0);
        assertEquals(5.0, c1.abs(), 0.001);
        
        Complex c2 = new Complex();
        assertEquals(0.0, c2.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        c1.plus(c2);
        assertEquals(3.0, c1.re, 0.001);
        assertEquals(5.0, c1.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        c1.minus(c2);
        assertEquals(1.0, c1.re, 0.001);
        assertEquals(1.0, c1.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        c1.times(c2);
        assertEquals(-4.0, c1.re, 0.001);
        assertEquals(7.0, c1.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex c1 = new Complex(2.0, 3.0);
        c1.times(2.0);
        assertEquals(4.0, c1.re, 0.001);
        assertEquals(6.0, c1.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex c1 = new Complex(2.0, 3.0);
        c1.conjugate();
        assertEquals(2.0, c1.re, 0.001);
        assertEquals(-3.0, c1.im, 0.001);
    }
    
    @Test
    public void testStaticPlus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        Complex result = Complex.plus(c1, c2);
        assertEquals(6.0, result.re, 0.001);
        assertEquals(8.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(c1, c2);
        assertEquals(-7.0, result.re, 0.001);
        assertEquals(22.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyDouble() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(c1, 2.0);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }
}