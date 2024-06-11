import org.junit.Test;

public class ComplexTest {
    private Complex complex1 = new Complex(3.0, 4.0);
    private Complex complex2 = new Complex(1.0, 2.0);

    @Test
    public void testToString() {
        String expected = "3.0 + 4.0i";
        assertEquals(expected, complex1.toString());
    }

    @Test
    public void testAbs() {
        double expected = 5.0;
        assertEquals(expected, complex1.abs(), 0.0001);
    }

    @Test
    public void testPlus() {
        complex1.plus(complex2);
        assertEquals(4.0, complex1.re, 0.0001);
        assertEquals(6.0, complex1.im, 0.0001);
    }

    @Test
    public void testMinus() {
        complex1.minus(complex2);
        assertEquals(2.0, complex1.re, 0.0001);
        assertEquals(2.0, complex1.im, 0.0001);
    }

    @Test
    public void testTimesComplex() {
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-5.0, result.re, 0.0001);
        assertEquals(10.0, result.im, 0.0001);
    }

    @Test
    public void testTimesDouble() {
        complex1.times(2.0);
        assertEquals(6.0, complex1.re, 0.0001);
        assertEquals(8.0, complex1.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        complex1.conjugate();
        assertEquals(3.0, complex1.re, 0.0001);
        assertEquals(-4.0, complex1.im, 0.0001);
    }

    @Test
    public void testStaticPlus() {
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(4.0, result.re, 0.0001);
        assertEquals(6.0, result.im, 0.0001);
    }

    @Test
    public void testStaticMultiplyComplex() {
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-5.0, result.re, 0.0001);
        assertEquals(10.0, result.im, 0.0001);
    }

    @Test
    public void testStaticMultiplyDouble() {
        Complex result = Complex.multiply(complex1, 2.0);
        assertEquals(6.0, result.re, 0.0001);
        assertEquals(8.0, result.im, 0.0001);
    }
}