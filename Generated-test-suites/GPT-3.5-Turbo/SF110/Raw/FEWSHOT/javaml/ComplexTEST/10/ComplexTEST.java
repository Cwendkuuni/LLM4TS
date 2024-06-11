import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComplexTest {
    @Test
    public void testToString() {
        Complex c = new Complex(1.5, 2.5);
        assertEquals("1.5 + 2.5i", c.toString());
        
        c = new Complex(-3.2, -4.8);
        assertEquals("-3.2 + -4.8i", c.toString());
    }
    
    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.001);
        
        c = new Complex(-2.0, -2.0);
        assertEquals(2.828, c.abs(), 0.001);
    }
    
    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        
        a.plus(b);
        assertEquals("4.0 + 6.0i", a.toString());
    }
    
    @Test
    public void testMinus() {
        Complex a = new Complex(5.0, 10.0);
        Complex b = new Complex(2.0, 3.0);
        
        a.minus(b);
        assertEquals("3.0 + 7.0i", a.toString());
    }
    
    @Test
    public void testTimesComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, -2.0);
        
        a.times(b);
        assertEquals("8.0 + -1.0i", a.toString());
    }
    
    @Test
    public void testTimesDouble() {
        Complex a = new Complex(3.0, 4.0);
        double alpha = 2.0;
        
        a.times(alpha);
        assertEquals("6.0 + 8.0i", a.toString());
    }
    
    @Test
    public void testConjugate() {
        Complex a = new Complex(2.0, 3.0);
        
        a.conjugate();
        assertEquals("2.0 + -3.0i", a.toString());
    }
    
    @Test
    public void testPlusStatic() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        
        Complex result = Complex.plus(a, b);
        assertEquals("4.0 + 6.0i", result.toString());
    }
    
    @Test
    public void testMultiplyDouble() {
        Complex a = new Complex(2.0, 3.0);
        double d = 3.0;
        
        Complex result = Complex.multiply(a, d);
        assertEquals("6.0 + 9.0i", result.toString());
    }
    
    @Test
    public void testMultiplyComplex() {
        Complex a = new Complex(2.0, 3.0);
        Complex b = new Complex(1.0, -2.0);
        
        Complex result = Complex.multiply(a, b);
        assertEquals("8.0 + -1.0i", result.toString());
    }
}