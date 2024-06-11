import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Basic test case
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());

        // Test case with whole number
        fraction = new Fraction(5, 4);
        expected = "1 1 / 4";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Basic test case
        String source = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, format.parse(source));

        // Test case with whole number
        source = "1 1 / 4";
        expected = new Fraction(5, 4);
        assertEquals(expected, format.parse(source));

        // Test case with invalid format
        source = "3.4";
        assertNull(format.parse(source));
    }

    // Test cases for edge cases and exception handling scenarios

    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test case with negative fraction
        Fraction fraction = new Fraction(-3, 4);
        String expected = "-3 / 4";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());

        // Test case with negative whole number
        fraction = new Fraction(-5, 4);
        expected = "-1 1 / 4";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testParseWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test case with negative fraction
        String source = "-3 / 4";
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, format.parse(source));

        // Test case with negative whole number
        source = "-1 1 / 4";
        expected = new Fraction(-5, 4);
        assertEquals(expected, format.parse(source));
    }

    @Test
    public void testParseWithInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test case with negative numerator
        String source = "-3 / 4";
        assertNull(format.parse(source));

        // Test case with negative denominator
        source = "3 / -4";
        assertNull(format.parse(source));

        // Test case with zero denominator
        source = "3 / 0";
        assertNull(format.parse(source));
    }
}

