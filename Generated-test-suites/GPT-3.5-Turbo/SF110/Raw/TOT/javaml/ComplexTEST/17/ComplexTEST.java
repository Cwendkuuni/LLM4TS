import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    private Complex complex;

    @Before
    public void setUp() {
        complex = new Complex(3.0, 4.0);
    }

    // Test for the constructor with arguments
    @Test
    public void testComplexConstructor() {
        assertEquals(3.0, complex.re, 0.001);
        assertEquals(4.0, complex.im, 0.001);
    }

    // Test for the default constructor
    @Test
    public void testDefaultConstructor() {
        Complex defaultComplex = new Complex();
        assertEquals(0.0, defaultComplex.re, 0.001);
        assertEquals(0.0, defaultComplex.im, 0.001);
    }

    // Test for the toString() method
    @Test
    public void testToString() {
        assertEquals("3.0 + 4.0i", complex.toString());
    }

    // Test for the abs() method
    @Test
    public void testAbs() {
        double absValue = complex.abs();
        assertEquals(5.0, absValue, 0.001);
    }

    // Test for the plus() method
    @Test
    public void testPlus() {
        Complex b = new Complex(2.0, 3.0);
        complex.plus(b);
        assertEquals(5.0, complex.re, 0.001);
        assertEquals(7.0, complex.im, 0.001);
    }

    // Test for the minus() method
    @Test
    public void testMinus() {
        Complex b = new Complex(2.0, 3.0);
        complex.minus(b);
        assertEquals(1.0, complex.re, 0.001);
        assertEquals(1.0, complex.im, 0.001);
    }

    // Test for the times(Complex) method
    @Test
    public void testTimesComplex() {
        Complex b = new Complex(2.0, 3.0);
        complex.times(b);
        assertEquals(-6.0, complex.re, 0.001);
        assertEquals(17.0, complex.im, 0.001);
    }

    // Test for the times(double) method
    @Test
    public void testTimesDouble() {
        complex.times(2.0);
        assertEquals(6.0, complex.re, 0.001);
        assertEquals(8.0, complex.im, 0.001);
    }

    // Test for the conjugate() method
    @Test
    public void testConjugate() {
        complex.conjugate();
        assertEquals(3.0, complex.re, 0.001);
        assertEquals(-4.0, complex.im, 0.001);
    }

    // Test for the plus(Complex, Complex) method
    @Test
    public void testStaticPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.plus(a, b);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    // Test for the multiply(Complex, double) method
    @Test
    public void testStaticMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(a, 2.0);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    // Test for the multiply(Complex, Complex) method
    @Test
    public void testStaticMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-7.0, result.re, 0.001);
        assertEquals(22.0, result.im, 0.001);
    }

    // test for the I constant
    @Test
    public void testIConstant() {
        assertEquals(0.0, Complex.I.re, 0.001);
        assertEquals(1.0, Complex.I.im, 0.001);
    }
}