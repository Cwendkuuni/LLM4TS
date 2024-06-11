import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ComplexTest {

    private Complex complex1;
    private Complex complex2;

    @Before
    public void setUp() {
        complex1 = new Complex(2.0, 3.0);
        complex2 = new Complex(4.0, 5.6);
    }

    @Test
    public void testConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.01);
        assertEquals(0.0, complex.im, 0.01);

        complex = new Complex(2.5, -1.5);
        assertEquals(2.5, complex.re, 0.01);
        assertEquals(-1.5, complex.im, 0.01);
    }

    @Test
    public void testToString() {
        assertEquals("2.0 + 3.0i", complex1.toString());
        assertEquals("4.0 + 5.6i", complex2.toString());
    }

    @Test
    public void testAbs() {
        assertEquals(3.605, complex1.abs(), 0.01);
        assertEquals(6.548, complex2.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        complex1.plus(complex2);
        assertEquals(6.0, complex1.re, 0.01);
        assertEquals(8.6, complex1.im, 0.01);

        compareComplexNumbers(complex1, Complex.plus(new Complex(2.0, 3.0), new Complex(4.0, 5.6)));
    }

    @Test
    public void testMinus() {
        complex1.minus(complex2);
        assertEquals(-2.0, complex1.re, 0.01);
        assertEquals(-2.6, complex1.im, 0.01);
    }

    @Test
    public void testTimes() {
        complex1.times(complex2);
        assertEquals(-1.4, complex1.re, 0.01);
        assertEquals(23.2, complex1.im, 0.01);

        compareComplexNumbers(complex1, Complex.multiply(new Complex(2.0, 3.0), new Complex(4.0, 5.6)));
    }

    @Test
    public void testTimesWithAlpha() {
        complex1.times(2.0);
        assertEquals(4.0, complex1.re, 0.01);
        assertEquals(6.0, complex1.im, 0.01);

        compareComplexNumbers(complex1, Complex.multiply(new Complex(2.0, 3.0), 2.0));
    }

    @Test
    public void testConjugate() {
        complex1.conjugate();
        assertEquals(2.0, complex1.re, 0.01);
        assertEquals(-3.0, complex1.im, 0.01);
    }

    @Test
    public void testPlusStatic() {
        Complex result = Complex.plus(complex1, complex2);
        compareComplexNumbers(result, new Complex(6.0, 8.6));
    }

    @Test
    public void testMultiplyWithAlphaStatic() {
        Complex result = Complex.multiply(complex1, 2.5);
        compareComplexNumbers(result, new Complex(5.0, 7.5));
    }

    @Test
    public void testMultiplyWithComplexStatic() {
        Complex result = Complex.multiply(complex1, complex2);
        compareComplexNumbers(result, new Complex(-1.4, 23.2));
    }

    private void compareComplexNumbers(Complex complex1, Complex complex2) {
        assertEquals(complex1.re, complex2.re, 0.01);
        assertEquals(complex1.im, complex2.im, 0.01);
    }

}