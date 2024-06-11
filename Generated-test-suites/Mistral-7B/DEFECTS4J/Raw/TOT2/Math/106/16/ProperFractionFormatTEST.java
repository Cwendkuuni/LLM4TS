import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, sb, pos);
        assertEquals("1 / 2", sb.toString());

        // Test with custom number formats
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction customFraction = new Fraction(3, 4);
        sb.setLength(0);
        pos.setBeginIndex(0);
        pos.setEndIndex(0);

        customFormat.format(customFraction, sb, pos);
        assertEquals("3 / 4", sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("-1 -1/2", null, pos);
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 1/2", null, pos);
        assertEquals(new Fraction(1, 2), fraction);
    }

    @Test
    public void testParseImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 1", null, pos);
        assertEquals(new Fraction(1, 1), fraction);
    }
}