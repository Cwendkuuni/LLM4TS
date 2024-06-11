import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.5);
        String result = complex.toString();
        assertEquals("2.5 + 3.5i", result);
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(2.5, 3.5);
        double result = complex.abs();
        assertEquals(4.301162633521313, result, 0.0001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.5, 3.5);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.plus(complex2);
        assertEquals(3.5, complex1.re, 0.0001);
        assertEquals(5.5, complex1.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.5, 3.5);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.minus(complex2);
        assertEquals(1.5, complex1.re, 0.0001);
        assertEquals(1.5, complex1.im, 0.0001);
    }

    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.5, 3.5);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.times(complex2);
        assertEquals(-4.0, complex1.re, 0.0001);
        assertEquals(10.5, complex1.im, 0.0001);
    }

    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(2.5, 3.5);
        complex.times(2.0);
        assertEquals(5.0, complex.re, 0.0001);
        assertEquals(7.0, complex.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.5, 3.5);
        complex.conjugate();
        assertEquals(2.5, complex.re, 0.0001);
        assertEquals(-3.5, complex.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(2.5, 3.5);
        Complex complex2 = new Complex(1.0, 2.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(3.5, result.re, 0.0001);
        assertEquals(5.5, result.im, 0.0001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex complex = new Complex(2.5, 3.5);
        Complex result = Complex.multiply(complex, 2.0);
        assertEquals(5.0, result.re, 0.0001);
        assertEquals(7.0, result.im, 0.0001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex complex1 = new Complex(2.5, 3.5);
        Complex complex2 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-4.0, result.re, 0.0001);
        assertEquals(10.5, result.im, 0.0001);
    }

}