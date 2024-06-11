package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 2.0);
        String expected = "3.0 + 2.0i";
        String actual = complex.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        double actual = complex.abs();
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        a.plus(b);
        double expectedRe = 5.0;
        double expectedIm = 5.0;
        assertEquals(expectedRe, a.re, 0.001);
        assertEquals(expectedIm, a.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        a.minus(b);
        double expectedRe = 1.0;
        double expectedIm = 3.0;
        assertEquals(expectedRe, a.re, 0.001);
        assertEquals(expectedIm, a.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        a.times(b);
        double expectedRe = 2.0;
        double expectedIm = 11.0;
        assertEquals(expectedRe, a.re, 0.001);
        assertEquals(expectedIm, a.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(3.0, 4.0);
        double alpha = 2.0;
        a.times(alpha);
        double expectedRe = 6.0;
        double expectedIm = 8.0;
        assertEquals(expectedRe, a.re, 0.001);
        assertEquals(expectedIm, a.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        double expectedRe = 3.0;
        double expectedIm = -4.0;
        assertEquals(expectedRe, complex.re, 0.001);
        assertEquals(expectedIm, complex.im, 0.001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        Complex result = Complex.plus(a, b);
        double expectedRe = 5.0;
        double expectedIm = 5.0;
        assertEquals(expectedRe, result.re, 0.001);
        assertEquals(expectedIm, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(3.0, 4.0);
        double d = 2.0;
        Complex result = Complex.multiply(a, d);
        double expectedRe = 6.0;
        double expectedIm = 8.0;
        assertEquals(expectedRe, result.re, 0.001);
        assertEquals(expectedIm, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        Complex result = Complex.multiply(a, b);
        double expectedRe = 2.0;
        double expectedIm = 11.0;
        assertEquals(expectedRe, result.re, 0.001);
        assertEquals(expectedIm, result.im, 0.001);
    }
    
}