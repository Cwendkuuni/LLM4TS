
import org.junit.Test;

public class ComplexTest {
    
    @Test
    public void testConstructor() {
        Complex complex = new Complex(2.0, 3.0);
        
        double expectedReal = 2.0;
        double expectedImaginary = 3.0;
        
        assert(complex.re == expectedReal);
        assert(complex.im == expectedImaginary);
    }
    
    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        
        String expectedString = "2.0 + 3.0i";
        
        assert(complex.toString().equals(expectedString));
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        
        double expectedAbs = 5.0;
        
        assert(complex.abs() == expectedAbs);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        complex1.plus(complex2);
        
        double expectedReal = 3.0;
        double expectedImaginary = 5.0;
        
        assert(complex1.re == expectedReal);
        assert(complex1.im == expectedImaginary);
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        complex1.minus(complex2);
        
        double expectedReal = 1.0;
        double expectedImaginary = 1.0;
        
        assert(complex1.re == expectedReal);
        assert(complex1.im == expectedImaginary);
    }
    
    @Test
    public void testTimesWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        complex1.times(complex2);
        
        double expectedReal = -4.0;
        double expectedImaginary = 7.0;
        
        assert(complex1.re == expectedReal);
        assert(complex1.im == expectedImaginary);
    }
    
    @Test
    public void testTimesWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        
        complex.times(2.0);
        
        double expectedReal = 4.0;
        double expectedImaginary = 6.0;
        
        assert(complex.re == expectedReal);
        assert(complex.im == expectedImaginary);
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        
        complex.conjugate();
        
        double expectedImaginary = -3.0;
        
        assert(complex.im == expectedImaginary);
    }
    
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        Complex result = Complex.plus(complex1, complex2);
        
        double expectedReal = 3.0;
        double expectedImaginary = 5.0;
        
        assert(result.re == expectedReal);
        assert(result.im == expectedImaginary);
    }
    
    @Test
    public void testStaticMultiplyWithDouble() {
        Complex complex = new Complex(2.0, 3.0);
        
        Complex result = Complex.multiply(complex, 2.0);
        
        double expectedReal = 4.0;
        double expectedImaginary = 6.0;
        
        assert(result.re == expectedReal);
        assert(result.im == expectedImaginary);
    }
    
    @Test
    public void testStaticMultiplyWithComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 2.0);
        
        Complex result = Complex.multiply(complex1, complex2);
        
        double expectedReal = -4.0;
        double expectedImaginary = 7.0;
        
        assert(result.re == expectedReal);
        assert(result.im == expectedImaginary);
    }
}
