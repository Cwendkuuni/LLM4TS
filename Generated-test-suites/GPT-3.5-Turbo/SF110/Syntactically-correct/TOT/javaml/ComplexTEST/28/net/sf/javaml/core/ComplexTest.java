import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    private static final double DELTA = 1e-15;
    private Complex c1;
    private Complex c2;
    
    @Before
    public void setUp() {
        c1 = new Complex(2.0, 3.0);
        c2 = new Complex(1.0, 2.0);
    }
    
    @Test
    public void testToString() {
        String expected = "2.0 + 3.0i";
        assertEquals(expected, c1.toString());
    }
    
    @Test
    public void testAbs() {
        double expected = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0);
        assertEquals(expected, c1.abs(), DELTA);
    }
    
    @Test
    public void testPlus() {
        Complex expected = new Complex(3.0, 5.0);
        c1.plus(c2);
        assertEquals(expected.re, c1.re, DELTA);
        assertEquals(expected.im, c1.im, DELTA);
    }
    
    @Test
    public void testMinus() {
        Complex expected = new Complex(1.0, 1.0);
        c1.minus(c2);
        assertEquals(expected.re, c1.re, DELTA);
        assertEquals(expected.im, c1.im, DELTA);
    }
    
    @Test
    public void testTimesComplex() {
        Complex expected = new Complex(-4.0, 7.0);
        c1.times(c2);
        assertEquals(expected.re, c1.re, DELTA);
        assertEquals(expected.im, c1.im, DELTA);
    }
    
    @Test
    public void testTimesDouble() {
        double alpha = 2.0;
        Complex expected = new Complex(4.0, 6.0);
        c1.times(alpha);
        assertEquals(expected.re, c1.re, DELTA);
        assertEquals(expected.im, c1.im, DELTA);
    }
    
    @Test
    public void testConjugate() {
        Complex expected = new Complex(2.0, -3.0);
        c1.conjugate();
        assertEquals(expected.re, c1.re, DELTA);
        assertEquals(expected.im, c1.im, DELTA);
    }
    
    @Test
    public void testPlusStatic() {
        Complex expected = new Complex(3.0, 5.0);
        Complex result = Complex.plus(c1, c2);
        assertEquals(expected.re, result.re, DELTA);
        assertEquals(expected.im, result.im, DELTA);
    }
    
    @Test
    public void testMultiplyDouble() {
        double alpha = 2.0;
        Complex expected = new Complex(4.0, 6.0);
        Complex result = Complex.multiply(c1, alpha);
        assertEquals(expected.re, result.re, DELTA);
        assertEquals(expected.im, result.im, DELTA);
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex expected = new Complex(-4.0, 7.0);
        Complex result = Complex.multiply(c1, c2);
        assertEquals(expected.re, result.re, DELTA);
        assertEquals(expected.im, result.im, DELTA);
    }
}