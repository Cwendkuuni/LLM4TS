import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {
    
    @Test
    public void testComplexConstructor() {
        Complex complex = new Complex(2.5, 3.7);
        Assert.assertEquals(2.5, complex.re, 0.001);
        Assert.assertEquals(3.7, complex.im, 0.001);
    }
    
    @Test
    public void testDefaultComplexConstructor() {
        Complex complex = new Complex();
        Assert.assertEquals(0.0, complex.re, 0.001);
        Assert.assertEquals(0.0, complex.im, 0.001);
    }
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, -3.7);
        Assert.assertEquals("2.5 + -3.7i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        Assert.assertEquals(5.0, complex.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.5, 3.0);
        Complex complex2 = new Complex(1.5, -2.5);
        complex1.plus(complex2);
        Assert.assertEquals(4.0, complex1.re, 0.001);
        Assert.assertEquals(0.5, complex1.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.5, 3.0);
        Complex complex2 = new Complex(1.5, -2.5);
        complex1.minus(complex2);
        Assert.assertEquals(1.0, complex1.re, 0.001);
        Assert.assertEquals(5.5, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -2.0);
        complex1.times(complex2);
        Assert.assertEquals(8.0, complex1.re, 0.001);
        Assert.assertEquals(-1.0, complex1.im, 0.001);
    }
    
    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.0);
        Assert.assertEquals(6.0, complex.re, 0.001);
        Assert.assertEquals(8.0, complex.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, -3.0);
        complex.conjugate();
        Assert.assertEquals(2.0, complex.re, 0.001);
        Assert.assertEquals(3.0, complex.im, 0.001);
    }
    
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(2.5, 3.0);
        Complex complex2 = new Complex(1.5, -2.5);
        Complex result = Complex.plus(complex1, complex2);
        Assert.assertEquals(4.0, result.re, 0.001);
        Assert.assertEquals(0.5, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyWithDouble() {
        Complex complex = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complex, 2.5);
        Assert.assertEquals(7.5, result.re, 0.001);
        Assert.assertEquals(10.0, result.im, 0.001);
    }
    
    @Test
    public void testStaticMultiplyWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, -2.0);
        Complex result = Complex.multiply(complex1, complex2);
        Assert.assertEquals(8.0, result.re, 0.001);
        Assert.assertEquals(-1.0, result.im, 0.001);
    }
}