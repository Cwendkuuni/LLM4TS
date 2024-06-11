import static org.junit.Assert.*;
import org.junit.*;

public class ComplexTest {

    // Test case for the constructor with parameters (typical use case)
    @Test
    public void testConstructorWithParams() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", complex.toString());
    }

    // Test case for the constructor without parameters (typical use case)
    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals("0.0 + 0.0i", complex.toString());
    }

    // Test case for the abs() method (typical use case)
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.001);
    }

    // Test case for the plus(Complex b) method (typical use case)
    @Test
    public void testPlusComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        complex1.plus(complex2);
        assertEquals("5.0 + 5.0i", complex1.toString());
    }

    // Test case for the minus(Complex b) method (typical use case)
    @Test
    public void testMinusComplex() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        complex1.minus(complex2);
        assertEquals("1.0 + 3.0i", complex1.toString());
    }

    // Test case for the times(Complex b) method (typical use case)
    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.times(complex2);
        assertEquals("-4.0 + 7.0i", complex1.toString());
    }

    // Test case for the times(double alpha) method (typical use case)
    @Test
    public void testTimesDouble() {
        Complex complex = new Complex(3.0, 4.0);
        complex.times(2.0);
        assertEquals("6.0 + 8.0i", complex.toString());
    }

    // Test case for the conjugate() method (typical use case)
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        assertEquals("3.0 - 4.0i", complex.toString());
    }

    // Test case for the plus(Complex a, Complex b) method (typical use case)
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(2.0, 1.0);
        Complex sum = Complex.plus(complex1, complex2);
        assertEquals("5.0 + 5.0i", sum.toString());
    }

    // Test case for the multiply(Complex a, double d) method (typical use case)
    @Test
    public void testStaticMultiplyDouble() {
        Complex complex = new Complex(2.0, 3.0);
        Complex product = Complex.multiply(complex, 2.0);
        assertEquals("4.0 + 6.0i", product.toString());
    }

    // Test case for the multiply(Complex a, Complex b) method (typical use case)
    @Test
    public void testStaticMultiplyComplex() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex product = Complex.multiply(complex1, complex2);
        assertEquals("-4.0 + 7.0i", product.toString());
    }
    
    // Test case for edge case of dividing by zero
    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(0.0, 0.0);
        complex1.divide(complex2);
    }

    // Test case for error scenario of passing null to plus(Complex b) method
    @Test(expected = NullPointerException.class)
    public void testPlusComplexNull() {
        Complex complex1 = new Complex(3.0, 4.0);
        complex1.plus(null);
    }
    
    // Test case for error scenario of passing null to minus(Complex b) method
    @Test(expected = NullPointerException.class)
    public void testMinusComplexNull() {
        Complex complex1 = new Complex(3.0, 4.0);
        complex1.minus(null);
    }
    
    // Test case for error scenario of passing null to times(Complex b) method
    @Test(expected = NullPointerException.class)
    public void testTimesComplexNull() {
        Complex complex1 = new Complex(3.0, 4.0);
        complex1.times(null);
    }

    // Test case for error scenario of passing null to plus(Complex a, Complex b) method
    @Test(expected = NullPointerException.class)
    public void testStaticPlusNull() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex.plus(complex1, null);
    }

    // Test case for error scenario of passing null to multiply(Complex a, Complex b) method
    @Test(expected = NullPointerException.class)
    public void testStaticMultiplyComplexNull() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex.multiply(complex1, null);
    }
}