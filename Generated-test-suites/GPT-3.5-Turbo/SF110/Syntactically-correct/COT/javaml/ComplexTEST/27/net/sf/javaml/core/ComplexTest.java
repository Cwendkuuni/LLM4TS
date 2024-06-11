import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(3.5, 2.0);
        assertEquals("3.5 + 2.0i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.5, 2.5);
        complex1.plus(complex2);
        assertEquals(4.5, complex1.re, 0.0001);
        assertEquals(6.5, complex1.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.5, 2.5);
        complex1.minus(complex2);
        assertEquals(1.5, complex1.re, 0.0001);
        assertEquals(1.5, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimesWithComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.5, 2.5);
        complex1.times(complex2);
        assertEquals(-4, complex1.re, 0.0001);
        assertEquals(13.0, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimesWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.5);
        assertEquals(7.5, complex.re, 0.0001);
        assertEquals(10.0, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        assertEquals(3.0, complex.re, 0.0001);
        assertEquals(-4.0, complex.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.5, 2.5);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(4.5, result.re, 0.0001);
        assertEquals(6.5, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex, 2.5);
        assertEquals(7.5, result.re, 0.0001);
        assertEquals(10.0, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyWithComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.5, 2.5);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-4, result.re, 0.0001);
        assertEquals(13.0, result.im, 0.0001);
    }
    
    @Test
    public void testIConstant() {
        assertEquals(0.0, Complex.I.re, 0.0001);
        assertEquals(1.0, Complex.I.im, 0.0001);
    }

    // Additional test cases for edge cases and exception handling should be added here

}

