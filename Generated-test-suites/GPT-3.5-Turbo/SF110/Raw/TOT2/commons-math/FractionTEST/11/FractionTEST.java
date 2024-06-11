import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorValue() {
        Fraction f = new Fraction(3.5);
        assertEquals("Failed to create Fraction from double value", new Fraction(7, 2), f);
    }

    @Test
    public void testConstructorMaxDenominator() {
        Fraction f = new Fraction(0.66, 3);
        assertEquals("Failed to create Fraction with specified max denominator", new Fraction(2, 3), f);
    }

    @Test
    public void testConstructorNumDen() {
        Fraction f = new Fraction(4, 6);
        assertEquals("Failed to create Fraction from numerator and denominator", new Fraction(2, 3), f);
    }
    
    @Test
    public void testAbs() {
        Fraction f = new Fraction(-2, 3);
        assertEquals("Failed to get absolute value of Fraction", new Fraction(2, 3), f.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 2);
        assertEquals("Failed to compare Fractions correctly", 1, f1.compareTo(f2));
    }

    // Add more test cases for other methods as needed

}