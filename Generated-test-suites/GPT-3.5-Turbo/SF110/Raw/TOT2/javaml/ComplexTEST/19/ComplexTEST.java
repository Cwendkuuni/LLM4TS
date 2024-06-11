import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComplexTest {

    private Complex complex1;
    private Complex complex2;

    @Test
    public void testToString() {
        complex1 = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", complex1.toString());
    }

    @Test
    public void testAbs() {
        complex1 = new Complex(3.0, 4.0);
        assertEquals(5.0, complex1.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        complex1 = new Complex(1.0, 2.0);
        complex2 = new Complex(3.0, 4.0);
        complex1.plus(complex2);
        assertEquals(4.0, complex1.re, 0.001);
        assertEquals(6.0, complex1.im, 0.001);
    }

    @Test
    public void testMinus() {
        complex1 = new Complex(3.0, 4.0);
        complex2 = new Complex(1.0, 2.0);
        complex1.minus(complex2);
        assertEquals(2.0, complex1.re, 0.001);
        assertEquals(2.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        complex1 = new Complex(1.0, 2.0);
        complex2 = new Complex(3.0, 4.0);
        complex1.times(complex2);
        assertEquals(-5.0, complex1.re, 0.001);
        assertEquals(10.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesDouble() {
        complex1 = new Complex(1.0, 2.0);
        complex1.times(2.0);
        assertEquals(2.0, complex1.re, 0.001);
        assertEquals(4.0, complex1.im, 0.001);
    }

    @Test
    public void testConjugate() {
        complex1 = new Complex(1.0, -2.0);
        complex1.conjugate();
        assertEquals(1.0, complex1.re, 0.001);
        assertEquals(2.0, complex1.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        complex1 = new Complex(1.0, 2.0);
        complex2 = new Complex(3.0, 4.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyComplex() {
        complex1 = new Complex(1.0, 2.0);
        complex2 = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-5.0, result.re, 0.001);
        assertEquals(10.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyDouble() {
        complex1 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(complex1, 2.0);
        assertEquals(2.0, result.re, 0.001);
        assertEquals(4.0, result.im, 0.001);
    }
}