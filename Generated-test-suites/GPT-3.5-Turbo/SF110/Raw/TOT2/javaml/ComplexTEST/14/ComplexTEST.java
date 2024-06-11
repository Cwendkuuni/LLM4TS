import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComplexTest {
    
    @Test
    public void testConstructorWithParams() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(3.0, complex.re, 0);
        assertEquals(4.0, complex.im, 0);
    }
    
    @Test
    public void testConstructorWithoutParams() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0);
        assertEquals(0.0, complex.im, 0);
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0);
    }
    
    @Test
    public void testPlus() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(5.0, 2.0);
        complexA.plus(complexB);
        assertEquals(8.0, complexA.re, 0);
        assertEquals(6.0, complexA.im, 0);
    }
    
    @Test
    public void testMinus() {
        Complex complexA = new Complex(5.0, 6.0);
        Complex complexB = new Complex(2.0, 3.0);
        complexA.minus(complexB);
        assertEquals(3.0, complexA.re, 0);
        assertEquals(3.0, complexA.im, 0);
    }
    
    @Test
    public void testTimesWithComplex() {
        Complex complexA = new Complex(3.0, 2.0);
        Complex complexB = new Complex(1.0, 1.0);
        complexA.times(complexB);
        assertEquals(1.0, complexA.re, 0);
        assertEquals(5.0, complexA.im, 0);
    }
    
    @Test
    public void testTimesWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(2.0);
        assertEquals(4.0, complex.re, 0);
        assertEquals(6.0, complex.im, 0);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0);
        assertEquals(-3.0, complex.im, 0);
    }
    
    @Test
    public void testStaticPlus() {
        Complex complexA = new Complex(2.0, 3.0);
        Complex complexB = new Complex(4.0, 2.0);
        Complex result = Complex.plus(complexA, complexB);
        assertEquals(6.0, result.re, 0);
        assertEquals(5.0, result.im, 0);
    }
    
    @Test
    public void testStaticMultiplyWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex, 2.0);
        assertEquals(6.0, result.re, 0);
        assertEquals(8.0, result.im, 0);
    }
    
    @Test
    public void testStaticMultiplyWithComplex() {
        Complex complexA = new Complex(3.0, 2.0);
        Complex complexB = new Complex(1.0, 1.0);
        Complex result = Complex.multiply(complexA, complexB);
        assertEquals(1.0, result.re, 0);
        assertEquals(5.0, result.im, 0);
    }
}