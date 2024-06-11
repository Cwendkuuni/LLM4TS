import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    @Before
    public void setup() {
        format = new ProperFractionFormat(NumberFormat.getInstance(Locale.US));
    }

    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("1 1/2", sb.toString());
    }

    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-5, 3);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("-1 2/3", sb.toString());
    }

    @Test
    public void testFormatWithZeroDenominator() {
        Fraction fraction = new Fraction(5, 0);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("Infinity", sb.toString());
    }

    @Test
    public void testFormatWithNullFraction() {
        StringBuffer sb = new StringBuffer();
        format.format(null, sb, null);
        assertEquals("null", sb.toString());
    }

    @Test
    public void testParseWithValidInput() {
        String input = "1 5/6";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
        assertNotNull(fraction);
        assertEquals(11, fraction.getNumerator());
        assertEquals(6, fraction.getDenominator());
    }

    @Test
    public void testParseWithInvalidInput() {
        String input = "1 5/abc";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
        assertNull(fraction);
    }

    @Test
    public void testParseWithEmptyString() {
        String input = "";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test(expected = NullPointerException.class)
    public void testSetWholeFormatWithNull() {
        format.setWholeFormat(null);
    }
}