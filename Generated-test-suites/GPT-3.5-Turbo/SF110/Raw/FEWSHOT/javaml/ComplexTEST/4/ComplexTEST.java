import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0001);
        
        complex = new Complex(0.0, 0.0);
        assertEquals(0.0, complex.abs(), 0.0001);
        
        complex = new Complex(-2.5, 1.5);
        assertEquals(2.8868, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.5, 1.5);
        a.plus(b);
        assertEquals(5.5, a.re, 0.0001);
        assertEquals(5.5, a.im, 0.0001);
        
        a = new Complex(-1.0, 1.0);
        b = new Complex(2.0, -2.0);
        a.plus(b);
        assertEquals(1.0, a.re, 0.0001);
        assertEquals(-1.0, a.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.5, 1.5);
        a.minus(b);
        assertEquals(0.5, a.re, 0.0001);
        assertEquals(2.5, a.im, 0.0001);
        
        a = new Complex(-1.0, 1.0);
        b = new Complex(2.0, -2.0);
        a.minus(b);
        assertEquals(-3.0, a.re, 0.0001);
        assertEquals(3.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimes_Complex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        a.times(b);
        assertEquals(2.0, a.re, 0.0001);
        assertEquals(11.0, a.im, 0.0001);
        
        a = new Complex(-1.0, 1.0);
        b = new Complex(2.0, -2.0);
        a.times(b);
        assertEquals(0.0, a.re, 0.0001);
        assertEquals(-4.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimes_double() {
        Complex a = new Complex(3.0, 4.0);
        a.times(2.5);
        assertEquals(7.5, a.re, 0.0001);
        assertEquals(10.0, a.im, 0.0001);
        
        a = new Complex(-1.0, 1.0);
        a.times(-0.5);
        assertEquals(0.5, a.re, 0.0001);
        assertEquals(-0.5, a.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        assertEquals(3.0, complex.re, 0.0001);
        assertEquals(-4.0, complex.im, 0.0001);
        
        complex = new Complex(-1.0, -1.0);
        complex.conjugate();
        assertEquals(-1.0, complex.re, 0.0001);
        assertEquals(1.0, complex.im, 0.0001);
    }
    
    @Test
    public void testPlus_Static() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.5, 1.5);
        Complex c = Complex.plus(a, b);
        assertEquals(5.5, c.re, 0.0001);
        assertEquals(5.5, c.im, 0.0001);
        
        a = new Complex(-1.0, 1.0);
        b = new Complex(2.0, -2.0);
        c = Complex.plus(a, b);
        assertEquals(1.0, c.re, 0.0001);
        assertEquals(-1.0, c.im, 0.0001);
    }
    
    @Test
    public void testMultiply_Complex_double() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        Complex c = Complex.multiply(a, 2.5);
        assertEquals(7.5, c.re, 0.0001);
        assertEquals(10.0, c.im, 0.0001);
        
        a = new Complex(-1.0, 1.0);
        b = new Complex(2.0, -2.0);
        c = Complex.multiply(a, -0.5);
        assertEquals(0.5, c.re, 0.0001);
        assertEquals(-0.5, c.im, 0.0001);
    }
    
    @Test
    public void testMultiply_Complex_Complex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 1.0);
        Complex c = Complex.multiply(a, b);
        assertEquals(2.0, c.re, 0.0001);
        assertEquals(11.0, c.im, 0.0001);
        
        a = new Complex(-1.0, 1.0);
        b = new Complex(2.0, -2.0);
        c = Complex.multiply(a, b);
        assertEquals(0.0, c.re, 0.0001);
        assertEquals(-4.0, c.im, 0.0001);
    }
}