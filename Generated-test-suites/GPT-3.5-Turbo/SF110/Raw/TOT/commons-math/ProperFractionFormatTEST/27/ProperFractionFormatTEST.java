
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 7);
        StringBuffer buffer = new StringBuffer();
        format.format(fraction, buffer, null);
        assertEquals("3 / 7", buffer.toString());
    }

    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(9, 4);
        StringBuffer buffer = new StringBuffer();
        format.format(fraction, buffer, null);
        assertEquals("2 1 / 4", buffer.toString());
    }

    @Test
    public void testFormatWithNegativeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 7);
        StringBuffer buffer = new StringBuffer();
        format.format(fraction, buffer, null);
        assertEquals("-3 / 7", buffer.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNull(wholeFormat);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("5 / 9", pos);
        assertEquals(5, fraction.getNumerator());
        assertEquals(9, fraction.getDenominator());
        assertEquals(6, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 -/ 4", pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());
    }

    @Test
    public void testParseWithInvalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("2 / -5", pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }
}

