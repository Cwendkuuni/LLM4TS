import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test positive fraction without whole number
        assertEquals("1 / 2", format.format(new Fraction(1, 2)));

        // Test positive fraction with whole number
        assertEquals("1 1 / 2", format.format(new Fraction(3, 2)));

        // Test negative fraction without whole number
        assertEquals("-1 / 2", format.format(new Fraction(-1, 2)));

        // Test negative fraction with whole number
        assertEquals("-1 1 / 2", format.format(new Fraction(-3, 2)));

        // Test fraction with zero numerator
        assertEquals("0 / 2", format.format(new Fraction(0, 2)));

        // Test fraction with zero denominator
        assertEquals("1 / 0", format.format(new Fraction(1, 0)));

        // Test fraction with negative numerator and zero denominator
        assertEquals("-1 / 0", format.format(new Fraction(-1, 0)));
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        NumberFormat wholeFormat = format.getWholeFormat();

        // Check if NumberFormat is not null
        assertNotNull(wholeFormat);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_NullArgument() {
        ProperFractionFormat format = new ProperFractionFormat();

        format.setWholeFormat(null);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test valid input
        Fraction fraction = format.parse("1 1 / 2");
        assertEquals(new Fraction(3, 2), fraction);

        // Test valid input with negative whole number
        fraction = format.parse("-1 1 / 2");
        assertEquals(new Fraction(-3, 2), fraction);

        // Test valid input with zero numerator
        fraction = format.parse("0 / 2");
        assertEquals(new Fraction(0, 2), fraction);

        // Test valid input with zero denominator
        fraction = format.parse("1 / 0");
        assertEquals(new Fraction(1, 0), fraction);

        // Test invalid input with missing fraction
        fraction = format.parse("1");
        assertNull(fraction);

        // Test invalid input with negative numerator and zero denominator
        fraction = format.parse("-1 / 0");
        assertNull(fraction);
    }

    @Test
    public void testParse_PositiveParsePosition() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);

        // Test valid input
        Fraction fraction = format.parse("1 1 / 2", pos);
        assertEquals(11, pos.getIndex());
        assertEquals(new Fraction(3, 2), fraction);

        // Reset parse position
        pos.setIndex(0);

        // Test invalid input
        fraction = format.parse("1a 1 / 2", pos);
        assertEquals(0, pos.getIndex());
        assertNull(fraction);
    }
}