package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(3.0, complex.re, 0.001);
        assertEquals(4.0, complex.im, 0.001);
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex complex = new Complex(3.0, 4.0);
        Complex toAdd = new Complex(2.0, 3.0);
        complex.plus(toAdd);
        assertEquals(5.0, complex.re, 0.001);
        assertEquals(7.0, complex.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex complex = new Complex(3.0, 4.0);
        Complex toSubtract = new Complex(2.0, 3.0);
        complex.minus(toSubtract);
        assertEquals(1.0, complex.re, 0.001);
        assertEquals(1.0, complex.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        Complex complex = new Complex(3.0, 4.0);
        Complex toMultiply = new Complex(1.0, 2.0);
        complex.times(toMultiply);
        assertEquals(-5.0, complex.re, 0.001);
        assertEquals(10.0, complex.im, 0.001);
    }

    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(3.0, 4.0);
        double alpha = 2.0;
        complex.times(alpha);
        assertEquals(6.0, complex.re, 0.001);
        assertEquals(8.0, complex.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        assertEquals(3.0, complex.re, 0.001);
        assertEquals(-4.0, complex.im, 0.001);
    }

    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(5.0, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiply() {
        Complex complex1 = new Complex(3.0, 4.0);
        double d = 2.0;
        Complex result = Complex.multiply(complex1, d);
        assertEquals(6.0, result.re, 0.001);
        assertEquals(8.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiplyComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-5.0, result.re, 0.001);
        assertEquals(10.0, result.im, 0.001);
    }
}