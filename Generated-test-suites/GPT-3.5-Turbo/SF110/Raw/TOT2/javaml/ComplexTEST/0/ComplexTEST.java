
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    
    @Test
    public void testConstructor() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(3.0, c.re, 0.0001);
        assertEquals(4.0, c.im, 0.0001);
    }
    
    @Test
    public void testToString() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", c.toString());
    }
    
    @Test
    public void testAbsoluteValue() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.0001);
    }
    
}
