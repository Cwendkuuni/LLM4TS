import org.junit.Test;

public class ComplexTest {
    
    @Test
    public void testConstructorWithParameters() {
        Complex complex = new Complex(2.5, 3.8);
        
        assert(complex.re == 2.5);
        assert(complex.im == 3.8);
    }
    
    @Test
    public void testConstructorWithoutParameters() {
        Complex complex = new Complex();
        
        assert(complex.re == 0.0);
        assert(complex.im == 0.0);
    }
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.5, 3.8);
        
        String expected = "2.5 + 3.8i";
        String actual = complex.toString();
        
        assert(actual.equals(expected));
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        
        double expected = 5.0;
        double actual = complex.abs();
        
        assert(actual == expected);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.5, 3.8);
        Complex complex2 = new Complex(1.2, 2.3);
        complex1.plus(complex2);
        
        assert(complex1.re == 3.7);
        assert(complex1.im == 6.1);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.5, 3.8);
        Complex complex2 = new Complex(1.2, 2.3);
        complex1.minus(complex2);
        
        assert(complex1.re == 1.3);
        assert(complex1.im == 1.5);
    }
    
    @Test
    public void testTimesWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.times(complex2);
        
        assert(complex1.re == -4.0);
        assert(complex1.im == 7.0);
    }
    
    @Test
    public void testTimesWithAlpha() {
        Complex complex = new Complex(2.5, 3.8);
        complex.times(2.0);
        
        assert(complex.re == 5.0);
        assert(complex.im == 7.6);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.5, 3.8);
        complex.conjugate();
        
        assert(complex.im == -3.8);
    }
    
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(2.2, 3.4);
        Complex complex2 = new Complex(1.1, 2.2);
        
        Complex result = Complex.plus(complex1, complex2);
        
        assert(result.re == 3.3);
        assert(result.im == 5.6);
    }
    
    @Test
    public void testStaticMultiplyWithDouble() {
        Complex complex = new Complex(2.5, 3.8);
        double alpha = 1.5;
        
        Complex result = Complex.multiply(complex, alpha);
        
        assert(result.re == 3.75);
        assert(result.im == 5.7);
    }
    
    @Test
    public void testStaticMultiplyWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        Complex result = Complex.multiply(complex1, complex2);
        
        assert(result.re == -4.0);
        assert(result.im == 7.0);
    }
}