import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        // Typical Use Case
        Complex complex = new Complex(2.5, -1.5);
        
        assertEquals(2.5, complex.re, 0.0001);
        assertEquals(-1.5, complex.im, 0.0001);
    }
    
    @Test
    public void testDefaultComplexConstructor() {
        // Typical Use Case
        Complex complex = new Complex();
        
        assertEquals(0.0, complex.re, 0.0001);
        assertEquals(0.0, complex.im, 0.0001);
    }
    
    @Test
    public void testToString() {
        // Typical Use Case
        Complex complex = new Complex(2.5, -1.5);
        
        assertEquals("2.5 + -1.5i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        // Typical Use Case
        Complex complex = new Complex(3.0, 4.0);
        
        assertEquals(5.0, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        // Typical Use Case
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(-1.0, 2.0);
        complex1.plus(complex2);
        
        assertEquals(1.0, complex1.re, 0.0001);
        assertEquals(5.0, complex1.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        // Typical Use Case
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(-1.0, 2.0);
        complex1.minus(complex2);
        
        assertEquals(3.0, complex1.re, 0.0001);
        assertEquals(1.0, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimesComplex() {
        // Typical Use Case
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(-1.0, 2.0);
        complex1.times(complex2);
        
        assertEquals(-8.0, complex1.re, 0.0001);
        assertEquals(1.0, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimesAlpha() {
        // Typical Use Case
        Complex complex = new Complex(2.0, 3.0);
        complex.times(2.0);
        
        assertEquals(4.0, complex.re, 0.0001);
        assertEquals(6.0, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        // Typical Use Case
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        
        assertEquals(2.0, complex.re, 0.0001);
        assertEquals(-3.0, complex.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        // Typical Use Case
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(-1.0, 2.0);
        Complex result = Complex.plus(complex1, complex2);
        
        assertEquals(1.0, result.re, 0.0001);
        assertEquals(5.0, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyStaticDouble() {
        // Typical Use Case
        Complex complex = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex, 2.0);
        
        assertEquals(4.0, result.re, 0.0001);
        assertEquals(6.0, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyStaticComplex() {
        // Typical Use Case
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(-1.0, 2.0);
        Complex result = Complex.multiply(complex1, complex2);
        
        assertEquals(-8.0, result.re, 0.0001);
        assertEquals(1.0, result.im, 0.0001);
    }
}