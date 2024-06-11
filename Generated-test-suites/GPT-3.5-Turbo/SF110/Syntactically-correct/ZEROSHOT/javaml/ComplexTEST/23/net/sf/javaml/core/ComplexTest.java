package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c = new Complex(2.0, 3.0);
        String expected = "2.0 + 3.0i";
        String actual = c.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        double expected = 5.0;
        double actual = c.abs();
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.plus(b);
        Complex expected = new Complex(6.0, 8.0);
        Complex actual = a;
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(6.0, 8.0);
        Complex b = new Complex(4.0, 5.0);
        a.minus(b);
        Complex expected = new Complex(2.0, 3.0);
        Complex actual = a;
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.times(b);
        Complex expected = new Complex(-7.0, 22.0);
        Complex actual = a;
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 5.0;
        a.times(alpha);
        Complex expected = new Complex(10.0, 15.0);
        Complex actual = a;
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        c.conjugate();
        Complex expected = new Complex(2.0, -3.0);
        Complex actual = c;
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(6.0, 8.0);
        Complex actual = Complex.plus(a, b);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 5.0;
        Complex expected = new Complex(10.0, 15.0);
        Complex actual = Complex.multiply(a, d);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex expected = new Complex(-7.0, 22.0);
        Complex actual = Complex.multiply(a, b);
        assertEquals(expected.toString(), actual.toString());
    }

}