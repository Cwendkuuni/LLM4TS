import org.junit.Test;

public class ComplexTest {
    
    @Test
    public void testToString() {
        Complex c = new Complex(3.0, 4.0);
        String expected = "3.0 + 4.0i";
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
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 5.0);
        a.plus(b);
        Complex expected = new Complex(5.0, 9.0);
        assertEquals(expected.toString(), a.toString());
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 5.0);
        a.minus(b);
        Complex expected = new Complex(1.0, -1.0);
        assertEquals(expected.toString(), a.toString());
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 5.0);
        a.times(b);
        Complex expected = new Complex(-14.0, 23.0);
        assertEquals(expected.toString(), a.toString());
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(3.0, 4.0);
        double alpha = 2.0;
        a.times(alpha);
        Complex expected = new Complex(6.0, 8.0);
        assertEquals(expected.toString(), a.toString());
    }
    
    @Test
    public void testConjugate() {
        Complex a = new Complex(3.0, 4.0);
        a.conjugate();
        Complex expected = new Complex(3.0, -4.0);
        assertEquals(expected.toString(), a.toString());
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 5.0);
        Complex result = Complex.plus(a, b);
        Complex expected = new Complex(5.0, 9.0);
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(3.0, 4.0);
        double d = 2.0;
        Complex result = Complex.multiply(a, d);
        Complex expected = new Complex(6.0, 8.0);
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(2.0, 5.0);
        Complex result = Complex.multiply(a, b);
        Complex expected = new Complex(-14.0, 23.0);
        assertEquals(expected.toString(), result.toString());
    }
}