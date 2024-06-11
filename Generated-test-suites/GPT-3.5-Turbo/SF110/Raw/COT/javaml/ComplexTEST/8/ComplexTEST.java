
import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c = new Complex(1.0, 2.0);
        Assert.assertEquals(1.0, c.re, 0.01);
        Assert.assertEquals(2.0, c.im, 0.01);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        Assert.assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        Assert.assertEquals(5.0, c.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        a.plus(b);
        Assert.assertEquals(4.0, a.re, 0.01);
        Assert.assertEquals(6.0, a.im, 0.01);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(5.0, 6.0);
        Complex b = new Complex(2.0, 3.0);
        a.minus(b);
        Assert.assertEquals(3.0, a.re, 0.01);
        Assert.assertEquals(3.0, a.im, 0.01);
    }

    @Test
    public void testTimesWithComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.times(b);
        Assert.assertEquals(-7.0, a.re, 0.01);
        Assert.assertEquals(22.0, a.im, 0.01);
    }

    @Test
    public void testTimesWithDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 2.0;
        a.times(alpha);
        Assert.assertEquals(4.0, a.re, 0.01);
        Assert.assertEquals(6.0, a.im, 0.01);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, 3.0);
        c.conjugate();
        Assert.assertEquals(2.0, c.re, 0.01);
        Assert.assertEquals(-3.0, c.im, 0.01);
    }

    @Test
    public void testPlusStaticMethod() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.plus(a, b);
        Assert.assertEquals(4.0, result.re, 0.01);
        Assert.assertEquals(6.0, result.im, 0.01);
    }

    @Test
    public void testMultiplyDoubleStaticMethod() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex result = Complex.multiply(a, d);
        Assert.assertEquals(4.0, result.re, 0.01);
        Assert.assertEquals(6.0, result.im, 0.01);
    }

    @Test
    public void testMultiplyComplexStaticMethod() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(a, b);
        Assert.assertEquals(-7.0, result.re, 0.01);
        Assert.assertEquals(22.0, result.im, 0.01);
    }
}
