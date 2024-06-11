import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    
    private Complex complex1;
    private Complex complex2;
    private Complex complex3;
    
    @Before
    public void setUp() {
        complex1 = new Complex(2.0, 3.0);
        complex2 = new Complex(4.0, -2.0);
        complex3 = new Complex();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(2.0, complex1.re, 0.0);
        assertEquals(3.0, complex1.im, 0.0);
        assertEquals(4.0, complex2.re, 0.0);
        assertEquals(-2.0, complex2.im, 0.0);
        assertEquals(0.0, complex3.re, 0.0);
        assertEquals(0.0, complex3.im, 0.0);
    }
    
    @Test
    public void testToString() {
        String expected1 = "2.0 + 3.0i";
        String expected2 = "4.0 + -2.0i";
        String expected3 = "0.0 + 0.0i";
        
        assertEquals(expected1, complex1.toString());
        assertEquals(expected2, complex2.toString());
        assertEquals(expected3, complex3.toString());
    }
    
    @Test
    public void testAbs() {
        double expected1 = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0);
        double expected2 = Math.sqrt(4.0 * 4.0 + (-2.0) * (-2.0));
        double expected3 = Math.sqrt(0.0 * 0.0 + 0.0 * 0.0);
        
        assertEquals(expected1, complex1.abs(), 0.0);
        assertEquals(expected2, complex2.abs(), 0.0);
        assertEquals(expected3, complex3.abs(), 0.0);
    }
    
    @Test
    public void testPlus() {
        Complex expected1 = new Complex(6.0, 1.0);
        Complex expected2 = new Complex(6.0, 1.0);
        Complex expected3 = new Complex(2.0, 3.0);
        
        complex1.plus(complex2);
        assertEquals(expected1.re, complex1.re, 0.0);
        assertEquals(expected1.im, complex1.im, 0.0);
        
        complex2.plus(complex3);
        assertEquals(expected2.re, complex2.re, 0.0);
        assertEquals(expected2.im, complex2.im, 0.0);
        
        complex3.plus(complex1);
        assertEquals(expected3.re, complex3.re, 0.0);
        assertEquals(expected3.im, complex3.im, 0.0);
    }
    
    @Test
    public void testMinus() {
        Complex expected1 = new Complex(-2.0, 5.0);
        Complex expected2 = new Complex(-4.0, 2.0);
        Complex expected3 = new Complex(-2.0, -3.0);
        
        complex1.minus(complex2);
        assertEquals(expected1.re, complex1.re, 0.0);
        assertEquals(expected1.im, complex1.im, 0.0);
        
        complex2.minus(complex3);
        assertEquals(expected2.re, complex2.re, 0.0);
        assertEquals(expected2.im, complex2.im, 0.0);
        
        complex3.minus(complex1);
        assertEquals(expected3.re, complex3.re, 0.0);
        assertEquals(expected3.im, complex3.im, 0.0);
    }
    
    @Test
    public void testTimesComplex() {
        Complex expected1 = new Complex(14.0, 8.0);
        Complex expected2 = new Complex(0.0, 0.0);
        Complex expected3 = new Complex(0.0, 0.0);
        
        complex1.times(complex2);
        assertEquals(expected1.re, complex1.re, 0.0);
        assertEquals(expected1.im, complex1.im, 0.0);
        
        complex2.times(complex3);
        assertEquals(expected2.re, complex2.re, 0.0);
        assertEquals(expected2.im, complex2.im, 0.0);
        
        complex3.times(complex1);
        assertEquals(expected3.re, complex3.re, 0.0);
        assertEquals(expected3.im, complex3.im, 0.0);
    }
    
    @Test
    public void testTimesDouble() {
        Complex expected1 = new Complex(4.0, 6.0);
        Complex expected2 = new Complex(0.0, 0.0);
        Complex expected3 = new Complex(0.0, 0.0);
        
        complex1.times(2.0);
        assertEquals(expected1.re, complex1.re, 0.0);
        assertEquals(expected1.im, complex1.im, 0.0);
        
        complex2.times(0.0);
        assertEquals(expected2.re, complex2.re, 0.0);
        assertEquals(expected2.im, complex2.im, 0.0);
        
        complex3.times(10.0);
        assertEquals(expected3.re, complex3.re, 0.0);
        assertEquals(expected3.im, complex3.im, 0.0);
    }
    
    @Test
    public void testConjugate() {
        Complex expected1 = new Complex(2.0, -3.0);
        Complex expected2 = new Complex(4.0, 2.0);
        Complex expected3 = new Complex(0.0, 0.0);
        
        complex1.conjugate();
        assertEquals(expected1.re, complex1.re, 0.0);
        assertEquals(expected1.im, complex1.im, 0.0);
        
        complex2.conjugate();
        assertEquals(expected2.re, complex2.re, 0.0);
        assertEquals(expected2.im, complex2.im, 0.0);
        
        complex3.conjugate();
        assertEquals(expected3.re, complex3.re, 0.0);
        assertEquals(expected3.im, complex3.im, 0.0);
    }
    
    @Test
    public void testPlusStatic() {
        Complex expected = new Complex(6.0, 1.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(expected.re, result.re, 0.0);
        assertEquals(expected.im, result.im, 0.0);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex expected = new Complex(10.0, 15.0);
        Complex result = Complex.multiply(complex1, 5.0);
        assertEquals(expected.re, result.re, 0.0);
        assertEquals(expected.im, result.im, 0.0);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex expected = new Complex(-2.0, 17.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(expected.re, result.re, 0.0);
        assertEquals(expected.im, result.im, 0.0);
    }
}