import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        String expected = "2.0 + 3.0i";
        String actual = complex.toString();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        double actual = complex.abs();
        Assert.assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex complex = new Complex(2.0, 3.0);
        Complex other = new Complex(4.0, 5.0);
        
        complex.plus(other);
        double expectedRe = 6.0;
        double expectedIm = 8.0;
        Assert.assertEquals(expectedRe, complex.re, 0.0001);
        Assert.assertEquals(expectedIm, complex.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        Complex complex = new Complex(2.0, 3.0);
        Complex other = new Complex(4.0, 5.0);
        
        complex.minus(other);
        double expectedRe = -2.0;
        double expectedIm = -2.0;
        Assert.assertEquals(expectedRe, complex.re, 0.0001);
        Assert.assertEquals(expectedIm, complex.im, 0.0001);
    }
    
    @Test
    public void testTimes_Complex() {
        Complex complex = new Complex(2.0, 3.0);
        Complex other = new Complex(4.0, 5.0);
        
        complex.times(other);
        double expectedRe = -7.0;
        double expectedIm = 22.0;
        Assert.assertEquals(expectedRe, complex.re, 0.0001);
        Assert.assertEquals(expectedIm, complex.im, 0.0001);
    }
    
    @Test
    public void testTimes_double() {
        Complex complex = new Complex(2.0, 3.0);
        double alpha = 5.0;
        
        complex.times(alpha);
        double expectedRe = 10.0;
        double expectedIm = 15.0;
        Assert.assertEquals(expectedRe, complex.re, 0.0001);
        Assert.assertEquals(expectedIm, complex.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        
        complex.conjugate();
        double expectedIm = -3.0;
        Assert.assertEquals(expectedIm, complex.im, 0.0001);
    }
    
    @Test
    public void testPlus_Static() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        
        Complex result = Complex.plus(a, b);
        double expectedRe = 6.0;
        double expectedIm = 8.0;
        Assert.assertEquals(expectedRe, result.re, 0.0001);
        Assert.assertEquals(expectedIm, result.im, 0.0001);
    }
    
    @Test
    public void testMultiply_double() {
        Complex complex = new Complex(2.0, 3.0);
        double d = 5.0;
        
        Complex result = Complex.multiply(complex, d);
        double expectedRe = 10.0;
        double expectedIm = 15.0;
        Assert.assertEquals(expectedRe, result.re, 0.0001);
        Assert.assertEquals(expectedIm, result.im, 0.0001);
    }
    
    @Test
    public void testMultiply_Complex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        
        Complex result = Complex.multiply(a, b);
        double expectedRe = -7.0;
        double expectedIm = 22.0;
        Assert.assertEquals(expectedRe, result.re, 0.0001);
        Assert.assertEquals(expectedIm, result.im, 0.0001);
    }
}