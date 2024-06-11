import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    // Testing the Complex constructor with valid values
    public void testComplexConstructor_ValidValues() {
        Complex complex = new Complex(1.2, 3.4);
        assertEquals(1.2, complex.re, 0.00001);
        assertEquals(3.4, complex.im, 0.00001);
    }

    @Test
    // Testing the Complex constructor with default values
    public void testComplexConstructor_DefaultValues() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.00001);
        assertEquals(0.0, complex.im, 0.00001);
    }

    @Test
    // Testing the toString method
    public void testToString() {
        Complex complex1 = new Complex(1.2, 3.4);
        assertEquals("1.2 + 3.4i", complex1.toString());

        Complex complex2 = new Complex(-2.1, 4.3);
        assertEquals("-2.1 + 4.3i", complex2.toString());
    }

    @Test
    // Testing the abs method
    public void testAbs() {
        Complex complex1 = new Complex(3.0, 4.0);
        assertEquals(5.0, complex1.abs(), 0.00001);

        Complex complex2 = new Complex(-2.0, -4.0);
        assertEquals(4.47214, complex2.abs(), 0.00001);
    }

    @Test
    // Testing the plus method
    public void testPlus() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(3.0, 4.0);
        complex1.plus(complex2);
        assertEquals(4.0, complex1.re, 0.00001);
        assertEquals(6.0, complex1.im, 0.00001);
    }
    
    @Test
    // Testing the minus method
    public void testMinus() {
        Complex complex1 = new Complex(5.0, 10.0);
        Complex complex2 = new Complex(2.0, 4.0);
        complex1.minus(complex2);
        assertEquals(3.0, complex1.re, 0.00001);
        assertEquals(6.0, complex1.im, 0.00001);
    }

    @Test
    // Testing the times method with a Complex parameter
    public void testTimes_Complex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        complex1.times(complex2);
        assertEquals(-7.0, complex1.re, 0.00001);
        assertEquals(22.0, complex1.im, 0.00001);
    }

    @Test
    // Testing the times method with a double parameter
    public void testTimes_Double() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(1.5);
        assertEquals(3.0, complex.re, 0.00001);
        assertEquals(4.5, complex.im, 0.00001);
    }

    @Test
    // Testing the conjugate method
    public void testConjugate() {
        Complex complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.00001);
        assertEquals(-3.0, complex.im, 0.00001);
    }

    @Test
    // Testing the static plus method
    public void testStaticPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals(6.0, result.re, 0.00001);
        assertEquals(8.0, result.im, 0.00001);
    }

    @Test
    // Testing the static multiply method with a Complex and a double parameter
    public void testStaticMultiply_ComplexDouble() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex1, 1.5);
        assertEquals(3.0, result.re, 0.00001);
        assertEquals(4.5, result.im, 0.00001);
    }

    @Test
    // Testing the static multiply method with two Complex parameters
    public void testStaticMultiply_ComplexComplex() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(4.0, 5.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals(-7.0, result.re, 0.00001);
        assertEquals(22.0, result.im, 0.00001);
    }

    @Test
    // Testing the static block initialization
    public void testStaticBlockInitialization() {
        assertEquals(0.0, Complex.I.re, 0.00001);
        assertEquals(1.0, Complex.I.im, 0.00001);
    }
}