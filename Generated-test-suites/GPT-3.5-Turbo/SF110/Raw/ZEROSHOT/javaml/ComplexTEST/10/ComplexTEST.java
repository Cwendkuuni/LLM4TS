import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.Complex;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        String expected = "3.0 + 4.0i";
        String actual = complex.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        double actual = complex.abs();
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        complex1.plus(complex2);
        double expectedRe = 5.0;
        double expectedIm = 5.0;
        assertEquals(expectedRe, complex1.re, 0.0001);
        assertEquals(expectedIm, complex1.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        complex1.minus(complex2);
        double expectedRe = 1.0;
        double expectedIm = 3.0;
        assertEquals(expectedRe, complex1.re, 0.0001);
        assertEquals(expectedIm, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        complex1.times(complex2);
        double expectedRe = 2.0;
        double expectedIm = 11.0;
        assertEquals(expectedRe, complex1.re, 0.0001);
        assertEquals(expectedIm, complex1.im, 0.0001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.0);
        double expectedRe = 6.0;
        double expectedIm = 8.0;
        assertEquals(expectedRe, complex.re, 0.0001);
        assertEquals(expectedIm, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        double expectedIm = -4.0;
        assertEquals(expectedIm, complex.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        Complex result = Complex.plus(complex1, complex2);
        double expectedRe = 5.0;
        double expectedIm = 5.0;
        assertEquals(expectedRe, result.re, 0.0001);
        assertEquals(expectedIm, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex complex = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex, 2.0);
        double expectedRe = 6.0;
        double expectedIm = 8.0;
        assertEquals(expectedRe, result.re, 0.0001);
        assertEquals(expectedIm, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        Complex result = Complex.multiply(complex1, complex2);
        double expectedRe = 2.0;
        double expectedIm = 11.0;
        assertEquals(expectedRe, result.re, 0.0001);
        assertEquals(expectedIm, result.im, 0.0001);
    }
}