import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ComplexTest {
    private Complex complex1;
    private Complex complex2;
    
    @Before
    public void setUp() {
        complex1 = new Complex(3.5, 2.5);
        complex2 = new Complex(1.5, 4.5);
    }
    
    @Test
    public void testConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
        
        complex = new Complex(2.0, -3.0);
        assertEquals(2.0, complex.re, 0.001);
        assertEquals(-3.0, complex.im, 0.001);
    }
    
    @Test
    public void testToString() {
        String expected = "3.5 + 2.5i";
        assertEquals(expected, complex1.toString());
        
        expected = "1.5 + 4.5i";
        assertEquals(expected, complex2.toString());
    }
    
    @Test
    public void testAbs() {
        double expected = Math.sqrt(3.5 * 3.5 + 2.5 * 2.5);
        assertEquals(expected, complex1.abs(), 0.001);
        
        expected = Math.sqrt(1.5 * 1.5 + 4.5 * 4.5);
        assertEquals(expected, complex2.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        complex1.plus(complex2);
        assertEquals(5.0, complex1.re, 0.001);
        assertEquals(7.0, complex1.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        complex1.minus(complex2);
        assertEquals(2.0, complex1.re, 0.001);
        assertEquals(-2.0, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        complex1.times(complex2);
        assertEquals(-5.25, complex1.re, 0.001);
        assertEquals(21.75, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        double alpha = 2.0;
        complex1.times(alpha);
        assertEquals(7.0, complex1.re, 0.001);
        assertEquals(5.0, complex1.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        complex1.conjugate();
        assertEquals(3.5, complex1.re, 0.001);
        assertEquals(-2.5, complex1.im, 0.001);
        
        complex2.conjugate();
        assertEquals(1.5, complex2.re, 0.001);
        assertEquals(-4.5, complex2.im, 0.001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(5.0, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyDoubleStatic() {
        double d = 2.0;
        Complex result = Complex.multiply(complex1, d);
        assertEquals(7.0, result.re, 0.001);
        assertEquals(5.0, result.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplexStatic() {
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-5.25, result.re, 0.001);
        assertEquals(21.75, result.im, 0.001);
    }
}