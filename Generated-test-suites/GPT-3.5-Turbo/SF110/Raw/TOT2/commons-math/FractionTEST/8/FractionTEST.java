
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorWithDoubleValue() {
        // Typical case
        Fraction fraction = new Fraction(0.5);
        assertEquals("1 / 2", fraction.toString());

        // Edge case
        fraction = new Fraction(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE + " / 1", fraction.toString());

        // Exceptional case
        try {
            fraction = new Fraction(Double.NaN);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithIntValues() {
        // Typical case
        Fraction fraction = new Fraction(2);
        assertEquals("2 / 1", fraction.toString());

        // Edge case
        fraction = new Fraction(Integer.MIN_VALUE, 1);
        assertEquals(Integer.MIN_VALUE + " / 1", fraction.toString());

        // Exceptional case
        try {
            fraction = new Fraction(1, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    // Additional test cases for other methods...
}

