
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testComplexConstructorWithArgs() {
        Complex c = new Complex(2.5, 3.7);
        assertEquals(2.5, c.re, 0.001);
        assertEquals(3.7, c.im, 0.001);
    }

    @Test
    public void testComplexConstructorWithoutArgs() {
        Complex c = new Complex();
        assertEquals(0.0, c.re, 0.001);
        assertEquals(0.0, c.im, 0.001);
    }
    
    @Test
    public void testToString() {
        Complex c = new Complex(2.5, 3.7);
        String expected = "2.5 + 3.7i";
        assertEquals(expected, c.toString());
    }
    
    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        double expected = 5.0;
        assertEquals(expected, c.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(2.5, 3.7);
        Complex b = new Complex(1.3, 4.2);
        a.plus(b);
        assertEquals(3.8, a.re, 0.001);
        assertEquals(8.0, a.im, 0.001);
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 5.0);
        Complex b = new Complex(2.0, 1.0);
        a.minus(b);
        assertEquals(1.0, a.re, 0.001);
        assertEquals(4.0, a.im, 0.001);
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        a.times(b);
        assertEquals(-4.0, a.re, 0.001);
        assertEquals(7.0, a.im, 0.001);
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(2.0, 3.0);
        double alpha = 1.5;
        a.times(alpha);
        assertEquals(3.0, a.re, 0.001);
        assertEquals(4.5, a.im, 0.001);
    }
    
    @Test
    public void testConjugate() {
        Complex a = new Complex(2.0, 3.0);
        a.conjugate();
        assertEquals(2.0, a.re, 0.001);
        assertEquals(-3.0, a.im, 0.001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex c = Complex.plus(a, b);
        assertEquals(3.0, c.re, 0.001);
        assertEquals(5.0, c.im, 0.001);
    }
    
    @Test
    public void testMultiplyDoubleStatic() {
        Complex a = new Complex(2.0, 3.0);
        double d = 2.5;
        Complex c = Complex.multiply(a, d);
        assertEquals(5.0, c.re, 0.001);
        assertEquals(7.5, c.im, 0.001);
    }
    
    @Test
    public void testMultiplyComplexStatic() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, 2.0);
        Complex c = Complex.multiply(a, b);
        assertEquals(-4.0, c.re, 0.001);
        assertEquals(7.0, c.im, 0.001);
    }
}
