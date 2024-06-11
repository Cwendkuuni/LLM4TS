
package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private NumberFormat wholeFormat;
    private NumberFormat numeratorFormat;
    private NumberFormat denominatorFormat;
    private ProperFractionFormat format;

    @Before
    public void setUp() {
        wholeFormat = NumberFormat.getInstance();
        numeratorFormat = NumberFormat.getInstance();
        denominatorFormat = NumberFormat.getInstance();
        format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
    }

    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("3 / 4", sb.toString());
    }

    @Test
    public void testFormatWithoutWholeNumber() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("1 / 2", sb.toString());
    }

    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-1, 2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("-1 / 2", sb.toString());
    }

    @Test
    public void testParseWithValidFormat() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 / 4", pos);
        assertEquals(new Fraction(3, 4), fraction);
        assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParseWithValidFormatAndWhitespace() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("    3 / 4   ", pos);
        assertEquals(new Fraction(3, 4), fraction);
        assertEquals(13, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidFormat() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 / 4 /", pos);
        assertNull(fraction);
        assertEquals(6, pos.getErrorIndex());
    }

    @Test
    public void testParseWithNegativeWholeNumber() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("-3 / 4", pos);
        assertEquals(new Fraction(-3, 4), fraction);
        assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParseWithNegativeFraction() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 / -4", pos);
        assertEquals(new Fraction(-3, 4), fraction);
        assertEquals(7, pos.getIndex());
    }
}
