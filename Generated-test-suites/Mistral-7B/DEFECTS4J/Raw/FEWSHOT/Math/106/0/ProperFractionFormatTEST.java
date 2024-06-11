package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionFormat;
import org.apache.commons.math.util.MathUtils;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatWithDefaults() {
        FractionFormat properFractionFormat = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer(100);
        // Test format method
        String expected = "1 / 2";
        String formatted = properFractionFormat.format(fraction, toAppendTo, null);
        assertEquals(expected, formatted.toString());

        // Test parse method
        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse(expected, pos);
        assertEquals(fraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(fraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(pos.getIndex(), expected.length());
    }

    @Test
    public void testProperFractionFormatWithCustomFormats() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        FractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer(100);
        // Test format method
        String expected = "3 / 4";
        String formatted = properFractionFormat.format(fraction, toAppendTo, null);
        assertEquals(expected, formatted.toString());

        // Test parse method
        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse(expected, pos);
        assertEquals(fraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(fraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(pos.getIndex(), expected.length());
    }

    @Test
    public void testParseNegativeWholeNumber() {
        FractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "-3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse(source, pos);
        assertNotNull(parsedFraction);
        assertEquals(-7, parsedFraction.getNumerator());
        assertEquals(2, parsedFraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() {
        FractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        properFractionFormat.parse(source, pos);
    }
}