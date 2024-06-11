package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(5, 2);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("2 1/2", buffer.toString());
    }

    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-5, 2);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("-2 1/2", buffer.toString());
    }

    @Test
    public void testFormatWithWholeNumberOnly() {
        Fraction fraction = new Fraction(5);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("5", buffer.toString());
    }

    @Test
    public void testFormatWithZeroFraction() {
        Fraction fraction = new Fraction(0, 5);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("0", buffer.toString());
    }

    @Test
    public void testParse() {
        String source = "2 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(2, fraction.getWhole());
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        assertNull(pos.getErrorIndex());
    }

    @Test
    public void testParseWithNegativeWholeNumber() {
        String source = "-2 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(-2, fraction.getWhole());
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        assertNull(pos.getErrorIndex());
    }

    @Test
    public void testParseWithImproperFraction() {
        String source = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(0, fraction.getWhole());
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        assertNull(pos.getErrorIndex());
    }

    @Test
    public void testParseWithInvalidFormat() {
        String source = "2&1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(1, pos.getErrorIndex());
    }

    @Test
    public void testParseWithInvalidWholeNumber() {
        String source = "a 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void testParseWithInvalidNumerator() {
        String source = "2 a/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(2, pos.getErrorIndex());
    }

    @Test
    public void testParseWithInvalidDenominator() {
        String source = "2 1/a";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }

}