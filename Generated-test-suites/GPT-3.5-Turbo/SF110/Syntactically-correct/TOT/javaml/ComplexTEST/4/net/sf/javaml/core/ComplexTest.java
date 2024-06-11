import static org.junit.Assert.*;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c = new Complex(3.0, -4.0);
        assertEquals(3.0, c.re, 0.001);
        assertEquals(-4.0, c.im, 0.001);
    }

    @Test
    public void testConstructor_DefaultValues() {
        Complex c = new Complex();
        assertEquals(0.0, c.re, 0.001);
        assertEquals(0.0, c.im, 0.001);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(2.5, 1.0);
        String expected = "2.5 + 1.0i";
        assertEquals(expected, c.toString());
    }

    @Test
    public void testAbs_PositiveRealAndPositiveImaginary() {
        Complex c = new Complex(3.0, 4.0);
        double expected = 5.0;
        assertEquals(expected, c.abs(), 0.001);
    }

    @Test
    public void testAbs_ZeroRealAndZeroImaginary() {
        Complex c = new Complex();
        double expected = 0.0;
        assertEquals(expected, c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(-1.0, 2.0);
        c1.plus(c2);
        assertEquals(1.0, c1.re, 0.001);
        assertEquals(5.0, c1.im, 0.001);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(-1.0, 2.0);
        c1.minus(c2);
        assertEquals(3.0, c1.re, 0.001);
        assertEquals(1.0, c1.im, 0.001);
    }

    @Test
    public void testTimes_Complex() {
        Complex c1 = new Complex(3.0, 2.0);
        Complex c2 = new Complex(1.0, -4.0);
        c1.times(c2);
        assertEquals(11.0, c1.re, 0.001);
        assertEquals(-10.0, c1.im, 0.001);
    }

    @Test
    public void testTimes_Double() {
        Complex c1 = new Complex(2.0, -3.0);
        double alpha = 2.5;
        c1.times(alpha);
        assertEquals(5.0, c1.re, 0.001);
        assertEquals(-7.5, c1.im, 0.001);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(2.0, -3.0);
        c.conjugate();
        assertEquals(2.0, c.re, 0.001);
        assertEquals(3.0, c.im, 0.001);
    }

    @Test
    public void testStaticPlus() {
        Complex c1 = new Complex(2.0, 3.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex result = Complex.plus(c1, c2);
        assertEquals(1.0, result.re, 0.001);
        assertEquals(5.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiply_Complex() {
        Complex c1 = new Complex(3.0, 2.0);
        Complex c2 = new Complex(1.0, -4.0);
        Complex result = Complex.multiply(c1, c2);
        assertEquals(11.0, result.re, 0.001);
        assertEquals(-10.0, result.im, 0.001);
    }

    @Test
    public void testStaticMultiply_Double() {
        Complex c1 = new Complex(2.0, -3.0);
        double alpha = 2.5;
        Complex result = Complex.multiply(c1, alpha);
        assertEquals(5.0, result.re, 0.001);
        assertEquals(-7.5, result.im, 0.001);
    }

    @Test
    public void testStaticMultiply_Zero() {
        Complex c1 = new Complex(3.0, -2.0);
        double alpha = 0.0;
        Complex result = Complex.multiply(c1, alpha);
        assertEquals(0.0, result.re, 0.001);
        assertEquals(0.0, result.im, 0.001);
    }
}