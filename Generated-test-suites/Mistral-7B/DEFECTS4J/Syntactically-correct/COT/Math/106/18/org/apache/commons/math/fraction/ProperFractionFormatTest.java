package org.apache.commons.math.fraction.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.NumberFormat;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberTheoryUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {
    private ProperFractionFormat format;
    private NumberFormat wholeFormat;
    private NumberFormat numeratorFormat;
    private NumberFormat denominatorFormat;

    @BeforeClass
    public static void setupClass() {
        wholeFormat = NumberFormat.getNumberInstance();
        numeratorFormat = NumberFormat.getNumberInstance();
        denominatorFormat = NumberFormat.getNumberInstance();
    }

    @Before
    public void setup() {
        format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
    }

    /**
     * Test method for {@link ProperFractionFormat#ProperFractionFormat()}.
     */
    @Test
    public void testDefaultConstructor() {
        Fraction result = format.parse("1 2/3");
        assertEquals(new Fraction(1, 2, 3), result);
    }

    /**
     * Test method for {@link ProperFractionFormat#ProperFractionFormat(NumberFormat)}.
     */
    @Test
    public void testConstructorWithDefaultNumberFormat() {
        Fraction result = format.parse("1 2/3");
        assertEquals(new Fraction(1, 2, 3), result);
    }

    /**
     * Test method for {@link ProperFractionFormat#ProperFractionFormat(NumberFormat, NumberFormat, NumberFormat)}.
     */
    @Test
    public void testConstructorWithCustomNumberFormat() {
        NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
        NumberFormat customNumeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat customDenominatorFormat = NumberFormat.getNumberInstance();

        customWholeFormat.setMinimumFractionDigits(2);
        customNumeratorFormat.setMinimumFractionDigits(2);
        customDenominatorFormat.setMinimumFractionDigits(2);

        ProperFractionFormat customFormat = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);

        Fraction result = customFormat.parse("1 2/3");
        Fraction expected = new Fraction(1, 2, 3);
        assertEquals(expected, result);

        String formatted = customFormat.format(new Fraction(1, 2, 3));
        String expectedFormatted = customWholeFormat.format(1) + " " + customNumeratorFormat.format(new BigDecimal("2.0").setScale(2, BigDecimal.ROUND_HALF_UP)) + "/" + customDenominatorFormat.format(new BigDecimal("3.0").setScale(2, BigDecimal.ROUND_HALF_UP));
        assertEquals(expectedFormatted, formatted);
    }

    /**
     * Test method for {@link ProperFractionFormat#format(Fraction, StringBuffer, FieldPosition)}.
     */
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(5, 7);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        String expected = wholeFormat.format(5) + " " + numeratorFormat.format(new BigDecimal(5).abs()) + "/" + denominatorFormat.format(new BigDecimal(7).abs());
        assertEquals(expected, toAppendTo.toString());
    }

    /**
     * Test method for {@link ProperFractionFormat#parse(String, ParsePosition)}.
     */
    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse("1 2/3", pos);
        assertEquals(new Fraction(1, 2, 3), result);
        assertEquals(10, pos.getIndex());

        result = format.parse("1 2/3", pos);
        assertNull(result);
        assertEquals(13, pos.getIndex());

        result = format.parse("-1 -2/3", pos);
        assertNull(result);
        assertEquals(6, pos.getIndex());

        result = format.parse("1 2 3/4", pos);
        assertNull(result);
        assertEquals(7, pos.getIndex());

        result = format.parse("1/2", pos);
        assertNull(result);
        assertEquals(3, pos.getIndex());

        result = format.parse("0/", pos);
        assertNull(result);
        assertEquals(3, pos.getIndex());

        result = format.parse("0", pos);
        assertNull(result);
        assertEquals(1, pos.getIndex());

        result = format.parse("-1", pos);
        assertEquals(new Fraction(-1, 1), result);
        assertEquals(2, pos.getIndex());
    }

    /**
     * Test method for {@link ProperFractionFormat#getWholeFormat()}.
     */
    @Test
    public void testGetWholeFormat() {
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    /**
     * Test method for {@link ProperFractionFormat#setWholeFormat(NumberFormat)}.
     */
    @Test
    public void testSetWholeFormat() {
        NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
        format.setWholeFormat(customWholeFormat);
        assertEquals(customWholeFormat, format.getWholeFormat());
    }
}