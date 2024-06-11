import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    private Complex complex;

    @Before
    public void setUp() {
        complex = new Complex(3.0, 4.0);
    }

    @Test
    public void testToString() {
        assertEquals("3.0 + 4.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        assertEquals(5.0, complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex b = new Complex(2.0, 2.0);
        complex.plus(b);
        assertEquals(5.0, complex.re, 0.001);
        assertEquals(6.0, complex.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex b = new Complex(2.0, 2.0);
        complex.minus(b);
        assertEquals(1.0, complex.re, 0.001);
        assertEquals(2.0, complex.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        Complex b = new Complex(2.0, 2.0);
        complex.times(b);
        assertEquals(-8.0, complex.re, 0.001);
        assertEquals(14.0, complex.im, 0.001);
    }

    @Test
    public void testTimesDouble() {
        double alpha = 2.0;
        complex.times(alpha);
        assertEquals(6.0, complex.re, 0.001);
        assertEquals(8.0, complex.im, 0.001);
    }

    @Test
    public void testConjugate() {
        complex.conjugate();
        assertEquals(3.0, complex.re, 0.001);
        assertEquals(-4.0, complex.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 1.0);
        Complex b = new Complex(2.0, 2.0);
        Complex result = Complex.plus(a, b);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(3.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 2.0);
        double d = 3.0;
        Complex result = Complex.multiply(a, d);
        assertEquals(6.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 2.0);
        Complex b = new Complex(3.0, 3.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(0.0, result.re, 0.001);
        assertEquals(12.0, result.im, 0.001);
    }
}