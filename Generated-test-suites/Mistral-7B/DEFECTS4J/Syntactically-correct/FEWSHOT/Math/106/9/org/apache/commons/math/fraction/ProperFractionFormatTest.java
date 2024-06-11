package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, 2);
        String expected = "1 / 2";
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected, formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse("1 / 2", pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testCustomWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMinimumIntegerDigits(2);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);

        Fraction fraction = new Fraction(1, 2);
        String expected = "01 / 2";
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected, formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse("01 / 2", pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testCustomNumeratorFormat() {
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        numeratorFormat.setMinimumIntegerDigits(3);

        ProperFractionFormat format = new ProperFractionFormat(null, numeratorFormat, null);

        Fraction fraction = new Fraction(3, 2);
        String expected = "3   / 2";
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected, formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse("3   / 2", pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testCustomDenominatorFormat() {
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        denominatorFormat.setMaximumFractionDigits(2);

        ProperFractionFormat format = new ProperFractionFormat(null, null, denominatorFormat);

        Fraction fraction = new Fraction(1, 3);
        String expected = "1 / 3.00";
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected, formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse("1 / 3.00", pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testParseNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-3 1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsed = format.parse(source, pos);
        assertEquals(new Fraction(-7, 2), parsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 a/2";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test
    public void testFormatBigNumbers() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMaximumIntegerDigits(10);

        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        numeratorFormat.setMaximumFractionDigits(10);

        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        denominatorFormat.setMaximumFractionDigits(10);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        BigInteger num = BigInteger.valueOf(12345678901234567890L);
        BigInteger den = BigInteger.valueOf(98765432109876543210L);
        Fraction fraction = new Fraction(num, den);

        String expected = String.format("%1$15d %2$+1$15d / %3$17d",
                Precision.round(num.doubleValue(), 15, RoundingMode.HALF_UP),
                Precision.round(num.doubleValue() % den.doubleValue(), 15, RoundingMode.HALF_UP),
                Precision.round(den.doubleValue(), 17, RoundingMode.HALF_UP));
        String formatted = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals(expected, formatted);

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(expected, pos);
        assertEquals(fraction, parsed);
    }

}