import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat_WithFraction_ReturnsProperlyFormattedString() {
        Fraction fraction = new Fraction(3, 4);

        String expected = "3 / 4";
        String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_WithNegativeFraction_ReturnsProperlyFormattedString() {
        Fraction fraction = new Fraction(-5, 7);

        String expected = "-5 / 7";
        String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_WithWholeNumberAndFraction_ReturnsProperlyFormattedString() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer("1 ");

        String expected = "1 2 / 3";
        String actual = properFractionFormat.format(fraction, toAppendTo, null).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_WithZeroWholeNumberAndFraction_ReturnsProperlyFormattedString() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer("0 ");

        String expected = "1 / 2";
        String actual = properFractionFormat.format(fraction, toAppendTo, null).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithValidFractionString_ReturnsFractionObject() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);

        Fraction expected = new Fraction(3, 4);
        Fraction actual = properFractionFormat.parse(source, pos);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithValidNegativeFractionString_ReturnsFractionObject() {
        String source = "-5 / 7";
        ParsePosition pos = new ParsePosition(0);

        Fraction expected = new Fraction(-5, 7);
        Fraction actual = properFractionFormat.parse(source, pos);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithValidWholeNumberAndFractionString_ReturnsFractionObject() {
        String source = "1 2 / 3";
        ParsePosition pos = new ParsePosition(0);

        Fraction expected = new Fraction(5, 3);
        Fraction actual = properFractionFormat.parse(source, pos);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithValidZeroWholeNumberAndFractionString_ReturnsFractionObject() {
        String source = "0 1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction expected = new Fraction(1, 2);
        Fraction actual = properFractionFormat.parse(source, pos);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithInvalidFractionString_ReturnsNull() {
        String source = "3.4";
        ParsePosition pos = new ParsePosition(0);

        Fraction actual = properFractionFormat.parse(source, pos);

        assertNull(actual);
    }

    @Test
    public void testParse_WithInvalidNegativeFractionString_ReturnsNull() {
        String source = "-5 / -7";
        ParsePosition pos = new ParsePosition(0);

        Fraction actual = properFractionFormat.parse(source, pos);

        assertNull(actual);
    }

    @Test
    public void testParse_WithInvalidWholeNumberAndFractionString_ReturnsNull() {
        String source = "1.2 / 3";
        ParsePosition pos = new ParsePosition(0);

        Fraction actual = properFractionFormat.parse(source, pos);

        assertNull(actual);
    }

    @Test
    public void testParse_WithInvalidZeroWholeNumberAndFractionString_ReturnsNull() {
        String source = "0.1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction actual = properFractionFormat.parse(source, pos);

        assertNull(actual);
    }

    @Test
    public void testSetWholeFormat_WithValidNumberFormat_SetsWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();

        properFractionFormat.setWholeFormat(wholeFormat);

        assertSame(wholeFormat, properFractionFormat.getWholeFormat());
    }
}