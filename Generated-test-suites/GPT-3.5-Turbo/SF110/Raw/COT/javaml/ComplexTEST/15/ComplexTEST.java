import org.junit.Test;

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
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.plus(b);
        Complex expected = new Complex(3.0, 5.0);
        assertEquals(expected.re, a.re, 0.00001);
        assertEquals(expected.im, a.im, 0.00001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.minus(b);
        Complex expected = new Complex(1.0, 1.0);
        assertEquals(expected.re, a.re, 0.00001);
        assertEquals(expected.im, a.im, 0.00001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.times(b);
        Complex expected = new Complex(-4.0, 7.0);
        assertEquals(expected.re, a.re, 0.00001);
        assertEquals(expected.im, a.im, 0.00001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 2.0;
        a.times(alpha);
        Complex expected = new Complex(4.0, 6.0);
        assertEquals(expected.re, a.re, 0.00001);
        assertEquals(expected.im, a.im, 0.00001);
    }
    
    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        c.conjugate();
        Complex expected = new Complex(2.0, -3.0);
        assertEquals(expected.re, c.re, 0.00001);
        assertEquals(expected.im, c.im, 0.00001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.plus(a, b);
        Complex expected = new Complex(3.0, 5.0);
        assertEquals(expected.re, result.re, 0.00001);
        assertEquals(expected.im, result.im, 0.00001);
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex result = Complex.multiply(a, d);
        Complex expected = new Complex(4.0, 6.0);
        assertEquals(expected.re, result.re, 0.00001);
        assertEquals(expected.im, result.im, 0.00001);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(a, b);
        Complex expected = new Complex(-4.0, 7.0);
        assertEquals(expected.re, result.re, 0.00001);
        assertEquals(expected.im, result.im, 0.00001);
    }
}