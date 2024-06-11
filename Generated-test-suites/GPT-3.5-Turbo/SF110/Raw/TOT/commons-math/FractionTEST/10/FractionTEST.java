import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorDouble() {
        // Typical case
        Fraction fraction1 = new Fraction(3.5);
        assertEquals(7, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());

        // Edge cases
        Fraction fraction2 = new Fraction(0.0);
        assertEquals(0, fraction2.getNumerator());
        assertEquals(1, fraction2.getDenominator());
        
        Fraction fraction3 = new Fraction(1.0, 0.1, 100);
        assertEquals(10, fraction3.getNumerator());
        assertEquals(1, fraction3.getDenominator());

        // Error scenarios
        try {
            Fraction fraction4 = new Fraction(Double.POSITIVE_INFINITY);
            fail("Exception not thrown for positive infinity");
        } catch (FractionConversionException e) {
            // Exception expected
        }

        try {
            Fraction fraction5 = new Fraction(Double.NEGATIVE_INFINITY);
            fail("Exception not thrown for negative infinity");
        } catch (FractionConversionException e) {
            // Exception expected
        }

        try {
            Fraction fraction6 = new Fraction(Double.NaN);
            fail("Exception not thrown for NaN");
        } catch (FractionConversionException e) {
            // Exception expected
        }
    }

    @Test
    public void testConstructorInt() {
        // Typical case
        Fraction fraction1 = new Fraction(4);
        assertEquals(4, fraction1.getNumerator());
        assertEquals(1, fraction1.getDenominator());
        
        // Edge cases
        Fraction fraction2 = new Fraction(0);
        assertEquals(0, fraction2.getNumerator());
        assertEquals(1, fraction2.getDenominator());

        // Error scenarios
        try {
            Fraction fraction3 = new Fraction(1, 0);
            fail("Exception not thrown for zero denominator");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }

    @Test
    public void testAbs() {
        // Typical case
        Fraction fraction1 = new Fraction(-3, 4);
        Fraction abs1 = fraction1.abs();
        assertEquals(3, abs1.getNumerator());
        assertEquals(4, abs1.getDenominator());

        // Edge case
        Fraction fraction2 = new Fraction(0, 5);
        Fraction abs2 = fraction2.abs();
        assertEquals(0, abs2.getNumerator());
        assertEquals(1, abs2.getDenominator());
    }

    // Continue with other test cases for all methods in the Fraction class
    // ...

}