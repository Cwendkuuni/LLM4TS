import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {
    
    private Complex c1;
    private Complex c2;
    
    @Before
    public void setUp() {
        c1 = new Complex(2.0, 3.0);
        c2 = new Complex(4.0, 5.0);
    }
    
    @Test
    public void testToString() {
        assertEquals("2.0 + 3.0i", c1.toString());
        assertEquals("4.0 + 5.0i", c2.toString());
    }
    
    @Test
    public void testAbs() {
        assertEquals(3.605551275463989, c1.abs(), 0.0001);
        assertEquals(6.4031242374328485, c2.abs(), 0.0001);
        
        c1 = new Complex(0.0, 0.0);
        assertEquals(0.0, c1.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        c1.plus(c2);
        assertEquals(6.0, c1.re, 0.0001);
        assertEquals(8.0, c1.im, 0.0001);
        
        assertEquals(4.0, c2.re, 0.0001);
        assertEquals(5.0, c2.im, 0.0001);
    }
    
    @Test
    public void testMinus() {
        c1.minus(c2);
        assertEquals(-2.0, c1.re, 0.0001);
        assertEquals(-2.0, c1.im, 0.0001);
        
        assertEquals(4.0, c2.re, 0.0001);
        assertEquals(5.0, c2.im, 0.0001);
    }
    
    @Test
    public void testTimesComplex() {
        c1.times(c2);
        assertEquals(-7.0, c1.re, 0.0001);
        assertEquals(22.0, c1.im, 0.0001);
        
        assertEquals(4.0, c2.re, 0.0001);
        assertEquals(5.0, c2.im, 0.0001);
    }
    
    @Test
    public void testTimesAlpha() {
        c1.times(2.0);
        assertEquals(4.0, c1.re, 0.0001);
        assertEquals(6.0, c1.im, 0.0001);
    }
    
    @Test
    public void testConjugate() {
        c1.conjugate();
        assertEquals(2.0, c1.re, 0.0001);
        assertEquals(-3.0, c1.im, 0.0001);
    }
    
    @Test
    public void testPlusStatic() {
        Complex result = Complex.plus(c1, c2);
        
        assertEquals(6.0, result.re, 0.0001);
        assertEquals(8.0, result.im, 0.0001);
        
        assertEquals(2.0, c1.re, 0.0001);
        assertEquals(3.0, c1.im, 0.0001);
        
        assertEquals(4.0, c2.re, 0.0001);
        assertEquals(5.0, c2.im, 0.0001);
    }
    
    @Test
    public void testMultiplyStaticDouble() {
        Complex result = Complex.multiply(c1, 2.0);
        assertEquals(4.0, result.re, 0.0001);
        assertEquals(6.0, result.im, 0.0001);
        
        assertEquals(2.0, c1.re, 0.0001);
        assertEquals(3.0, c1.im, 0.0001);
    }
    
    @Test
    public void testMultiplyStaticComplex() {
        Complex result = Complex.multiply(c1, c2);
        assertEquals(-7.0, result.re, 0.0001);
        assertEquals(22.0, result.im, 0.0001);
        
        assertEquals(2.0, c1.re, 0.0001);
        assertEquals(3.0, c1.im, 0.0001);
        
        assertEquals(4.0, c2.re, 0.0001);
        assertEquals(5.0, c2.im, 0.0001);
    }
}