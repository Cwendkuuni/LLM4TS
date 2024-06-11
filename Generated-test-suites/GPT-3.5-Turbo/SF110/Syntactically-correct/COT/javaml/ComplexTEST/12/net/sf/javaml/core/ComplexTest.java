import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ComplexTest {
    
    private Complex complex;
    
    @Before
    public void setUp() {
        complex = new Complex(2.0, 3.0);
    }
    
    @Test
    public void testToString() {
        assertEquals("2.0 + 3.0i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        assertEquals(3.605551275463989, complex.abs(), 0.00001);
    }
    
    @Test
    public void testPlus() {
        Complex b = new Complex(1.0, 2.0);
        complex.plus(b);
        assertEquals(3.0, complex.re, 0.00001);
        assertEquals(5.0, complex.im, 0.00001);
    }
    
    @Test
    public void testMinus() {
        Complex b = new Complex(1.0, 2.0);
        complex.minus(b);
        assertEquals(1.0, complex.re, 0.00001);
        assertEquals(1.0, complex.im, 0.00001);
    }
    
    @Test
    public void testTimes() {
        Complex b = new Complex(1.0, 2.0);
        complex.times(b);
        assertEquals(-4.0, complex.re, 0.00001);
        assertEquals(7.0, complex.im, 0.00001);
    }
    
    @Test
    public void testTimesAlpha() {
        complex.times(2.0);
        assertEquals(4.0, complex.re, 0.00001);
        assertEquals(6.0, complex.im, 0.00001);
    }
    
    @Test
    public void testConjugate() {
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.00001);
        assertEquals(-3.0, complex.im, 0.00001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.plus(a, b);
        assertEquals(4.0, result.re, 0.00001);
        assertEquals(6.0, result.im, 0.00001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(a, 2.0);
        assertEquals(2.0, result.re, 0.00001);
        assertEquals(4.0, result.im, 0.00001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-5.0, result.re, 0.00001);
        assertEquals(10.0, result.im, 0.00001);
    }
    
}
