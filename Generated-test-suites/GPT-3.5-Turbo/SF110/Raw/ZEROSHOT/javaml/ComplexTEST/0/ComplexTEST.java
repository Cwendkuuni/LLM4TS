package net.sf.javaml.core;

import org.junit.Test;
import org.junit.Assert;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        String expected = "2.0 + 3.0i";
        String actual = complex.toString();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(2.0, 3.0);
        double expected = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0);
        double actual = complex.abs();
        Assert.assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.plus(complex2);
        
        double expectedReal = 2.0 + 4.0;
        double expectedImag = 3.0 + 5.0;
        
        Assert.assertEquals(expectedReal, complex1.re, 0.001);
        Assert.assertEquals(expectedImag, complex1.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.minus(complex2);
        
        double expectedReal = 2.0 - 4.0;
        double expectedImag = 3.0 - 5.0;
        
        Assert.assertEquals(expectedReal, complex1.re, 0.001);
        Assert.assertEquals(expectedImag, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.times(complex2);
        
        double expectedReal = 2.0 * 4.0 - 3.0 * 5.0;
        double expectedImag = 2.0 * 5.0 + 3.0 * 4.0;
        
        Assert.assertEquals(expectedReal, complex1.re, 0.001);
        Assert.assertEquals(expectedImag, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 2.5;
        complex.times(alpha);
        
        double expectedReal = 2.0 * alpha;
        double expectedImag = 3.0 * alpha;
        
        Assert.assertEquals(expectedReal, complex.re, 0.001);
        Assert.assertEquals(expectedImag, complex.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        
        double expectedImag = -3.0;
        
        Assert.assertEquals(expectedImag, complex.im, 0.001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.plus(complex1, complex2);
        
        double expectedReal = 2.0 + 4.0;
        double expectedImag = 3.0 + 5.0;
        
        Assert.assertEquals(expectedReal, result.re, 0.001);
        Assert.assertEquals(expectedImag, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyDoubleStatic() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 2.5;
        Complex result = Complex.multiply(complex, alpha);
        
        double expectedReal = 2.0 * alpha;
        double expectedImag = 3.0 * alpha;
        
        Assert.assertEquals(expectedReal, result.re, 0.001);
        Assert.assertEquals(expectedImag, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplexStatic() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(complex1, complex2);
        
        double expectedReal = 2.0 * 4.0 - 3.0 * 5.0;
        double expectedImag = 2.0 * 5.0 + 3.0 * 4.0;
        
        Assert.assertEquals(expectedReal, result.re, 0.001);
        Assert.assertEquals(expectedImag, result.im, 0.001);
    }
}