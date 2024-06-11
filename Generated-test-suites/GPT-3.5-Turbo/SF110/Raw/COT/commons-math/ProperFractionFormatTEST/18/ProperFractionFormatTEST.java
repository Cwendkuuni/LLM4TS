

import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormatWithWholeFormat() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        properFractionFormat.format(fraction, buffer, pos);

        assertEquals("1 1/2", buffer.toString());
    }

    @Test
    public void testFormatWithoutWholeFormat() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        properFractionFormat.format(fraction, buffer, pos);

        assertEquals("1/2", buffer.toString());
    }

    @Test
    public void testParseWithValidFormat() {
        String source = "1 1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testParseWithInvalidFormat() {
        String source = "1 1/2/3";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
    }

    @Test
    public void testParseWithInvalidNumerator() {
        String source = "1 -1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
    }

    @Test
    public void testParseWithInvalidDenominator() {
        String source = "1 1/-2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
    }

    @Test
    public void testParseWithNoFraction() {
        String source = "1";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertEquals(1, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testParseWithEmptyString() {
        String source = "";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
    }

    @Test
    public void testSetWholeFormatWithNull() {
        NumberFormat format = null;

        try {
            properFractionFormat.setWholeFormat(format);
        } catch (NullPointerException e) {
            assertEquals(LocalizedFormats.WHOLE_FORMAT.toString(), e.getMessage());
        }
    }

}
