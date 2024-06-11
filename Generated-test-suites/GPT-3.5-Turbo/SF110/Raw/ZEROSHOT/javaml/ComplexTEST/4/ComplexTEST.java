package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testComplex() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(3.0, complex.re, 0.0);
        assertEquals(4.0, complex.im, 0.0);
    }

    @Test
    public void testComplexDefault() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.0);
        assertEquals(0.0, complex.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0);
    }

    @Test
    public void testPlus() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        complexA.plus(complexB);
        assertEquals(4.0, complexA.re, 0.0);
        assertEquals(6.0, complexA.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        complexA.minus(complexB);
        assertEquals(2.0, complexA.re, 0.0);
        assertEquals(2.0, complexA.im, 0.0);
    }

    @Test
    public void testTimesComplex() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        complexA.times(complexB);
        assertEquals(-5.0, complexA.re, 0.0);
        assertEquals(10.0, complexA.im, 0.0);
    }

    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.0);
        assertEquals(6.0, complex.re, 0.0);
        assertEquals(8.0, complex.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        assertEquals(3.0, complex.re, 0.0);
        assertEquals(-4.0, complex.im, 0.0);
    }

    @Test
    public void testPlusStatic() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        Complex result = Complex.plus(complexA, complexB);
        assertEquals(4.0, result.re, 0.0);
        assertEquals(6.0, result.im, 0.0);
    }

    @Test
    public void testMultiplyDouble() {
        Complex complex = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex, 2.0);
        assertEquals(6.0, result.re, 0.0);
        assertEquals(8.0, result.im, 0.0);
    }

    @Test
    public void testMultiplyComplex() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(complexA, complexB);
        assertEquals(-5.0, result.re, 0.0);
        assertEquals(10.0, result.im, 0.0);
    }

}