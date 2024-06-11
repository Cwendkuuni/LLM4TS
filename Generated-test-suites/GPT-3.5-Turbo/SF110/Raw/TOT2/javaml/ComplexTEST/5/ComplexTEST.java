import static org.junit.Assert.*;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex testComplex = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", testComplex.toString());
    }

    @Test
    public void testAbs() {
        Complex testComplex = new Complex(3.0, 4.0);
        assertEquals(5.0, testComplex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(4.0, 5.0);
        a.plus(b);
        assertEquals(6.0, a.re, 0.001);
        assertEquals(8.0, a.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(4.0, 5.0);
        Complex b = new Complex(2.0, 3.0);
        a.minus(b);
        assertEquals(2.0, a.re, 0.001);
        assertEquals(2.0, a.im, 0.001);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(2.0, 3.0);
        a.times(b);
        assertEquals(-4.0, a.re, 0.001);
        assertEquals(7.0, a.im, 0.001);
    }

    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        a.times(2.0);
        assertEquals(4.0, a.re, 0.001);
        assertEquals(6.0, a.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex testComplex = new Complex(2.0, 3.0);
        testComplex.conjugate();
        assertEquals(2.0, testComplex.re, 0.001);
        assertEquals(-3.0, testComplex.im, 0.001);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = Complex.plus(a, b);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(a, 2.0);
        assertEquals(4.0, result.re, 0.001);
        assertEquals(6.0, result.im, 0.001);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(-4.0, result.re, 0.001);
        assertEquals(7.0, result.im, 0.001);
    }
}