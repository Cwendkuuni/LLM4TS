import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    private Complex complex;

    @Test
    public void testComplexConstructor() {
        complex = new Complex(2.0, 3.0);
        assertEquals(2.0, complex.re, 0.0);
        assertEquals(3.0, complex.im, 0.0);
    }

    @Test
    public void testComplexDefaultConstructor() {
        complex = new Complex();
        assertEquals(0.0, complex.re, 0.0);
        assertEquals(0.0, complex.im, 0.0);
    }

    @Test
    public void testToString() {
        complex = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0);
    }

    @Test
    public void testPlus() {
        complex = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        complex.plus(b);
        assertEquals(3.0, complex.re, 0.0);
        assertEquals(5.0, complex.im, 0.0);
    }

    @Test
    public void testMinus() {
        complex = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        complex.minus(b);
        assertEquals(1.0, complex.re, 0.0);
        assertEquals(1.0, complex.im, 0.0);
    }

    @Test
    public void testTimesComplex() {
        complex = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        complex.times(b);
        assertEquals(-4.0, complex.re, 0.0);
        assertEquals(7.0, complex.im, 0.0);
    }

    @Test
    public void testTimesDouble() {
        complex = new Complex(2.0, 3.0);
        double alpha = 2.0;
        complex.times(alpha);
        assertEquals(4.0, complex.re, 0.0);
        assertEquals(6.0, complex.im, 0.0);
    }

    @Test
    public void testConjugate() {
        complex = new Complex(2.0, 3.0);
        complex.conjugate();
        assertEquals(2.0, complex.re, 0.0);
        assertEquals(-3.0, complex.im, 0.0);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.plus(a, b);
        assertEquals(3.0, result.re, 0.0);
        assertEquals(5.0, result.im, 0.0);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.0;
        Complex result = Complex.multiply(a, d);
        assertEquals(4.0, result.re, 0.0);
        assertEquals(6.0, result.im, 0.0);
    }

    @Test
    public void testMultiplyComplex(){
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-4.0, result.re, 0.0);
        assertEquals(7.0, result.im, 0.0);
    }
}