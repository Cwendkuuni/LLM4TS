package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, result, pos);
        assertEquals("1 / 2", result.toString());

        parseAndCheck("1 / 2", format, new Fraction(1, 2));

        result.setLength(0);
        fraction = new Fraction(-1, 2);
        format.format(fraction, result, pos);
        assertEquals("-1 / 2", result.toString());
        parseAndCheck("-1 / 2", format, new Fraction(-1, 2));

        result.setLength(0);
        fraction = new Fraction(0, 2);
        format.format(fraction, result, pos);
        assertEquals("0 / 1", result.toString());
        parseAndCheck("0 / 1", format, new Fraction(0, 1));
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMinimumFractionDigits(1);
        wholeFormat.setMaximumFractionDigits(3);

        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        numeratorFormat.setMinimumFractionDigits(1);
        numeratorFormat.setMaximumFractionDigits(3);
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        denominatorFormat.setMinimumFractionDigits(1);
        denominatorFormat.setMaximumFractionDigits(3);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(BigDecimal.valueOf(1.123), BigDecimal.valueOf(2.345));
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, result, pos);
        assertEquals("1.123 / 2.345", result.toString());
        parseAndCheck("1.123 / 2.345", format, fraction);

        result.setLength(0);
        fraction = new Fraction(-BigDecimal.valueOf(1.123), BigDecimal.valueOf(2.345));
        format.format(fraction, result, pos);
        assertEquals("-1.123 / 2.345", result.toString());
        parseAndCheck("-1.123 / 2.345", format, fraction);

        result.setLength(0);
        fraction = new Fraction(BigDecimal.ZERO, BigDecimal.valueOf(2.345));
        format.format(fraction, result, pos);
        assertEquals("0 / 2.345", result.toString());
        parseAndCheck("0 / 2.345", format, fraction);

        result.setLength(0);
        fraction = new Fraction(BigDecimal.valueOf(1.123), BigDecimal.valueOf(0));
        format.format(fraction, result, pos);
        assertEquals("1.123 / 1", result.toString());
        parseAndCheck("1.123 / 1", format, fraction);

        result.setLength(0);
        fraction = new Fraction(BigDecimal.valueOf(1.123), BigDecimal.valueOf(-2.345));
        format.format(fraction, result, pos);
        assertEquals("1.123 / -2.345", result.toString());
        parseAndCheck("1.123 / -2.345", format, fraction);

        result.setLength(0);
        fraction = new Fraction(BigDecimal.valueOf(-1.123), BigDecimal.valueOf(-2.345));
        format.format(fraction, result, pos);
        assertEquals("-1.123 / -2.345", result.toString());
        parseAndCheck("-1.123 / -2.345", format, fraction);
    }

    @Test
    public void testProperFractionFormatNegativeWhole() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(-1, 2);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, result, pos);
        assertEquals("-1 / 2", result.toString());
        parseAndCheck("-1 / 2", format, fraction);

        result.setLength(0);
        fraction = new Fraction(-2, 2);
        format.format(fraction, result, pos);
        assertEquals("-2 / 2", result.toString());
        parseAndCheck("-2 / 2", format, fraction);
    }

    @Test
    public void testProperFractionFormatImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(5, 2);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, result, pos);
        assertEquals("2 1/2", result.toString());
        parseAndCheck("2 1/2", format, fraction);

        result.setLength(0);
        fraction = new Fraction(-5, 2);
        format.format(fraction, result, pos);
        assertEquals("-2 1/2", result.toString());
        parseAndCheck("-2 1/2", format, fraction);
    }

    @Test
    public void testProperFractionFormatInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();

        String source = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse(source, pos);
        assertNull(fraction);
        assertEquals(source.length(), pos.getErrorIndex());

        source = "3 -1/2";
        fraction = format.parse(source, pos);
        assertNull(fraction);
        assertEquals(source.length(), pos.getErrorIndex());

        source = "3 / -2";
        fraction = format.parse(source, pos);
        assertNull(fraction);
        assertEquals(source.length(), pos.getErrorIndex());

        source = "3 / 0";
        fraction = format.parse(source, pos);
        assertNull(fraction);
        assertEquals(source.length(), pos.getErrorIndex());
    }

    private void parseAndCheck(String source, ProperFractionFormat format, Fraction expected) {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);

        assertEquals(expected.getNumerator(), fraction.getNumerator());
        assertEquals(expected.getDenominator(), fraction.getDenominator());
        assertEquals(expected.toString(), fraction.toString());
        assertEquals(source.length(), pos.getIndex());
    }

    private void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
        int index = pos.getIndex();
        while (Character.isWhitespace(source.charAt(index))) {
            index++;
            pos.setIndex(index);
        }
    }

    private char parseNextCharacter(String source, ParsePosition pos) {
        int index = pos.getIndex();
        char c = source.charAt(index);
        if (Character.isWhitespace(c)) {
            index++;
            pos.setIndex(index);
        }

        return c;
    }
}