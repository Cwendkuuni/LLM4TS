import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    /********** Test cases for Complex() **********/
    
    @Test
    public void testComplexWithDefaultValues() {
        Complex complex = new Complex();
        Assert.assertEquals(0.0, complex.im, 0.0);
        Assert.assertEquals(0.0, complex.re, 0.0);
    }
    
    @Test
    public void testComplexWithGivenValues() {
        Complex complex = new Complex(2.5, -3.5);
        Assert.assertEquals(-3.5, complex.im, 0.0);
        Assert.assertEquals(2.5, complex.re, 0.0);
    }
    
    /********** Test cases for toString() **********/
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, -3.5);
        String expected = "2.5 + -3.5i";
        Assert.assertEquals(expected, complex.toString());
    }
    
    /********** Test cases for abs() **********/
    
    @Test
    public void testAbsWithZeroValues() {
        Complex complex = new Complex();
        double expected = 0.0;
        Assert.assertEquals(expected, complex.abs(), 0.0);
    }
    
    @Test
    public void testAbsWithPositiveValues() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = 5.0;
        Assert.assertEquals(expected, complex.abs(), 0.0);
    }
    
    @Test
    public void testAbsWithNegativeValues() {
        Complex complex = new Complex(-3.0, -4.0);
        double expected = 5.0;
        Assert.assertEquals(expected, complex.abs(), 0.0);
    }
    
    /********** Test cases for plus(Complex b) **********/
    
    @Test
    public void testPlusWithZeroValues() {
        Complex complex1 = new Complex();
        Complex complex2 = new Complex();
        
        complex1.plus(complex2);
        
        Assert.assertEquals(0.0, complex1.im, 0.0);
        Assert.assertEquals(0.0, complex1.re, 0.0);
    }
    
    @Test
    public void testPlusWithPositiveValues() {
        Complex complex1 = new Complex(1.5, 2.5);
        Complex complex2 = new Complex(3.0, 4.0);
        
        complex1.plus(complex2);
        
        Assert.assertEquals(6.0, complex1.im, 0.0);
        Assert.assertEquals(4.5, complex1.re, 0.0);
    }
    
    @Test
    public void testPlusWithNegativeValues() {
        Complex complex1 = new Complex(-1.5, -2.5);
        Complex complex2 = new Complex(-3.0, -4.0);
        
        complex1.plus(complex2);
        
        Assert.assertEquals(-6.0, complex1.im, 0.0);
        Assert.assertEquals(-4.5, complex1.re, 0.0);
    }
    
    /********** Test cases for minus(Complex b) **********/
    
    @Test
    public void testMinusWithZeroValues() {
        Complex complex1 = new Complex();
        Complex complex2 = new Complex();
        
        complex1.minus(complex2);
        
        Assert.assertEquals(0.0, complex1.im, 0.0);
        Assert.assertEquals(0.0, complex1.re, 0.0);
    }
    
    @Test
    public void testMinusWithPositiveValues() {
        Complex complex1 = new Complex(1.5, 2.5);
        Complex complex2 = new Complex(3.0, 4.0);
        
        complex1.minus(complex2);
        
        Assert.assertEquals(-1.5, complex1.im, 0.0);
        Assert.assertEquals(-1.5, complex1.re, 0.0);
    }
    
    @Test
    public void testMinusWithNegativeValues() {
        Complex complex1 = new Complex(-1.5, -2.5);
        Complex complex2 = new Complex(-3.0, -4.0);
        
        complex1.minus(complex2);
        
        Assert.assertEquals(1.5, complex1.im, 0.0);
        Assert.assertEquals(1.5, complex1.re, 0.0);
    }
    
    /********** Test cases for times(Complex b) **********/
    
    @Test
    public void testTimesWithZeroValues() {
        Complex complex1 = new Complex();
        Complex complex2 = new Complex();
        
        complex1.times(complex2);
        
        Assert.assertEquals(0.0, complex1.im, 0.0);
        Assert.assertEquals(0.0, complex1.re, 0.0);
    }
    
    @Test
    public void testTimesWithPositiveValues() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        
        complex1.times(complex2);
        
        Assert.assertEquals(-5.0, complex1.im, 0.0);
        Assert.assertEquals(10.0, complex1.re, 0.0);
    }
    
    @Test
    public void testTimesWithNegativeValues() {
        Complex complex1 = new Complex(-1.0, -2.0);
        Complex complex2 = new Complex(-3.0, -4.0);
        
        complex1.times(complex2);
        
        Assert.assertEquals(5.0, complex1.im, 0.0);
        Assert.assertEquals(-10.0, complex1.re, 0.0);
    }
    
    /********** Test cases for times(double alpha) **********/
    
    @Test
    public void testTimesAlphaWithZeroValues() {
        Complex complex = new Complex();
        complex.times(2.5);
        Assert.assertEquals(0.0, complex.im, 0.0);
        Assert.assertEquals(0.0, complex.re, 0.0);
    }
    
    @Test
    public void testTimesAlphaWithPositiveValues() {
        Complex complex = new Complex(1.0, 2.0);
        complex.times(2.5);
        Assert.assertEquals(5.0, complex.im, 0.0);
        Assert.assertEquals(2.5, complex.re, 0.0);
    }
    
    @Test
    public void testTimesAlphaWithNegativeValues() {
        Complex complex = new Complex(-1.0, -2.0);
        complex.times(2.5);
        Assert.assertEquals(-5.0, complex.im, 0.0);
        Assert.assertEquals(-2.5, complex.re, 0.0);
    }
    
    /********** Test cases for conjugate() **********/
    
    @Test
    public void testConjugateWithZeroValues() {
        Complex complex = new Complex();
        complex.conjugate();
        Assert.assertEquals(0.0, complex.im, 0.0);
        Assert.assertEquals(0.0, complex.re, 0.0);
    }
    
    @Test
    public void testConjugateWithPositiveValues() {
        Complex complex = new Complex(1.5, 2.5);
        complex.conjugate();
        Assert.assertEquals(-2.5, complex.im, 0.0);
        Assert.assertEquals(1.5, complex.re, 0.0);
    }
    
    @Test
    public void testConjugateWithNegativeValues() {
        Complex complex = new Complex(-1.5, -2.5);
        complex.conjugate();
        Assert.assertEquals(2.5, complex.im, 0.0);
        Assert.assertEquals(-1.5, complex.re, 0.0);
    }
    
    /********** Test cases for plus(Complex a, Complex b) **********/
    
    @Test
    public void testPlusStaticWithZeroValues() {
        Complex complex1 = new Complex();
        Complex complex2 = new Complex();
        
        Complex result = Complex.plus(complex1, complex2);
        
        Assert.assertEquals(0.0, result.im, 0.0);
        Assert.assertEquals(0.0, result.re, 0.0);
    }
    
    @Test
    public void testPlusStaticWithPositiveValues() {
        Complex complex1 = new Complex(1.5, 2.5);
        Complex complex2 = new Complex(3.0, 4.0);
        
        Complex result = Complex.plus(complex1, complex2);
        
        Assert.assertEquals(6.5, result.im, 0.0);
        Assert.assertEquals(4.5, result.re, 0.0);
    }
    
    @Test
    public void testPlusStaticWithNegativeValues() {
        Complex complex1 = new Complex(-1.5, -2.5);
        Complex complex2 = new Complex(-3.0, -4.0);
        
        Complex result = Complex.plus(complex1, complex2);
        
        Assert.assertEquals(-6.5, result.im, 0.0);
        Assert.assertEquals(-4.5, result.re, 0.0);
    }
    
    /********** Test cases for multiply(Complex a, double d) **********/
    
    @Test
    public void testMultiplyStaticWithZeroValues() {
        Complex complex = new Complex();
        
        Complex result = Complex.multiply(complex, 2.5);
        
        Assert.assertEquals(0.0, result.im, 0.0);
        Assert.assertEquals(0.0, result.re, 0.0);
    }
    
    @Test
    public void testMultiplyStaticWithPositiveValues() {
        Complex complex = new Complex(1.0, 2.0);
        
        Complex result = Complex.multiply(complex, 2.5);
        
        Assert.assertEquals(5.0, result.im, 0.0);
        Assert.assertEquals(2.5, result.re, 0.0);
    }
    
    @Test
    public void testMultiplyStaticWithNegativeValues() {
        Complex complex = new Complex(-1.0, -2.0);
        
        Complex result = Complex.multiply(complex, 2.5);
        
        Assert.assertEquals(-5.0, result.im, 0.0);
        Assert.assertEquals(-2.5, result.re, 0.0);
    }
    
    /********** Test cases for multiply(Complex a, Complex b) **********/
    
    @Test
    public void testMultiplyStaticWithZeroValues() {
        Complex complex1 = new Complex();
        Complex complex2 = new Complex();
        
        Complex result = Complex.multiply(complex1, complex2);
        
        Assert.assertEquals(0.0, result.im, 0.0);
        Assert.assertEquals(0.0, result.re, 0.0);
    }
    
    @Test
    public void testMultiplyStaticWithPositiveValues() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        
        Complex result = Complex.multiply(complex1, complex2);
        
        Assert.assertEquals(-5.0, result.im, 0.0);
        Assert.assertEquals(10.0, result.re, 0.0);
    }
    
    @Test
    public void testMultiplyStaticWithNegativeValues() {
        Complex complex1 = new Complex(-1.0, -2.0);
        Complex complex2 = new Complex(-3.0, -4.0);
        
        Complex result = Complex.multiply(complex1, complex2);
        
        Assert.assertEquals(5.0, result.im, 0.0);
        Assert.assertEquals(-10.0, result.re, 0.0);
    }

}