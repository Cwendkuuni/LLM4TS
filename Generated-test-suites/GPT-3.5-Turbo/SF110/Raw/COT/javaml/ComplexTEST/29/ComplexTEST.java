import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {

    // Basic test for toString() method
    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", complex.toString());
    }

    // Basic test for abs() method
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0001);
    }

    // Basic test for plus() method
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.plus(complex2);
        assertEquals(5.0, complex1.re, 0.0001);
        assertEquals(7.0, complex1.im, 0.0001);
    }

    // Basic test for minus() method
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.minus(complex2);
        assertEquals(1.0, complex1.re, 0.0001);
        assertEquals(1.0, complex1.im, 0.0001);
    }

    // Basic test for times() method
    @Test
    public void testTimes() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.times(complex2);
        assertEquals(-6.0, complex1.re, 0.0001);
        assertEquals(17.0, complex1.im, 0.0001);
    }

    // Basic test for times(double alpha) method
    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.5);
        assertEquals(7.5, complex.re, 0.0001);
        assertEquals(10.0, complex.im, 0.0001);
    }

    // Basic test for conjugate() method
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        assertEquals(3.0, complex.re, 0.0001);
        assertEquals(-4.0, complex.im, 0.0001);
    }

    // Basic test for Complex.plus(Complex a, Complex b) method
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(5.0, result.re, 0.0001);
        assertEquals(7.0, result.im, 0.0001);
    }

    // Basic test for Complex.multiply(Complex a, double d) method
    @Test
    public void testStaticMultiplyWithDouble() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex1, 2.5);
        assertEquals(7.5, result.re, 0.0001);
        assertEquals(10.0, result.im, 0.0001);
    }

    // Basic test for Complex.multiply(Complex a, Complex b) method
    @Test
    public void testStaticMultiplyWithComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-6.0, result.re, 0.0001);
        assertEquals(17.0, result.im, 0.0001);
    }

    // Test for edge case of I constant
    @Test
    public void testIConstant() {
        assertEquals(0.0, Complex.I.re, 0.0001);
        assertEquals(1.0, Complex.I.im, 0.0001);
    }

    // Test for default constructor
    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.0001);
        assertEquals(0.0, complex.im, 0.0001);
    }

    // Add more test cases for potential edge cases and exception handling scenarios if needed

    // 

    // 
}