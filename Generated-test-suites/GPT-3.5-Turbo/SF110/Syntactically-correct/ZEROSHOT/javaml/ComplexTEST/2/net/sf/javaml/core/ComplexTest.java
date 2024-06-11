package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(1.0, c.re, 0.001);
        assertEquals(2.0, c.im, 0.001);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        
        a.plus(b);
        
        assertEquals(4.0, a.re, 0.001);
        assertEquals(6.0, a.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(5.0, 6.0);
        Complex b = new Complex(3.0, 2.0);
        
        a.minus(b);
        
        assertEquals(2.0, a.re, 0.001);
        assertEquals(4.0, a.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        
        a.times(b);
        
        assertEquals(-5.0, a.re, 0.001);
        assertEquals(10.0, a.im, 0.001);
    }

    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        
        a.times(2.0);
        
        assertEquals(4.0, a.re, 0.001);
        assertEquals(6.0, a.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex a = new Complex(1.0, 2.0);
        
        a.conjugate();
        
        assertEquals(1.0, a.re, 0.001);
        assertEquals(-2.0, a.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        
        Complex c = Complex.plus(a, b);
        
        assertEquals(4.0, c.re, 0.001);
        assertEquals(6.0, c.im, 0.001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        
        Complex c = Complex.multiply(a, 2.0);
        
        assertEquals(4.0, c.re, 0.001);
        assertEquals(6.0, c.im, 0.001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        
        Complex c = Complex.multiply(a, b);
        
        assertEquals(-5.0, c.re, 0.001);
        assertEquals(10.0, c.im, 0.001);
    }
}