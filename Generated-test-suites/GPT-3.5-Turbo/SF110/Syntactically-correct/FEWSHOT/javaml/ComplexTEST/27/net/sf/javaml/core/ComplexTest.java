package net.sf.javaml.core;

import net.sf.javaml.core.Complex;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexTest {

    private Complex complex;

    @Before
    public void setUp() {
        complex = new Complex();
        complex.re = 1.5;
        complex.im = 2.5;
    }
    
    @Test
    public void testToString() {
        assertEquals("1.5 + 2.5i", complex.toString());
    }

    @Test
    public void testAbs() {
        assertEquals(2.9154759474226504, complex.abs(), 0.00001);
    }

    @Test
    public void testPlus() {
        Complex b = new Complex(2.0, 3.0);
        complex.plus(b);
        assertEquals(3.5, complex.re, 0.00001);
        assertEquals(5.5, complex.im, 0.00001);
    }

    @Test
    public void testMinus() {
        Complex b = new Complex(0.5, 0.5);
        complex.minus(b);
        assertEquals(1.0, complex.re, 0.00001);
        assertEquals(2.0, complex.im, 0.00001);
    }

    @Test
    public void testTimesComplex() {
        Complex b = new Complex(2.0, 3.0);
        complex.times(b);
        assertEquals(-4.75, complex.re, 0.00001);
        assertEquals(9.5, complex.im, 0.00001);
    }

    @Test
    public void testTimesDouble() {
        complex.times(2);
        assertEquals(3.0, complex.re, 0.00001);
        assertEquals(5.0, complex.im, 0.00001);
    }

    @Test
    public void testConjugate() {
        complex.conjugate();
        assertEquals(1.5, complex.re, 0.00001);
        assertEquals(-2.5, complex.im, 0.00001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 1.0);
        Complex b = new Complex(2.0, 3.0);
        Complex result = Complex.plus(a, b);
        assertEquals(3.0, result.re, 0.00001);
        assertEquals(4.0, result.im, 0.00001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(a, 1.5);
        assertEquals(3.0, result.re, 0.00001);
        assertEquals(4.5, result.im, 0.00001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 1.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-1.0, result.re, 0.00001);
        assertEquals(5.0, result.im, 0.00001);
    }
}