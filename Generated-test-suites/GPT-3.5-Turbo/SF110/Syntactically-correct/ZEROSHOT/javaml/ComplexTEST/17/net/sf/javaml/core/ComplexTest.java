package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {
    
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
        Assert.assertEquals(expected, complex.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        a.plus(b);
        Complex expected = new Complex(4.0, 6.0);
        Assert.assertEquals(expected.re, a.re, 0.001);
        Assert.assertEquals(expected.im, a.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        a.minus(b);
        Complex expected = new Complex(-2.0, -2.0);
        Assert.assertEquals(expected.re, a.re, 0.001);
        Assert.assertEquals(expected.im, a.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        a.times(b);
        Complex expected = new Complex(-5.0, 10.0);
        Assert.assertEquals(expected.re, a.re, 0.001);
        Assert.assertEquals(expected.im, a.im, 0.001);
    }
    
    @Test
    public void testTimesAlpha() {
        Complex complex = new Complex(1.0, 2.0);
        double alpha = 3.0;
        complex.times(alpha);
        Complex expected = new Complex(3.0, 6.0);
        Assert.assertEquals(expected.re, complex.re, 0.001);
        Assert.assertEquals(expected.im, complex.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(1.0, 2.0);
        complex.conjugate();
        Complex expected = new Complex(1.0, -2.0);
        Assert.assertEquals(expected.re, complex.re, 0.001);
        Assert.assertEquals(expected.im, complex.im, 0.001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.plus(a, b);
        Complex expected = new Complex(4.0, 6.0);
        Assert.assertEquals(expected.re, result.re, 0.001);
        Assert.assertEquals(expected.im, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyAlphaStatic() {
        Complex a = new Complex(1.0, 2.0);
        double alpha = 3.0;
        Complex result = Complex.multiply(a, alpha);
        Complex expected = new Complex(3.0, 6.0);
        Assert.assertEquals(expected.re, result.re, 0.001);
        Assert.assertEquals(expected.im, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplexStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(a, b);
        Complex expected = new Complex(-5.0, 10.0);
        Assert.assertEquals(expected.re, result.re, 0.001);
        Assert.assertEquals(expected.im, result.im, 0.001);
    }
}