package net.sf.javaml.core;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(3.0, complex.re, 0.001);
        assertEquals(4.0, complex.im, 0.001);
    }

    @Test
    public void testComplexDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.plus(b);
        assertEquals(6.0, a.re, 0.001);
        assertEquals(8.0, a.im, 0.001);
    }

    // Add test cases for each method in the Complex class

}