import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    
    @Test
    public void testFractionConstructorWithValue() {
        Fraction fraction = new Fraction(2.5);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testFractionConstructorWithMaxDenominator() {
        Fraction fraction = new Fraction(3.14159, 1000);
        assertEquals(355, fraction.getNumerator());
        assertEquals(113, fraction.getDenominator());
    }
    
    @Test
    public void testFractionConstructorWithNegativeValue() {
        try {
            Fraction fraction = new Fraction(-2.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFractionConstructorWithZeroDenominator() {
        try {
            Fraction fraction = new Fraction(1, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 5);
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(5, absFraction.getDenominator());
    }
    
    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 2);
        assertEquals(1, fraction1.compareTo(fraction2));
    }
    
    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }
    
    // Add more test cases for other methods
    
}