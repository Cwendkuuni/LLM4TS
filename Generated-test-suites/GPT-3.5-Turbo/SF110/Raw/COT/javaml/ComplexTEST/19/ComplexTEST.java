import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex c = new Complex(2.0, 3.0);
        String expected = "2.0 + 3.0i";
        String actual = c.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        double expected = 5.0;
        double actual = c.abs();
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.plus(b);
        double expectedReal = 6.0;
        double expectedImag = 8.0;
        assertEquals(expectedReal, a.re, 0.0001);
        assertEquals(expectedImag, a.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(6.0, 8.0);
        Complex b = new Complex(4.0, 5.0);
        a.minus(b);
        double expectedReal = 2.0;
        double expectedImag = 3.0;
        assertEquals(expectedReal, a.re, 0.0001);
        assertEquals(expectedImag, a.im, 0.0001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.times(b);
        double expectedReal = -7.0;
        double expectedImag = 22.0;
        assertEquals(expectedReal, a.re, 0.0001);
        assertEquals(expectedImag, a.im, 0.0001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 2.0;
        a.times(alpha);
        double expectedReal = 4.0;
        double expectedImag = 6.0;
        assertEquals(expectedReal, a.re, 0.0001);
        assertEquals(expectedImag, a.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex a = new Complex(2.0, 3.0);
        a.conjugate();
        double expectedReal = 2.0;
        double expectedImag = -3.0;
        assertEquals(expectedReal, a.re, 0.0001);
        assertEquals(expectedImag, a.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex result = Complex.plus(a, b);
        double expectedReal = 6.0;
        double expectedImag = 8.0;
        assertEquals(expectedReal, result.re, 0.0001);
        assertEquals(expectedImag, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex result = Complex.multiply(a, d);
        double expectedReal = 4.0;
        double expectedImag = 6.0;
        assertEquals(expectedReal, result.re, 0.0001);
        assertEquals(expectedImag, result.im, 0.0001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(a, b);
        double expectedReal = -7.0;
        double expectedImag = 22.0;
        assertEquals(expectedReal, result.re, 0.0001);
        assertEquals(expectedImag, result.im, 0.0001);
    }
    
    @Test
    public void testStaticI() {
        Complex expected = new Complex(0.0, 1.0);
        Complex actual = Complex.I;
        assertEquals(expected.re, actual.re, 0.0001);
        assertEquals(expected.im, actual.im, 0.0001);
    }
}