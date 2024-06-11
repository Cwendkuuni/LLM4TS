import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ComplexTest {

    private Complex complex1, complex2, complex3;

    @Before
    public void setUp() {
        complex1 = new Complex(3.0, 4.0);
        complex2 = new Complex(-2.0, -6.0);
        complex3 = new Complex();
    }

    @Test
    public void testConstructorWithParameters() {
        assertEquals(3.0, complex1.re, 0.001);
        assertEquals(4.0, complex1.im, 0.001);
    }

    @Test
    public void testConstructorWithoutParameters() {
        assertEquals(0.0, complex3.re, 0.001);
        assertEquals(0.0, complex3.im, 0.001);
    }

    @Test
    public void testToString() {
        assertEquals("3.0 + 4.0i", complex1.toString());
    }

    @Test
    public void testAbs() {
        assertEquals(5.0, complex1.abs(), 0.001);
        assertEquals(6.324, complex2.abs(), 0.001);
    }

    @Test
    public void testPlusWithComplex() {
        complex1.plus(complex2);
        assertEquals(1.0, complex1.re, 0.001);
        assertEquals(-2.0, complex1.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex complexSum = Complex.plus(complex1, complex2);
        assertEquals(1.0, complexSum.re, 0.001);
        assertEquals(-2.0, complexSum.im, 0.001);
    }

    @Test
    public void testMinus() {
        complex1.minus(complex2);
        assertEquals(5.0, complex1.re, 0.001);
        assertEquals(10.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesWithComplex() {
        complex1.times(complex2);
        assertEquals(-10.0, complex1.re, 0.001);
        assertEquals(-30.0, complex1.im, 0.001);
    }

    @Test
    public void testTimesWithDouble() {
        complex1.times(2.0);
        assertEquals(6.0, complex1.re, 0.001);
        assertEquals(8.0, complex1.im, 0.001);
    }

    @Test
    public void testMultiplyStaticWithDouble() {
        Complex complexProduct = Complex.multiply(complex1, 3.0);
        assertEquals(9.0, complexProduct.re, 0.001);
        assertEquals(12.0, complexProduct.im, 0.001);
    }

    @Test
    public void testMultiplyStaticWithComplex() {
        Complex complexProduct = Complex.multiply(complex1, complex2);
        assertEquals(-10.0, complexProduct.re, 0.001);
        assertEquals(-30.0, complexProduct.im, 0.001);
    }

    @Test
    public void testConjugate() {
        complex1.conjugate();
        assertEquals(3.0, complex1.re, 0.001);
        assertEquals(-4.0, complex1.im, 0.001);
    }

    @Test
    public void testImStatic() {
        assertEquals(1.0, Complex.I.im, 0.001);
    }

    @Test
    public void testEdgeCase() {
        Complex complex4 = new Complex(1.0, 0.0);
        assertEquals(1.0, complex4.abs(), 0.001);
    }

    @Test
    public void testErrorScenario() {
        Complex complex5 = new Complex(0.0, 0.0);
        assertEquals(0.0, complex5.abs(), 0.001);
    }
}