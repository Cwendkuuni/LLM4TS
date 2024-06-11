
package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex c = new Complex(3.5, 2.0);
        assertEquals("3.5 + 2.0i", c.toString());
    }
    
    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.001);
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
        Complex c1 = new Complex(3.0, 5.0);
        Complex c2 = new Complex(2.0, 3.0);
        c1.minus(c2);
        assertEquals(1.0, c1.re, 0.001);
        assertEquals(2.0, c1.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        c1.times(c2);
        assertEquals(-7.0, c1.re, 0.001);
        assertEquals(22.0, c1.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex c = new Complex(2.0, 3.0);
        c.times(2.5);
        assertEquals(5.0, c.re, 0.001);
        assertEquals(7.5, c.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        c.conjugate();
        assertEquals(2.0, c.re, 0.001);
        assertEquals(-3.0, c.im, 0.001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(1.0, 2.0);
        Complex result = Complex.plus(c1, c2);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(5.0, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex c = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(c, 2.5);
        assertEquals(5.0, result.re, 0.001);
        assertEquals(7.5, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(c1, c2);
        assertEquals(-7.0, result.re, 0.001);
        assertEquals(22.0, result.im, 0.001);
    }
}
