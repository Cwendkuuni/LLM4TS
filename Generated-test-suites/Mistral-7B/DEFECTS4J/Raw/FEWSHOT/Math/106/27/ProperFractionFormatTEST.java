package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String input = "1 2/3";
        Fraction expected = new Fraction(1, 2, 3);
        String output = pff.format(expected, new StringBuffer(), null);
        assertEquals(input, output);

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = pff.parse(input, pos);
        assertEquals(expected, parsed);
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);

        ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        String input = "1.5 -2 3/4";
        Fraction expected = new Fraction(-7, 4, 3);
        String output = pff.format(expected, new StringBuffer(), null);
        assertEquals(input, output);

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = pff.parse(input, pos);
        assertEquals(expected, parsed);
    }

    @Test
    public void testProperFractionFormatWhole() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, getDefaultNumberFormat(), getDefaultNumberFormat());

        String input = "-3";
        int expectedWhole = -3;
        Fraction expectedFraction = null;
        String output = pff.format(new Fraction(expectedWhole), new StringBuffer(), null);
        assertEquals(input, output.substring(0, input.length() - 1));

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = pff.parse(input, pos);
        if (parsed != null) {
            expectedFraction = parsed;
        }
        assertEquals(expectedWhole, parsed.getNumerator());
        assertEquals(1, parsed.getDenominator());
        assertEquals(expectedFraction, parsed);
    }

    @Test
    public void testProperFractionFormatNumerator() {
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat pff = new ProperFractionFormat(getDefaultNumberFormat(), numeratorFormat, getDefaultNumberFormat());

        String input = "2 1/2";
        int expectedNumerator = 2;
        int expectedDenominator = 1;
        Fraction expectedFraction = new Fraction(expectedNumerator, expectedDenominator);
        String output = pff.format(expectedFraction, new StringBuffer(), null);
        assertEquals(input, output);

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = pff.parse(input, pos);
        assertEquals(expectedFraction, parsed);
    }

    @Test
    public void testProperFractionFormatDenominator() {
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
        ProperFractionFormat pff = new ProperFractionFormat(getDefaultNumberFormat(), getDefaultNumberFormat(), denominatorFormat);

        String input = "1 2/3";
        int expectedNumerator = 1;
        int expectedDenominator = 3;
        Fraction expectedFraction = new Fraction(expectedNumerator, expectedDenominator);
        String output = pff.format(expectedFraction, new StringBuffer(), null);
        assertEquals(input, output);

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = pff.parse(input, pos);
        assertEquals(expectedFraction, parsed);

        input = "1 -2/3";
        expectedDenominator = -3;
        expectedFraction = new Fraction(1, expectedDenominator);
        output = pff.format(expectedFraction, new StringBuffer(), null);
        assertEquals(input, output);

        parsed = pff.parse(input, pos);
        assertEquals(expectedFraction, parsed);
    }

    @Test
    public void testProperFractionFormatEmptyInput() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String input = "";
        String output = pff.format(new Fraction(0), new StringBuffer(), null);
        assertEquals(input, output);

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = pff.parse(input, pos);
        assertNull(parsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProperFractionFormatNullFormat() {
        ProperFractionFormat pff = new ProperFractionFormat(null, getDefaultNumberFormat(), getDefaultNumberFormat());
    }

    private NumberFormat getDefaultNumberFormat() {
        return NumberFormat.getNumberInstance(Locale.ENGLISH);
    }
}