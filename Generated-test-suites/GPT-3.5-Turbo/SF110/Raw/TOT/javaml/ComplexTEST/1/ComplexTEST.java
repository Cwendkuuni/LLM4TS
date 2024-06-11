import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {

    private Complex complex1;
    private Complex complex2;

    @Before
    public void setUp() {
        complex1 = new Complex(2.0, 3.0);
        complex2 = new Complex(5.0, 6.0);
    }

    @Test
    public void testConstructor() {
        assertEquals(2.0, complex1.re, 0);  
        assertEquals(3.0, complex1.im, 0);
    }

    @Test
    public void testToString() {
        assertEquals("2.0 + 3.0i", complex1.toString());
        assertEquals("5.0 + 6.0i", complex2.toString());
    }

    @Test
    public void testAbs() {
        assertEquals(3.605, complex1.abs(), 0.001);
        assertEquals(7.810, complex2.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        complex1.plus(complex2);
        assertEquals(7.0, complex1.re, 0);
        assertEquals(9.0, complex1.im, 0);
    }

    @Test
    public void testMinus() {
        complex1.minus(complex2);
        assertEquals(-3.0, complex1.re, 0);
        assertEquals(-3.0, complex1.im, 0);
    }

    @Test
    public void testTimes() {
        complex1.times(complex2);
        assertEquals(-8.0, complex1.re, 0);
        assertEquals(27.0, complex1.im, 0);
    }

    @Test
    public void testTimesWithAlpha() {
        complex1.times(2.0);
        assertEquals(4.0, complex1.re, 0);
        assertEquals(6.0, complex1.im, 0);
    }

    @Test
    public void testConjugate() {
        complex1.conjugate();
        assertEquals(2.0, complex1.re, 0);
        assertEquals(-3.0, complex1.im, 0);
    }

    @Test
    public void testStaticPlus() {
        Complex sum = Complex.plus(complex1, complex2);
        assertEquals(7.0, sum.re, 0);
        assertEquals(9.0, sum.im, 0);
    }

    @Test
    public void testStaticMultiplyWithDouble() {
        Complex product = Complex.multiply(complex1, 2.0);
        assertEquals(4.0, product.re, 0);
        assertEquals(6.0, product.im, 0);
    }

    @Test
    public void testStaticMultiplyWithComplex() {
        Complex product = Complex.multiply(complex1, complex2);
        assertEquals(-8.0, product.re, 0);
        assertEquals(27.0, product.im, 0);
    }

}