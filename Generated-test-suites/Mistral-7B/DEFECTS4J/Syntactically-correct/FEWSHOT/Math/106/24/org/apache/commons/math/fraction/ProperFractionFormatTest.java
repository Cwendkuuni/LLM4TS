package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.commons.math.fraction.Fraction;
import org.apache.commons.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.commons.math.util.MathUtils;

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

        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = format.parse("1 / 2", pp);
        assertNotNull(parsedFraction);
        assertEquals(fraction, parsedFraction);
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction1 = new Fraction(1, 2);
        StringBuffer result1 = new StringBuffer();
        FieldPosition pos1 = new FieldPosition(0, 0);

        format.format(fraction1, result1, pos1);
        assertEquals("1 / 2", result1.toString());

        ParsePosition pp1 = new ParsePosition(0);
        Fraction parsedFraction1 = format.parse("1 / 2", pp1);
        assertNotNull(parsedFraction1);
        assertEquals(fraction1, parsedFraction1);

        // Test with BigDecimal and BigInteger
        BigDecimal wholeDec = new BigDecimal("3");
        BigDecimal numeratorDec = new BigDecimal("4");
        BigDecimal denominatorDec = new BigDecimal("5");
        Fraction fraction2 = new Fraction(wholeDec, numeratorDec, denominatorDec);

        StringBuffer result2 = new StringBuffer();
        FieldPosition pos2 = new FieldPosition(0, 0);

        format.format(fraction2, result2, pos2);
        assertEquals("3 / 4 / 5", result2.toString());

        ParsePosition pp2 = new ParsePosition(0);
        Fraction parsedFraction2 = format.parse("3 / 4 / 5", pp2);
        assertNotNull(parsedFraction2);

        assertEquals(fraction2, parsedFraction2);
    }

    @Test
    public void testParseWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        // Test valid whole number with fraction
        String input = "2 1/3";
        Fraction parsedFraction = format.parse(input, pos);
        assertNotNull(parsedFraction);
        assertEquals(new Fraction(2, 1, 3), parsedFraction);

        assertEquals(input.length(), pos.getIndex());
        assertEquals(input.length(), pos.getErrorIndex());

        // Test invalid whole number with fraction
        input = "2 -1/2";
        parsedFraction = format.parse(input, pos);
        assertNull(parsedFraction);
        assertEquals(2, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());

        // Test valid whole number only
        input = "2";
        parsedFraction = format.parse(input, pos);
        assertNotNull(parsedFraction);
        assertEquals(new Fraction(2), parsedFraction);

        assertEquals(input.length(), pos.getIndex());
        assertEquals(input.length(), pos.getErrorIndex());
    }

    @Test
    public void testParseWithFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        // Test valid fraction
        String input = "1 2/3";
        Fraction parsedFraction = format.parse(input, pos);
        assertNotNull(parsedFraction);
        assertEquals(new Fraction(1, 2, 3), parsedFraction);

        assertEquals(input.length(), pos.getIndex());
        assertEquals(input.length(), pos.getErrorIndex());

        // Test fraction with leading minus sign
        input = "-1 2/3";
        parsedFraction = format.parse(input, pos);
        assertNotNull(parsedFraction);
        assertEquals(new Fraction(-1, 2, 3), parsedFraction);

        assertEquals(input.length(), pos.getIndex());
        assertEquals(input.length(), pos.getErrorIndex());

        // Test fraction with trailing minus sign
        input = "1 2/3-";
        parsedFraction = format.parse(input, pos);
        assertNull(parsedFraction);
        assertEquals(input.length(), pos.getIndex());
        assertEquals(input.length(), pos.getErrorIndex());

        // Test fraction with double minus sign
        input = "-1 -2/3";
        parsedFraction = format.parse(input, pos);
        assertNull(parsedFraction);
        assertEquals(3, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }
}