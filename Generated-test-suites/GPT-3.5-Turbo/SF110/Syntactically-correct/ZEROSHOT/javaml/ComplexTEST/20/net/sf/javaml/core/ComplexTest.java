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
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        assertEquals(expected, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.plus(complex2);
        Complex expected = new Complex(6.0, 8.0);
        assertEquals(expected.toString(), complex1.toString());
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(4.0, 5.0);
        Complex complex2 = new Complex(2.0, 1.0);
        complex1.minus(complex2);
        Complex expected = new Complex(2.0, 4.0);
        assertEquals(expected.toString(), complex1.toString());
    }

    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.times(complex2);
        Complex expected = new Complex(-7.0, 22.0);
        assertEquals(expected.toString(), complex1.toString());
    }

    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(2.0);
        Complex expected = new Complex(4.0, 6.0);
        assertEquals(expected.toString(), complex.toString());
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        Complex expected = new Complex(2.0, -3.0);
        assertEquals(expected.toString(), complex.toString());
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex sum = Complex.plus(complex1, complex2);
        Complex expected = new Complex(6.0, 8.0);
        assertEquals(expected.toString(), sum.toString());
    }

    @Test
    public void testMultiplyDouble() {
        Complex complex = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex result = Complex.multiply(complex, d);
        Complex expected = new Complex(4.0, 6.0);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testMultiplyComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(complex1, complex2);
        Complex expected = new Complex(-7.0, 22.0);
        assertEquals(expected.toString(), result.toString());
    }

}