package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex complex = new Complex(2.0, 3.0);
        
        assertEquals(2.0, complex.re, 0.0);
        assertEquals(3.0, complex.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        
        assertEquals("2.0 + 3.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        
        assertEquals(5.0, complex.abs(), 0.0);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 5.0);
        
        complex1.plus(complex2);
        
        assertEquals(3.0, complex1.re, 0.0);
        assertEquals(8.0, complex1.im, 0.0);
    }

    // Additional test cases for other methods

}