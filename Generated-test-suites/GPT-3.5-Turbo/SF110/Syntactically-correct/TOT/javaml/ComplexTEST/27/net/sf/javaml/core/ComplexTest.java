import static org.junit.Assert.*;
import org.junit.Test;

public class ComplexTest {
    
    // Test case for the constructor with parameters
    @Test
    public void testConstructorWithParameters() {
        Complex complex = new Complex(2.5, 1.5);
        assertEquals(2.5, complex.re, 0.001);
        assertEquals(1.5, complex.im, 0.001);
    }
    
    // Test case for the constructor without parameters
    @Test
    public void testConstructorWithoutParameters() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }
    
    // Test case for the toString() method
    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 1.5);
        String expected = "2.5 + 1.5i";
        assertEquals(expected, complex.toString());
    }
    
    // Test case for the abs() method - typical use case
    @Test
    public void testAbsTypical() {
        Complex complex = new Complex(-3.0, 4.0);
        double expected = 5.0;
        assertEquals(expected, complex.abs(), 0.001);
    }
    
    // Test case for the abs() method - edge case
    @Test
    public void testAbsEdge() {
        Complex complex = new Complex(0.0, 0.0);
        double expected = 0.0;
        assertEquals(expected, complex.abs(), 0.001);
    }
    
    // Test case for the plus() method
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -1.0);
        complex1.plus(complex2);
        assertEquals(3.0, complex1.re, 0.001);
        assertEquals(2.0, complex1.im, 0.001);
    }
    
    // Test case for the minus() method
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -1.0);
        complex1.minus(complex2);
        assertEquals(1.0, complex1.re, 0.001);
        assertEquals(4.0, complex1.im, 0.001);
    }
    
    // Test case for the times() method - typical use case
    @Test
    public void testTimesTypical() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -1.0);
        complex1.times(complex2);
        assertEquals(5.0, complex1.re, 0.001);
        assertEquals(1.0, complex1.im, 0.001);
    }
    
    // Test case for the times() method - edge case
    @Test
    public void testTimesEdge() {
        Complex complex1 = new Complex(0.0, 0.0);
        Complex complex2 = new Complex(1.0, -1.0);
        complex1.times(complex2);
        assertEquals(0.0, complex1.re, 0.001);
        assertEquals(0.0, complex1.im, 0.001);
    }
    
    // Test case for the times() method with alpha parameter
    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(2.5);
        assertEquals(5.0, complex.re, 0.001);
        assertEquals(7.5, complex.im, 0.001);
    }
    
    // Test case for the conjugate() method
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.001);
        assertEquals(-3.0, complex.im, 0.001);
    }
    
    // Test case for the plus() method (static)
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -1.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(2.0, result.im, 0.001);
    }
    
    // Test case for the multiply() method with double parameter
    @Test
    public void testStaticMultiplyWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex, 1.5);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(4.5, result.im, 0.001);
    }
    
    // Test case for the multiply() method with Complex parameter - typical use case
    @Test
    public void testStaticMultiplyWithComplexTypical() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -1.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(5.0, result.re, 0.001);
        assertEquals(1.0, result.im, 0.001);
    }
    
    // Test case for the multiply() method with Complex parameter - edge case
    @Test
    public void testStaticMultiplyWithComplexEdge() {
        Complex complex1 = new Complex(0.0, 0.0);
        Complex complex2 = new Complex(1.0, -1.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(0.0, result.re, 0.001);
        assertEquals(0.0, result.im, 0.001);
    }
    
    // Test case for the I constant
    @Test
    public void testConstantI() {
        Complex complex = Complex.I;
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(1.0, complex.im, 0.001);
    }
}