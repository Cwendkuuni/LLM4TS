import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {
    
    @Test
    public void testConstructor_doubleValue() {
        Fraction fraction = new Fraction(3.5);
        assertEquals(7, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testConstructor_intValue() {
        Fraction fraction = new Fraction(2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }
    
    @Test
    public void testConstructor_intValue_denominator() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }
    
    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 5);
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(5, absFraction.getDenominator());
    }
    
    // More test cases for other methods can be added here
    
    @Test(expected = MathArithmeticException.class)
    public void testZeroDenominatorException() {
        new Fraction(1, 0);
    }
    
    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(4, 6);
        assertEquals(fraction1, fraction2);
    }
    
}