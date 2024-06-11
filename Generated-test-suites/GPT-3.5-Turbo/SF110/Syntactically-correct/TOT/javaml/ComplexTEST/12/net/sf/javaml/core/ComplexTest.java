import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testConstructor() {
        Complex complex = new Complex(5.0, 10.0);
        
        assertEquals(5.0, complex.re, 0.001);
        assertEquals(10.0, complex.im, 0.001);
    }
    
    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, -3.5);
        
        assertEquals("2.5 + -3.5i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        
        assertEquals(5.0, complex.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        complex1.plus(complex2);
        
        assertEquals(3.0, complex1.re, 0.001);
        assertEquals(5.0, complex1.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        complex1.minus(complex2);
        
        assertEquals(1.0, complex1.re, 0.001);
        assertEquals(1.0, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        complex1.times(complex2);
        
        assertEquals(-4.0, complex1.re, 0.001);
        assertEquals(7.0, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesNumber() {
        Complex complex = new Complex(2.0, 3.0);
        
        complex.times(2.0);
        
        assertEquals(4.0, complex.re, 0.001);
        assertEquals(6.0, complex.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        
        complex.conjugate();
        
        assertEquals(2.0, complex.re, 0.001);
        assertEquals(-3.0, complex.im, 0.001);
    }
    
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        Complex result = Complex.plus(complex1, complex2);
        
        assertEquals(3.0, result.re, 0.001);
        assertEquals(5.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        Complex result = Complex.multiply(complex1, complex2);
        
        assertEquals(-4.0, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyNumber() {
        Complex complex = new Complex(2.0, 3.0);
        
        Complex result = Complex.multiply(complex, 2.0);
        
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }
    
    @Test
    public void testConstants() {
        Complex complex = Complex.I;
        
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(1.0, complex.im, 0.001);
    }
}