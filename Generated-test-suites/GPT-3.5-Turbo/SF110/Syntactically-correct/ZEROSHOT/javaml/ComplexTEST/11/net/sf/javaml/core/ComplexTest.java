import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex complex = new Complex();
        String expected = "0.0 + 0.0i";
        Assert.assertEquals(expected, complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        Assert.assertEquals(expected, complex.abs(), 0.0001);
    }

    @Test
    public void testPlus() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);

        complex1.plus(complex2);

        Assert.assertEquals(expected.re, complex1.re, 0.0001);
        Assert.assertEquals(expected.im, complex1.im, 0.0001);
    }

    @Test
    public void testMinus() {
        Complex complex1 = new Complex(5.0, 6.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex expected = new Complex(2.0, 2.0);

        complex1.minus(complex2);

        Assert.assertEquals(expected.re, complex1.re, 0.0001);
        Assert.assertEquals(expected.im, complex1.im, 0.0001);
    }

    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex expected = new Complex(-7.0, 22.0);

        complex1.times(complex2);

        Assert.assertEquals(expected.re, complex1.re, 0.0001);
        Assert.assertEquals(expected.im, complex1.im, 0.0001);
    }

    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 1.5;
        Complex expected = new Complex(3.0, 4.5);

        complex.times(alpha);

        Assert.assertEquals(expected.re, complex.re, 0.0001);
        Assert.assertEquals(expected.im, complex.im, 0.0001);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        Complex expected = new Complex(2.0, -3.0);

        complex.conjugate();

        Assert.assertEquals(expected.re, complex.re, 0.0001);
        Assert.assertEquals(expected.im, complex.im, 0.0001);
    }

    @Test
    public void testPlusStatic() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);

        Complex result = Complex.plus(complex1, complex2);

        Assert.assertEquals(expected.re, result.re, 0.0001);
        Assert.assertEquals(expected.im, result.im, 0.0001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex complex = new Complex(2.0, 3.0);
        double d = 1.5;
        Complex expected = new Complex(3.0, 4.5);

        Complex result = Complex.multiply(complex, d);

        Assert.assertEquals(expected.re, result.re, 0.0001);
        Assert.assertEquals(expected.im, result.im, 0.0001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex expected = new Complex(-7.0, 22.0);

        Complex result = Complex.multiply(complex1, complex2);

        Assert.assertEquals(expected.re, result.re, 0.0001);
        Assert.assertEquals(expected.im, result.im, 0.0001);
    }
}