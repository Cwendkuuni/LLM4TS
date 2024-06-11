package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.apache.commons.math3.fraction.Fraction.ONE;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, 2);
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 / 2", formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testCustomFormat() {
        Locale locale = new Locale("en", "US");

        NumberFormat wholeFormat = NumberFormat.getNumberInstance(locale);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(locale);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(locale);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(BigInteger.valueOf(12345), BigInteger.valueOf(7));
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals("1,2345 / 7", formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testFormatWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();

        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMinimumFractionDigits(1);
        wholeFormat.setMaximumFractionDigits(1);

        ProperFractionFormat newFormat = new ProperFractionFormat(wholeFormat, getDefaultNumberFormat(), getDefaultNumberFormat());

        Fraction fraction = new Fraction(1, 2);
        String formatted = newFormat.format(fraction, new StringBuffer(), null);
        assertEquals("-1.0 / 2", formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = newFormat.parse(formatted, pos);
        assertEquals(fraction, parsed);
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        String source = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        format.parse(source, pos);
    }

    private NumberFormat getDefaultNumberFormat() {
        Locale locale = Locale.getDefault();
        return NumberFormat.getNumberInstance(locale);
    }
}