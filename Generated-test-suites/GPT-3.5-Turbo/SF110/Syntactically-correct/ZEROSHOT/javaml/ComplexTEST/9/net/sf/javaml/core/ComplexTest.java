package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex complex = new Complex(1.0, 2.0);
        Assert.assertEquals(1.0, complex.re, 0.01);
        Assert.assertEquals(2.0, complex.im, 0.01);
    }

    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        Assert.assertEquals(0.0, complex.re, 0.01);
        Assert.assertEquals(0.0, complex.im, 0.01);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(1.0, 2.0);
        String expected = "1.0 + 2.0i";
        Assert.assertEquals(expected, complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        Assert.assertEquals(expected, complex.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.plus(complex2);
        Assert.assertEquals(4.0, complex1.re, 0.01);
        Assert.assertEquals(6.0, complex1.im, 0.01);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.minus(complex2);
        Assert.assertEquals(2.0, complex1.re, 0.01);
        Assert.assertEquals(2.0, complex1.im, 0.01);
    }

    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.times(complex2);
        Assert.assertEquals(-5.0, complex1.re, 0.01);
        Assert.assertEquals(10.0, complex1.im, 0.01);
    }

    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(1.0, 2.0);
        complex.times(2.0);
        Assert.assertEquals(2.0, complex.re, 0.01);
        Assert.assertEquals(4.0, complex.im, 0.01);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(1.0, 2.0);
        complex.conjugate();
        Assert.assertEquals(1.0, complex.re, 0.01);
        Assert.assertEquals(-2.0, complex.im, 0.01);
    }

    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex result = Complex.plus(complex1, complex2);
        Assert.assertEquals(4.0, result.re, 0.01);
        Assert.assertEquals(6.0, result.im, 0.01);
    }

    @Test
    public void testStaticMultiplyDouble() {
        Complex complex = new Complex(1.0, 2.0);
        double alpha = 2.0;
        Complex result = Complex.multiply(complex, alpha);
        Assert.assertEquals(2.0, result.re, 0.01);
        Assert.assertEquals(4.0, result.im, 0.01);
    }

    @Test
    public void testStaticMultiplyComplex() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex1, complex2);
        Assert.assertEquals(-5.0, result.re, 0.01);
        Assert.assertEquals(10.0, result.im, 0.01);
    }
}