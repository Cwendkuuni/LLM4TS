
import org.junit.Test;

public class ComplexTest {
    
    @Test
    public void testConstructorWithArguments() {
        // Typical use case
        Complex c = new Complex(3.5, 2.5);
        assert(c.re == 3.5);
        assert(c.im == 2.5);
    }
    
    @Test
    public void testConstructorWithoutArguments() {
        Complex c = new Complex();
        // Typical use case
        assert(c.re == 0.0);
        assert(c.im == 0.0);
    }
    
    @Test
    public void testToString() {
        // Typical use case
        Complex c = new Complex(3.5, 2.5);
        String expected = "3.5 + 2.5i";
        assert(c.toString().equals(expected));
    }
    
    @Test
    public void testAbs() {
        // Typical use case
        Complex c = new Complex(3.0, 4.0);
        assert(c.abs() == 5.0);
        
        // Edge case: zero magnitude
        Complex zeroComplex = new Complex(0.0, 0.0);
        assert(zeroComplex.abs() == 0.0);
    }
    
    @Test
    public void testPlus() {
        Complex c1 = new Complex(3.5, 2.5);
        Complex c2 = new Complex(1.2, 0.7);
        
        // Typical use case
        c1.plus(c2);
        assert(c1.re == 4.7);
        assert(c1.im == 3.2);
    }
    
    @Test
    public void testMinus() {
        Complex c1 = new Complex(3.5, 2.5);
        Complex c2 = new Complex(1.2, 0.7);
        
        // Typical use case
        c1.minus(c2);
        assert(c1.re == 2.3);
        assert(c1.im == 1.8);
    }
    
    @Test
    public void testTimesWithComplex() {
        Complex c1 = new Complex(3.5, 2.5);
        Complex c2 = new Complex(1.2, 0.7);
        
        // Typical use case
        c1.times(c2);
        assert(c1.re == 2.75);
        assert(c1.im == 9.45);
    }
    
    @Test
    public void testTimesWithDouble() {
        Complex c = new Complex(3.5, 2.5);
        
        // Typical use case
        c.times(2.0);
        assert(c.re == 7.0);
        assert(c.im == 5.0);
    }
    
    @Test
    public void testConjugate() {
        Complex c = new Complex(3.5, 2.5);
        
        // Typical use case
        c.conjugate();
        assert(c.re == 3.5);
        assert(c.im == -2.5);
    }
    
    @Test
    public void testPlusStaticMethod() {
        Complex a = new Complex(3.5, 2.5);
        Complex b = new Complex(1.2, 0.7);
        
        // Typical use case
        Complex result = Complex.plus(a, b);
        assert(result.re == 4.7);
        assert(result.im == 3.2);
    }
    
    @Test
    public void testMultiplyWithDoubleStaticMethod() {
        Complex a = new Complex(3.5, 2.5);
        double d = 1.5;
        
        // Typical use case
        Complex result = Complex.multiply(a, d);
        assert(result.re == 5.25);
        assert(result.im == 3.75);
    }
    
    @Test
    public void testMultiplyWithComplexStaticMethod() {
        Complex a = new Complex(3.5, 2.5);
        Complex b = new Complex(1.2, 0.7);
        
        // Typical use case
        Complex result = Complex.multiply(a, b);
        assert(result.re == -0.85);
        assert(result.im == 9.45);
    }
     
}
