import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        String expectedResult = "3.0 + 4.0i";
        String actualResult = complex.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expectedResult = 5.0;
        double actualResult = complex.abs();
        assertEquals(expectedResult, actualResult, 0.001);
    }

    @Test
    public void testPlus() {
        Complex complex = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 2.0);
        Complex expectedResult = new Complex(4.0, 6.0);
        complex.plus(b);
        assertEquals(expectedResult.re, complex.re, 0.001);
        assertEquals(expectedResult.im, complex.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex complex = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 2.0);
        Complex expectedResult = new Complex(2.0, 2.0);
        complex.minus(b);
        assertEquals(expectedResult.re, complex.re, 0.001);
        assertEquals(expectedResult.im, complex.im, 0.001);
    }

    @Test
    public void testTimesWithComplex() {
        Complex complex = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 2.0);
        Complex expectedResult = new Complex(-5.0, 10.0);
        complex.times(b);
        assertEquals(expectedResult.re, complex.re, 0.001);
        assertEquals(expectedResult.im, complex.im, 0.001);
    }

    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(3.0, 4.0);
        double alpha = 2.0;
        Complex expectedResult = new Complex(6.0, 8.0);
        complex.times(alpha);
        assertEquals(expectedResult.re, complex.re, 0.001);
        assertEquals(expectedResult.im, complex.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        Complex expectedResult = new Complex(3.0, -4.0);
        complex.conjugate();
        assertEquals(expectedResult.re, complex.re, 0.001);
        assertEquals(expectedResult.im, complex.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 2.0);
        Complex expectedResult = new Complex(4.0, 6.0);
        Complex actualResult = Complex.plus(a, b);
        assertEquals(expectedResult.re, actualResult.re, 0.001);
        assertEquals(expectedResult.im, actualResult.im, 0.001);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        double d = 2.0;
        Complex expectedResult = new Complex(6.0, 8.0);
        Complex actualResult = Complex.multiply(complex, d);
        assertEquals(expectedResult.re, actualResult.re, 0.001);
        assertEquals(expectedResult.im, actualResult.im, 0.001);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 2.0);
        Complex expectedResult = new Complex(-5.0, 10.0);
        Complex actualResult = Complex.multiply(a, b);
        assertEquals(expectedResult.re, actualResult.re, 0.001);
        assertEquals(expectedResult.im, actualResult.im, 0.001);
    }
}