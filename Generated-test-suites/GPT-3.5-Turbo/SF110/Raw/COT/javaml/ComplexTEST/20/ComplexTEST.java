import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        String expected = "1.0 + 2.0i";
        assertEquals(expected, c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        double expected = 5.0;
        assertEquals(expected, c.abs(), 0.0001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);
        a.plus(b);
        assertEquals(expected, a);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(-2.0, -2.0);
        a.minus(b);
        assertEquals(expected, a);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(-5.0, 10.0);
        a.times(b);
        assertEquals(expected, a);
    }

    @Test
    public void testTimesDouble() {
        Complex c = new Complex(1.0, 2.0);
        double alpha = 2.0;
        Complex expected = new Complex(2.0, 4.0);
        c.times(alpha);
        assertEquals(expected, c);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex expected = new Complex(1.0, -2.0);
        c.conjugate();
        assertEquals(expected, c);
    }

    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);
        Complex result = Complex.plus(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(1.0, 2.0);
        double d = 2.0;
        Complex expected = new Complex(2.0, 4.0);
        Complex result = Complex.multiply(a, d);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(-5.0, 10.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(expected, result);
    }
}