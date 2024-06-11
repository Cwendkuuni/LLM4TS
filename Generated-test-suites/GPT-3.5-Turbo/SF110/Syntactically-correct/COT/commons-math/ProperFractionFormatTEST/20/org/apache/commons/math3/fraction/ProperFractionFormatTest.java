import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testProperFractionFormatWithFormat() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(format);
        assertNotNull(fractionFormat);
    }

    @Test
    public void testProperFractionFormatWithThreeFormats() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(fractionFormat);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertNotNull(result);
        assertEquals("3 / 4", result.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / 4";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertNotNull(result);
        assertEquals("-3 / 4", result.toString());
    }

    @Test
    public void testFormatWithZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertNotNull(result);
        assertEquals("0", result.toString());
    }

    @Test
    public void testParseWithEmptyString() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertNull(fraction);
        assertEquals(0, position.getIndex());
    }

    @Test
    public void testParseWithMissingDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / ";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertNull(fraction);
        assertEquals(0, position.getErrorIndex());
    }

    @Test
    public void testParseWithInvalidFormattingString() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertNull(fraction);
        assertEquals(1, position.getIndex());
    }

    @Test
    public void testParseWithWhitespaceCharacters() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "       3 / 4     ";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }
}