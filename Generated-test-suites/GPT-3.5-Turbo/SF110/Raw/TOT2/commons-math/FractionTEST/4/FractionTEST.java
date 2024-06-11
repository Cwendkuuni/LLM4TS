import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    
    @Test
    public void testConstructorDoubleValue() {
        Fraction fraction = new Fraction(0.5);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
    }
    
    @Test
    public void testConstructorIntInt() {
        Fraction fraction = new Fraction(3, 6);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
    }
    
    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-2, 3);
        assertEquals(2, fraction.abs().getNumerator());
    }
    
    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals(-1, fraction1.compareTo(fraction2));
    }
    
    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals(65523, fraction.hashCode());
    }
    
    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(4, 6);
        assertTrue(fraction1.equals(fraction2));
    }
    
    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(5, 8);
        assertEquals(0.625, fraction.doubleValue(), 0.001);
    }
    
    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }
    
    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }
    
    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(5, 6);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(10, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }
    
    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 7);
        assertEquals("3 / 7", fraction.toString());
    }

}