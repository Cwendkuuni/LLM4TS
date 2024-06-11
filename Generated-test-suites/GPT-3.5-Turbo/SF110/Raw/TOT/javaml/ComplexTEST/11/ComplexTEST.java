import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ComplexTest {

    private Complex complex1;
    private Complex complex2;

    @Before
    public void setup() {
        complex1 = new Complex(2.0, 3.0);
        complex2 = new Complex(4.0, 5.0);
    }

    @After
    public void tearDown() {
        complex1 = null;
        complex2 = null;
    }

    @Test
    public void testConstructor() {
        Complex complex = new Complex(1.0, 2.0);
        Assert.assertEquals(1.0, complex.re, 0.001);
        Assert.assertEquals(2.0, complex.im, 0.001);
    }

    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        Assert.assertEquals(0.0, complex.re, 0.001);
        Assert.assertEquals(0.0, complex.im, 0.001);
    }

    @Test
    public void testToString() {
        String expected = "2.0 + 3.0i";
        Assert.assertEquals(expected, complex1.toString());
    }

    @Test
    public void testAbs() {
        double expected = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0);
        Assert.assertEquals(expected, complex1.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        complex1.plus(complex2);
        Assert.assertEquals(6.0, complex1.re, 0.001);
        Assert.assertEquals(8.0, complex1.im, 0.001);
    }

    @Test
    public void testMinus() {
        complex1.minus(complex2);
        Assert.assertEquals(-2.0, complex1.re, 0.001);
        Assert.assertEquals(-2.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        complex1.times(complex2);
        Assert.assertEquals(-7.0, complex1.re, 0.001);
        Assert.assertEquals(22.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesDouble() {
        complex1.times(2.0);
        Assert.assertEquals(4.0, complex1.re, 0.001);
        Assert.assertEquals(6.0, complex1.im, 0.001);
    }

    @Test
    public void testConjugate() {
        complex1.conjugate();
        Assert.assertEquals(2.0, complex1.re, 0.001);
        Assert.assertEquals(-3.0, complex1.im, 0.001);
    }

    @Test
    public void testStaticPlus() {
        Complex result = Complex.plus(complex1, complex2);
        Assert.assertEquals(6.0, result.re, 0.001);
        Assert.assertEquals(8.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiplyDouble() {
        Complex result = Complex.multiply(complex1, 2.0);
        Assert.assertEquals(4.0, result.re, 0.001);
        Assert.assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiplyComplex() {
        Complex result = Complex.multiply(complex1, complex2);
        Assert.assertEquals(-7.0, result.re, 0.001);
        Assert.assertEquals(22.0, result.im, 0.001);
    }

    @Test
    public void testStaticI() {
        Assert.assertEquals(0.0, Complex.I.re, 0.001);
        Assert.assertEquals(1.0, Complex.I.im, 0.001);
    }
}