package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex complex = new Complex();
        Assert.assertEquals(0.0, complex.re, 0.001);
        Assert.assertEquals(0.0, complex.im, 0.001);
        
        complex = new Complex(2.5, 3.0);
        Assert.assertEquals(2.5, complex.re, 0.001);
        Assert.assertEquals(3.0, complex.im, 0.001);
    }
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.0);
        Assert.assertEquals("2.5 + 3.0i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        Assert.assertEquals(5.0, complex.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.5, 3.0);
        Complex b = new Complex(1.0, 2.0);
        
        a.plus(b);
        
        Assert.assertEquals(3.5, a.re, 0.001);
        Assert.assertEquals(5.0, a.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(2.5, 3.0);
        Complex b = new Complex(1.0, 2.0);
        
        a.minus(b);
        
        Assert.assertEquals(1.5, a.re, 0.001);
        Assert.assertEquals(1.0, a.im, 0.001);
    }
    
    @Test
    public void testTimes() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        
        a.times(b);
        
        Assert.assertEquals(-4.0, a.re, 0.001);
        Assert.assertEquals(7.0, a.im, 0.001);
    }
    
    @Test
    public void testTimesWithAlpha() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 2.0;
        
        a.times(alpha);
        
        Assert.assertEquals(4.0, a.re, 0.001);
        Assert.assertEquals(6.0, a.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        
        Assert.assertEquals(2.0, complex.re, 0.001);
        Assert.assertEquals(-3.0, complex.im, 0.001);
    }
    
    @Test
    public void testStaticPlus() {
        Complex a = new Complex(2.5, 3.0);
        Complex b = new Complex(1.0, 2.0);
        
        Complex result = Complex.plus(a, b);
        
        Assert.assertEquals(3.5, result.re, 0.001);
        Assert.assertEquals(5.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyWithDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.0;
        
        Complex result = Complex.multiply(a, d);
        
        Assert.assertEquals(4.0, result.re, 0.001);
        Assert.assertEquals(6.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyWithComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        
        Complex result = Complex.multiply(a, b);
        
        Assert.assertEquals(-4.0, result.re, 0.001);
        Assert.assertEquals(7.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticI() {
        Assert.assertEquals(0.0, Complex.I.re, 0.001);
        Assert.assertEquals(1.0, Complex.I.im, 0.001);
    }
}