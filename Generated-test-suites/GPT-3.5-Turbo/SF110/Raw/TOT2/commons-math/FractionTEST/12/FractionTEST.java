import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    
    @Test
    public void testAbs() {
        // Typical use case
        Fraction fraction1 = new Fraction(-5, 3);
        assertEquals(new Fraction(5, 3), fraction1.abs());
        
        // Edge case
        Fraction fraction2 = new Fraction(Integer.MIN_VALUE, 1);
        assertEquals(new Fraction(Integer.MIN_VALUE, 1), fraction2.abs());
    }
    
    @Test
    public void testCompareTo() {
        // Typical use case
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals(-1, fraction1.compareTo(fraction2));
        
        // Edge case
        Fraction fraction3 = new Fraction(Integer.MAX_VALUE, 1);
        Fraction fraction4 = new Fraction(-1, 1);
        assertEquals(1, fraction3.compareTo(fraction4));
    }
    
    // Add more test cases for the remaining methods
    
}