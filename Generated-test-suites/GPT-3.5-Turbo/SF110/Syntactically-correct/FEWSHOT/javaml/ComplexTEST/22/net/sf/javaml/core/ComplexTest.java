
package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex complex = new Complex(1.0, 2.0);
        assertNotNull(complex);
        assertEquals(1.0, complex.re, 0.001);
        assertEquals(2.0, complex.im, 0.001);
    }

    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        assertNotNull(complex);
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }

    @Test
    public void testPlusInstance() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.plus(complex2);
        assertEquals(4.0, complex1.re, 0.001);
        assertEquals(6.0, complex1.im, 0.001);
    }

    @Test
    public void testMinusInstance() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.minus(complex2);
        assertEquals(-2.0, complex1.re, 0.001);
        assertEquals(-2.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesInstance() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.times(complex2);
        assertEquals(-5.0, complex1.re, 0.001);
        assertEquals(10.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesInstanceWithAlpha() {
        Complex complex = new Complex(1.0, 2.0);
        complex.times(2.0);
        assertEquals(2.0, complex.re, 0.001);
        assertEquals(4.0, complex.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(1.0, 2.0);
        complex.conjugate();
        assertEquals(1.0, complex.re, 0.001);
        assertEquals(-2.0, complex.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(complex, 2.0);
        assertEquals(2.0, result.re, 0.001);
        assertEquals(4.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-5.0, result.re, 0.001);
        assertEquals(10.0, result.im, 0.001);
    }
}
