import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComplexTest {

    private Complex complex1;
    private Complex complex2;

    @Before
    public void setUp() {
        complex1 = new Complex(2.0, 3.0);
        complex2 = new Complex(1.0, -2.0);
    }

    @Test
    public void testConstructorWithParameters() {
        assertEquals(2.0, complex1.re, 0.001);
        assertEquals(3.0, complex1.im, 0.001);
    }

    @Test
    public void testConstructorWithoutParameters() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.001);
        assertEquals(0.0, complex.im, 0.001);
    }

    @Test
    public void testToString() {
        assertEquals("2.0 + 3.0i", complex1.toString());
        assertEquals("1.0 + -2.0i", complex2.toString());
    }

    @Test
    public void testAbs() {
        assertEquals(3.605, complex1.abs(), 0.001);
        assertEquals(2.236, complex2.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        complex1.plus(complex2);
        assertEquals(3.0, complex1.re, 0.001);
        assertEquals(1.0, complex1.im, 0.001);
    }

    @Test
    public void testMinus() {
        complex1.minus(complex2);
        assertEquals(1.0, complex1.re, 0.001);
        assertEquals(5.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        complex1.times(complex2);
        assertEquals(8.0, complex1.re, 0.001);
        assertEquals(-1.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesDouble() {
        complex1.times(2.0);
        assertEquals(4.0, complex1.re, 0.001);
        assertEquals(6.0, complex1.im, 0.001);
    }

    @Test
    public void testConjugate() {
        complex1.conjugate();
        assertEquals(2.0, complex1.re, 0.001);
        assertEquals(-3.0, complex1.im, 0.001);
    }

    @Test
    public void testStaticPlus() {
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(3.0, result.re, 0.001);
        assertEquals(1.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiplyDouble() {
        Complex result = Complex.multiply(complex1, 2.0);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiplyComplex() {
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(8.0, result.re, 0.001);
        assertEquals(-1.0, result.im, 0.001);
    }
}