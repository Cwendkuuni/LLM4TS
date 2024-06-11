import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor() {
        // Typical use case: creating a fraction with positive numerator and denominator
        Fraction fraction1 = new Fraction(3, 4);
        assertEquals(3, fraction1.getNumerator());
        assertEquals(4, fraction1.getDenominator());

        // Edge case: creating a fraction with negative numerator and denominator
        Fraction fraction2 = new Fraction(-5, -2);
        assertEquals(-5, fraction2.getNumerator());
        assertEquals(-2, fraction2.getDenominator());

        // Error scenario: creating a fraction with zero denominator
        try {
            Fraction fraction3 = new Fraction(2, 0);
            fail("Expected MathArithmeticException to be thrown.");
        } catch (MathArithmeticException e) {
            assertEquals("Zero denominator in fraction: 2/0", e.getMessage());
        }
    }

    @Test
    public void testAbs() {
        // Typical use case: absolute value of a positive fraction
        Fraction fraction1 = new Fraction(3, 4);
        Fraction absFraction1 = fraction1.abs();
        assertEquals(3, absFraction1.getNumerator());
        assertEquals(4, absFraction1.getDenominator());

        // Typical use case: absolute value of a negative fraction
        Fraction fraction2 = new Fraction(-5, 2);
        Fraction absFraction2 = fraction2.abs();
        assertEquals(5, absFraction2.getNumerator());
        assertEquals(2, absFraction2.getDenominator());

        // Edge case: absolute value of a fraction with numerator 0
        Fraction fraction3 = new Fraction(0, 6);
        Fraction absFraction3 = fraction3.abs();
        assertEquals(0, absFraction3.getNumerator());
        assertEquals(1, absFraction3.getDenominator());
    }

    @Test
    public void testCompareTo() {
        // Typical use case: comparing two fractions where the first is greater than the second
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        assertEquals(1, fraction1.compareTo(fraction2));

        // Typical use case: comparing two fractions where the first is smaller than the second
        Fraction fraction3 = new Fraction(5, 2);
        Fraction fraction4 = new Fraction(7, 3);
        assertEquals(-1, fraction3.compareTo(fraction4));

        // Typical use case: comparing two equal fractions
        Fraction fraction5 = new Fraction(2, 3);
        Fraction fraction6 = new Fraction(4, 6);
        assertEquals(0, fraction5.compareTo(fraction6));
    }

    // Implement test methods for the remaining methods in the Fraction class

}