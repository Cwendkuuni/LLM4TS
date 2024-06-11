package org.apache.commons.math.fraction.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.NumberFormat;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;

import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    @org.junit.Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);

        String formatted = new StringBuffer().append(format.format(fraction, new StringBuffer(), null)).toString();
        assertEquals("-3 / 2", formatted);

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, parsePosition);
        assertEquals(fraction, parsed);
    }

    @org.junit.Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(3, 2);

        String formatted = new StringBuffer().append(format.format(fraction, new StringBuffer(), null)).toString();
        assertEquals("-3 / 2", formatted);

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, parsePosition);
        assertEquals(fraction, parsed);
    }

    @org.junit.Test
    public void testProperFractionFormatMultiDigitWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(12, 3);

        String formatted = new StringBuffer().append(format.format(fraction, new StringBuffer(), null)).toString();
        assertEquals("-12 / 3", formatted);

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, parsePosition);
        assertEquals(fraction, parsed);
    }

    @org.junit.Test
    public void testProperFractionFormatNegativeWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);

        String formatted = new StringBuffer().append(format.format(fraction, new StringBuffer(), null)).toString();
        assertEquals("-3 / 2", formatted);

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, parsePosition);
        assertEquals(fraction, parsed);
    }

    @org.junit.Test
    public void testProperFractionFormatZeroNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 1);

        assertNull(format.format(fraction, new StringBuffer(), null));
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsed = format.parse("0", parsePosition);
        assertEquals(fraction, parsed);
    }

    @org.junit.Test
    public void testProperFractionFormatZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 0);

        assertNull(format.format(fraction, new StringBuffer(), null));
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            format.parse("0/0", parsePosition);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testProperFractionFormatBigDecimal() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(new BigDecimal(-3), new BigDecimal(2));

        String formatted = new StringBuffer().append(format.format(fraction, new StringBuffer(), null)).toString();
        assertEquals("-3 / 2", formatted);

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, parsePosition);
        assertEquals(fraction, parsed);
    }

    @org.junit.Test
    public void testProperFractionFormatBigInteger() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(new BigInteger("-3"), new BigInteger("2"));

        String formatted = new StringBuffer().append(format.format(fraction, new StringBuffer(), null)).toString();
        assertEquals("-3 / 2", formatted);

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, parsePosition);
        assertEquals(fraction, parsed);
    }

    @org.junit.Test
    public void testProperFractionFormatParseInvalid() {
        ProperFractionFormat format = new ProperFractionFormat();

        // invalid fraction: missing ' ' between whole number and fraction
        String input = "-31/2";
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            format.parse(input, parsePosition);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // invalid fraction: missing '/' between numerator and denominator
        input = "-3 2";
        parsePosition = new ParsePosition(0);
        try {
            format.parse(input, parsePosition);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // invalid fraction: non-integer whole number
        input = "-3.1 2";
        parsePosition = new ParsePosition(0);
        try {
            format.parse(input, parsePosition);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // invalid fraction: negative numerator or denominator
        input = "-3 -2";
        parsePosition = new ParsePosition(0);
        try {
            format.parse(input, parsePosition);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}