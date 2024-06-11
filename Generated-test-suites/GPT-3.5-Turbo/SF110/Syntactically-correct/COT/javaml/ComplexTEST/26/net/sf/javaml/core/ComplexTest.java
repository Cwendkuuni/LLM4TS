import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.plus(b);
        assertEquals(6.0, a.re, 0.0001);
        assertEquals(8.0, a.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(7.0, 8.0);
        Complex b = new Complex(2.0, 3.0);
        a.minus(b);
        assertEquals(5.0, a.re, 0.0001);
        assertEquals(5.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.times(b);
        assertEquals(-7.0, a.re, 0.0001);
        assertEquals(22.0, a.im, 0.0001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(2.5);
        assertEquals(5.0, complex.re, 0.0001);
        assertEquals(7.5, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.0001);
        assertEquals(-3.0, complex.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex result = Complex.plus(a, b);
        assertEquals(6.0, result.re, 0.0001);
        assertEquals(8.0, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(a, 2.5);
        assertEquals(5.0, result.re, 0.0001);
        assertEquals(7.5, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-7.0, result.re, 0.0001);
        assertEquals(22.0, result.im, 0.0001);
    }
}