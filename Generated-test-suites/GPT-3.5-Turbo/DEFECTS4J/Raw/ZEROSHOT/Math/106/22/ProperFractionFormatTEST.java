import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test with positive fraction
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        String actual = format.format(fraction);
        assertEquals(expected, actual);

        // Test with negative fraction
        fraction = new Fraction(-3, 4);
        expected = "-3 / 4";
        actual = format.format(fraction);
        assertEquals(expected, actual);

        // Test with whole number
        fraction = new Fraction(5, 4);
        expected = "1 1 / 4";
        actual = format.format(fraction);
        assertEquals(expected, actual);

        // Test with negative whole number
        fraction = new Fraction(-5, 4);
        expected = "-1 1 / 4";
        actual = format.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test valid fraction
        String source = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);

        // Test valid negative fraction
        source = "-3 / 4";
        expected = new Fraction(-3, 4);
        actual = format.parse(source);
        assertEquals(expected, actual);

        // Test valid whole number
        source = "1 1 / 4";
        expected = new Fraction(5, 4);
        actual = format.parse(source);
        assertEquals(expected, actual);

        // Test valid negative whole number
        source = "-1 1 / 4";
        expected = new Fraction(-5, 4);
        actual = format.parse(source);
        assertEquals(expected, actual);

        // Test invalid format
        source = "3 /";
        actual = format.parse(source);
        assertNull(actual);
    }
}