import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        String expected = "2.0 + 3.0i";
        assertEquals(expected, complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        assertEquals(expected, complex.abs(), 0.0001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.plus(complex2);
        assertEquals(6.0, complex1.re, 0.0001);
        assertEquals(8.0, complex1.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(4.0, 5.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.minus(complex2);
        assertEquals(2.0, complex1.re, 0.0001);
        assertEquals(2.0, complex1.im, 0.0001);
    }

    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.times(complex2);
        assertEquals(-7.0, complex1.re, 0.0001);
        assertEquals(22.0, complex1.im, 0.0001);
    }

    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 2.0;
        complex.times(alpha);
        assertEquals(4.0, complex.re, 0.0001);
        assertEquals(6.0, complex.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.0001);
        assertEquals(-3.0, complex.im, 0.0001);
    }

    @Test
    public void testPlus_Static() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(6.0, result.re, 0.0001);
        assertEquals(8.0, result.im, 0.0001);
    }

    @Test
    public void testMultiply_Double() {
        Complex complex = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex result = Complex.multiply(complex, d);
        assertEquals(4.0, result.re, 0.0001);
        assertEquals(6.0, result.im, 0.0001);
    }

    @Test
    public void testMultiply_Complex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-7.0, result.re, 0.0001);
        assertEquals(22.0, result.im, 0.0001);
    }
}