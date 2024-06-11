package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.util.MathUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 3);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);

        format.format(fraction, sb, fp);
        assertEquals("4 3/3", sb.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = format.parse("4 3/3", pos);
        assertEquals(fraction, parsedFraction);
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.US);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(new BigInteger("1234567890"), new BigInteger("987654321"));
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);

        format.format(fraction, sb, fp);
        String expected = "1,234,567,890.00 / 9,876,543,210.00";
        assertEquals(expected, sb.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = format.parse(expected, pos);
        assertEquals(fraction, parsedFraction);
    }

    @Test
    public void testParseNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-1 2/3";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = format.parse(source, pos);
        assertEquals(new Fraction(-1, 2), parsedFraction);
    }

    @Test(expected = ParseException.class)
    public void testParseNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-1 -1/2";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 5/3";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = format.parse(source, pos);
        assertNull(parsedFraction);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidWhitespace() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 2/3 tab";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidCharacter() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 2/3 %";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseMixedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 2 3/4";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = format.parse(source, pos);
        assertNull(parsedFraction);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 2/";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 2/0";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDenominatorSign() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 2/-";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test
    public void testParseLargeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "12345678901234567890 12345678901234567890 / 98765432109876543210";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = format.parse(source, pos);
        BigInteger expectedNumerator = new BigInteger("123456789012345678901234567890");
        BigInteger expectedDenominator = new BigInteger("98765432109876543210");
        assertEquals(new Fraction(expectedNumerator, expectedDenominator), parsedFraction);
    }

    @Test
    public void testFormatLargeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(new BigInteger("123456789012345678901234567890"), new BigInteger("98765432109876543210"));
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);

        format.format(fraction, sb, fp);
        String expected = "123456789012345678901234567890.00 / 98765432109876543210.00";
        assertEquals(expected, sb.toString());
    }
}