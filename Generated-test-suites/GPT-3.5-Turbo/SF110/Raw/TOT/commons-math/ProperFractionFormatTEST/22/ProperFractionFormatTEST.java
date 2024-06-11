
import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat_WithWholeNumber_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(5, 2);

        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        String expected = "2 1 / 2";

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormat_WithZeroWholeNumber_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(1, 2);

        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        String expected = "1 / 2";

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormat_WithNegativeFraction_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(-5, 2);

        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        String expected = "-2 1 / 2";

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormat_WithZeroFraction_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(0, 2);

        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        String expected = "0";

        assertEquals(expected, result.toString());
    }

    @Test
    public void testParse_WithValidSource_ReturnsParsedFraction() {
        String source = "2 1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);
        Fraction expected = new Fraction(5, 2);

        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_WithValidFractionSource_ReturnsParsedFraction() {
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);
        Fraction expected = new Fraction(1, 2);

        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_WithValidZeroFractionSource_ReturnsParsedFraction() {
        String source = "0";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);
        Fraction expected = new Fraction(0);

        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_WithInvalidSource_ReturnsNull() {
        String source = "2 1 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(6, pos.getErrorIndex());
    }

    @Test
    public void testParse_WithInvalidFractionSource_ReturnsNull() {
        String source = "1 / a";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }

    @Test
    public void testParse_WithInvalidNegativeFractionSource_ReturnsNull() {
        String source = "1 / -2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(5, pos.getErrorIndex());
    }

    @Test
    public void testParse_WithInvalidZeroFractionSource_ReturnsNull() {
        String source = "/ 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void testSetWholeFormat_WithValidFormat_SetsWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        wholeFormat.setMaximumFractionDigits(0);

        format.setWholeFormat(wholeFormat);

        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_WithNullFormat_ThrowsNullArgumentException() {
        format.setWholeFormat(null);
    }

}

