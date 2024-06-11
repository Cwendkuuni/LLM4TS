package org.apache.commons.math.fraction.test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        FractionFormat format = new ProperFractionFormat();

        // Test valid proper fractions
        Fraction fraction = format.parse("1 2/3", new ParsePosition(0, Locale.US));
        assertEquals("1 2/3", format.format(fraction, new StringBuffer(), null));
        fraction = format.parse("2 3/4", new ParsePosition(0, Locale.US));
        assertEquals("2 3/4", format.format(fraction, new StringBuffer(), null));
        fraction = format.parse("-1 1/2", new ParsePosition(0, Locale.US));
        assertEquals("-1 1/2", format.format(fraction, new StringBuffer(), null));

        // Test invalid improper fractions
        assertNull(format.parse("1 3/2", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("1/2 3", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("-1 3/2", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("-1/2 3", new ParsePosition(0, Locale.US)));

        // Test invalid fractions with leading minus sign
        assertNull(format.parse("-1 -1/2", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("-1/-2 -1", new ParsePosition(0, Locale.US)));

        // Test valid improper fractions with custom format
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();
        FractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fractionImp = customFormat.parse("3 2/3", new ParsePosition(0, Locale.US));
        assertEquals("3 2/3", customFormat.format(fractionImp, new StringBuffer(), null));
    }

    @Test
    public void testCustomFormat() {
        // Test valid proper fractions with custom format
        Locale locale = new Locale("en", "US");
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(locale);
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance(locale);
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance(locale);
        FractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = format.parse("1 2/3", new ParsePosition(0, Locale.US));
        assertEquals("1 2/3", format.format(fraction, new StringBuffer(), null));
        fraction = format.parse("2 3/4", new ParsePosition(0, Locale.US));
        assertEquals("2 3/4", format.format(fraction, new StringBuffer(), null));
        fraction = format.parse("-1 1/2", new ParsePosition(0, Locale.US));
        assertEquals("-1 1/2", format.format(fraction, new StringBuffer(), null));

        // Test invalid improper fractions with custom format
        assertNull(format.parse("1 3/2", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("1/2 3", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("-1 3/2", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("-1/2 3", new ParsePosition(0, Locale.US)));

        // Test invalid fractions with leading minus sign with custom format
        assertNull(format.parse("-1 -1/2", new ParsePosition(0, Locale.US)));
        assertNull(format.parse("-1/-2 -1", new ParsePosition(0, Locale.US)));

        // Test improper fractions with custom format
        Fraction fractionImp = format.parse("3 2/3", new ParsePosition(0, Locale.US));
        assertEquals("3 2/3", format.format(fractionImp, new StringBuffer(), null));

        // Test improper fractions with large integers
        FractionFormat largeFormat = new ProperFractionFormat(new DecimalFormat("#,##0"), new DecimalFormat("#,##0"), new DecimalFormat("#,##0"));
        Fraction fractionLarge = largeFormat.parse("1,000 1,234,567/8,912,345", new ParsePosition(0, Locale.US));
        assertEquals("1,000 1,234,567/8,912,345", largeFormat.format(fractionLarge, new StringBuffer(), null));
    }

    @Test
    public void testParseNegativeWholeNumber() {
        FractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("-1 1/2", new ParsePosition(0, Locale.US));
        assertEquals("-1 1/2", format.format(fraction, new StringBuffer(), null));
        assertEquals(-1, fraction.getNumerator() / fraction.getDenominator());
    }

    @Test
    public void testFormatNegativeWholeNumber() {
        FractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 2);
        assertEquals("-1 1/2", format.format(fraction, new StringBuffer(), null));
    }

    @Test(expected = ParseException.class)
    public void testInvalidFraction() {
        FractionFormat format = new ProperFractionFormat();
        format.parse("1 -1/2", new ParsePosition(0, Locale.US));
    }

    @Test(expected = ParseException.class)
    public void testInvalidFractionWithLeadingMinusSign() {
        FractionFormat format = new ProperFractionFormat();
        format.parse("-1 -1/2", new ParsePosition(0, Locale.US));
    }

    @Test(expected = ParseException.class)
    public void testInvalidImproperFraction() {
        FractionFormat format = new ProperFractionFormat();
        format.parse("1 3/2", new ParsePosition(0, Locale.US));
    }

    @Test(expected = ParseException.class)
    public void testInvalidImproperFractionWithLeadingMinusSign() {
        FractionFormat format = new ProperFractionFormat();
        format.parse("-1 3/2", new ParsePosition(0, Locale.US));
    }

    @Test(expected = ParseException.class)
    public void testInvalidFractionWithBigNumbers() {
        FractionFormat format = new ProperFractionFormat();
        Fraction bigFraction = new Fraction(new BigInteger("123456789012345678901234567890"), new BigDecimal("987654321098765432109876543210"));
        format.parse(format.format(bigFraction, new StringBuffer(), null), new ParsePosition(0, Locale.US));
    }

    @Test
    public void testFormatAndParseBigNumbers() {
        FractionFormat format = new ProperFractionFormat();
        Fraction bigFraction = new Fraction(new BigInteger("123456789012345678901234567890"), new BigDecimal("987654321098765432109876543210"));
        String formattedString = format.format(bigFraction, new StringBuffer(), null).toString();
        Fraction parsedFraction = format.parse(formattedString, new ParsePosition(0, Locale.US));
        assertEquals(bigFraction, parsedFraction);
    }

    // Test parse and format with various locales
    @Test
    public void testFormatAndParseWithLocales() {
        FractionFormat format = new ProperFractionFormat();

        // Test with US locale
        Fraction fractionUS = format.parse("1 2/3", new ParsePosition(0, Locale.US));
        String formattedStringUS = format.format(fractionUS, new StringBuffer(), null).toString();
        assertEquals("1 2/3", formattedStringUS);
        Fraction parsedFractionUS = format.parse(formattedStringUS, new ParsePosition(0, Locale.US));
        assertEquals(fractionUS, parsedFractionUS);

        // Test with other locales
        Fraction fractionDE = format.parse("1 2/3", new ParsePosition(0, new Locale("de", "DE")));
        String formattedStringDE = format.format(fractionDE, new StringBuffer(), null).toString();
        assertEquals("1 2/3", formattedStringDE);
        Fraction parsedFractionDE = format.parse(formattedStringDE, new ParsePosition(0, new Locale("de", "DE")));
        assertEquals(fractionDE, parsedFractionDE);

        Fraction fractionCN = format.parse("1 2/3", new ParsePosition(0, new Locale("zh", "CN")));
        String formattedStringCN = format.format(fractionCN, new StringBuffer(), null).toString();
        assertEquals("1 2/3", formattedStringCN);
        Fraction parsedFractionCN = format.parse(formattedStringCN, new ParsePosition(0, new Locale("zh", "CN")));
        assertEquals(fractionCN, parsedFractionCN);
    }
}