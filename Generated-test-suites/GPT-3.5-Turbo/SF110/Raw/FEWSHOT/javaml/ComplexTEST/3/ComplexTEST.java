package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testConstructors() {
        Complex c1 = new Complex(2.0, 3.0);
        assertEquals(2.0, c1.re, 0.00001);
        assertEquals(3.0, c1.im, 0.00001);
        
        Complex c2 = new Complex();
        assertEquals(0.0, c2.re, 0.00001);
        assertEquals(0.0, c2.im, 0.00001);
    }
    
    @Test
    public void testToString() {
        Complex c = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", c.toString());
    }
    
    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.00001);
    }
    
    @Test
    public void testPlusComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        c1.plus(c2);
        assertEquals(6.0, c1.re, 0.00001);
        assertEquals(8.0, c1.im, 0.00001);
    }
    
    @Test
    public void testMinusComplex() {
        Complex c1 = new Complex(4.0, 5.0);
        Complex c2 = new Complex(2.0, 3.0);
        c1.minus(c2);
        assertEquals(2.0, c1.re, 0.00001);
        assertEquals(2.0, c1.im, 0.00001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        c1.times(c2);
        assertEquals(-7.0, c1.re, 0.00001);
        assertEquals(22.0, c1.im, 0.00001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex c = new Complex(2.0, 3.0);
        c.times(3.0);
        assertEquals(6.0, c.re, 0.00001);
        assertEquals(9.0, c.im, 0.00001);
    }
    
    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        c.conjugate();
        assertEquals(2.0, c.re, 0.00001);
        assertEquals(-3.0, c.im, 0.00001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        Complex c3 = Complex.plus(c1, c2);
        assertEquals(6.0, c3.re, 0.00001);
        assertEquals(8.0, c3.im, 0.00001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = Complex.multiply(c1, 2.0);
        assertEquals(4.0, c2.re, 0.00001);
        assertEquals(6.0, c2.im, 0.00001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        Complex c3 = Complex.multiply(c1, c2);
        assertEquals(-7.0, c3.re, 0.00001);
        assertEquals(22.0, c3.im, 0.00001);
    }
}