import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex complex = new Complex(2.5, 3.5);
        Assert.assertEquals(2.5, complex.re, 0.0);
        Assert.assertEquals(3.5, complex.im, 0.0);
    }

    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        Assert.assertEquals(0.0, complex.re, 0.0);
        Assert.assertEquals(0.0, complex.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.5);
        String expected = "2.5 + 3.5i";
        Assert.assertEquals(expected, complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        Assert.assertEquals(expected, complex.abs(), 0.0);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.plus(complex2);
        Assert.assertEquals(6.0, complex1.re, 0.0);
        Assert.assertEquals(8.0, complex1.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.minus(complex2);
        Assert.assertEquals(-2.0, complex1.re, 0.0);
        Assert.assertEquals(-2.0, complex1.im, 0.0);
    }

    @Test
    public void testTimesWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.times(complex2);
        Assert.assertEquals(-7.0, complex1.re, 0.0);
        Assert.assertEquals(22.0, complex1.im, 0.0);
    }

    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 2.0;
        complex.times(alpha);
        Assert.assertEquals(4.0, complex.re, 0.0);
        Assert.assertEquals(6.0, complex.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        Assert.assertEquals(2.0, complex.re, 0.0);
        Assert.assertEquals(-3.0, complex.im, 0.0);
    }

    @Test
    public void testPlusWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.plus(complex1, complex2);
        Assert.assertEquals(6.0, result.re, 0.0);
        Assert.assertEquals(8.0, result.im, 0.0);
    }

    @Test
    public void testMultiplyWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex result = Complex.multiply(complex, d);
        Assert.assertEquals(4.0, result.re, 0.0);
        Assert.assertEquals(6.0, result.im, 0.0);
    }

    @Test
    public void testMultiplyWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(complex1, complex2);
        Assert.assertEquals(-7.0, result.re, 0.0);
        Assert.assertEquals(22.0, result.im, 0.0);
    }

    // Add additional test cases for edge cases and exception handling

}

