import static org.junit.Assert.*;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer expected = new StringBuffer("1 1/2");
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testFormatWithNegative() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);
        StringBuffer expected = new StringBuffer("-1 1/2");
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 1/2";
        Fraction expected = new Fraction(3, 2);
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        assertEquals(expected, result);
    }

    @Test
    public void testParseWithInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 1/2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        assertNull(result);
    }

    @Test
    public void testParseWithNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-1 1/2";
        Fraction expected = new Fraction(-3, 2);
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        assertEquals(expected, result);
    }
}