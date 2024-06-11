package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testToString() {
        Complex c = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", c.toString());
        
        c = new Complex(-2.0, 3.0);
        assertEquals("-2.0 + 3.0i", c.toString());
        
        c = new Complex(2.0, -3.0);
        assertEquals("2.0 + -3.0i", c.toString());
        
        c = new Complex(-2.0, -3.0);
        assertEquals("-2.0 + -3.0i", c.toString());
    }
    
    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.0001);
        
        c = new Complex(-3.0, -4.0);
        assertEquals(5.0, c.abs(), 0.0001);
        
        c = new Complex(0.0, 0.0);
        assertEquals(0.0, c.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(6.0, 8.0);
        a.plus(b);
        assertEquals(expected, a);
        
        a = new Complex(-2.0, -3.0);
        b = new Complex(4.0, 5.0);
        expected = new Complex(2.0, 2.0);
        a.plus(b);
        assertEquals(expected, a);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(-2.0, -2.0);
        a.minus(b);
        assertEquals(expected, a);
        
        a = new Complex(-2.0, -3.0);
        b = new Complex(4.0, 5.0);
        expected = new Complex(-6.0, -8.0);
        a.minus(b);
        assertEquals(expected, a);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(-7.0, 22.0);
        a.times(b);
        assertEquals(expected, a);
        
        a = new Complex(-2.0, -3.0);
        b = new Complex(4.0, 5.0);
        expected = new Complex(-11.0, -22.0);
        a.times(b);
        assertEquals(expected, a);
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 2.0;
        Complex expected = new Complex(4.0, 6.0);
        a.times(alpha);
        assertEquals(expected, a);
        
        a = new Complex(-2.0, -3.0);
        alpha = -2.0;
        expected = new Complex(4.0, 6.0);
        a.times(alpha);
        assertEquals(expected, a);
    }
    
    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        Complex expected = new Complex(2.0, -3.0);
        c.conjugate();
        assertEquals(expected, c);
        
        c = new Complex(-2.0, -3.0);
        expected = new Complex(-2.0, 3.0);
        c.conjugate();
        assertEquals(expected, c);
    }
    
    @Test
    public void testPlusComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(6.0, 8.0);
        Complex result = Complex.plus(a, b);
        assertEquals(expected, result);
        
        a = new Complex(-2.0, -3.0);
        b = new Complex(4.0, 5.0);
        expected = new Complex(2.0, 2.0);
        result = Complex.plus(a, b);
        assertEquals(expected, result);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex expected = new Complex(4.0, 6.0);
        Complex result = Complex.multiply(a, d);
        assertEquals(expected, result);
        
        a = new Complex(-2.0, -3.0);
        d = -2.0;
        expected = new Complex(4.0, 6.0);
        result = Complex.multiply(a, d);
        assertEquals(expected, result);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(-7.0, 22.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(expected, result);
        
        a = new Complex(-2.0, -3.0);
        b = new Complex(4.0, 5.0);
        expected = new Complex(-11.0, -22.0);
        result = Complex.multiply(a, b);
        assertEquals(expected, result);
    }
}