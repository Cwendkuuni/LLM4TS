package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(2.0, 3.0);
        double expected = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0);
        assertEquals(expected, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.plus(complex2);
        assertEquals(6.0, complex1.re, 0.0001);
        assertEquals(8.0, complex1.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.minus(complex2);
        assertEquals(-2.0, complex1.re, 0.0001);
        assertEquals(-2.0, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimes() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.times(complex2);
        assertEquals(-7.0, complex1.re, 0.0001);
        assertEquals(22.0, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 1.5;
        complex.times(alpha);
        assertEquals(3.0, complex.re, 0.0001);
        assertEquals(4.5, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.0001);
        assertEquals(-3.0, complex.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex complex = Complex.plus(complex1, complex2);
        assertEquals(6.0, complex.re, 0.0001);
        assertEquals(8.0, complex.im, 0.0001);
    }
    
    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        double d = 1.5;
        Complex result = Complex.multiply(complex, d);
        assertEquals(3.0, result.re, 0.0001);
        assertEquals(4.5, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-7.0, result.re, 0.0001);
        assertEquals(22.0, result.im, 0.0001);
    }
    
    @Test
    public void testStaticInitialization() {
        Complex complex = Complex.I;
        assertEquals(0.0, complex.re, 0.0001);
        assertEquals(1.0, complex.im, 0.0001);
    }
    
}