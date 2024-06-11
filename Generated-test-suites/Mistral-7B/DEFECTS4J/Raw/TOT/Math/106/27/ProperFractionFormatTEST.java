package org.apache.commons.math.fraction.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.NumberFormat;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    // Test case 1: Test default constructor
    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction f = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        String result = pff.format(f, sb, fp).toString();
        assertEquals("1 / 2", result);
    }

    // Test case 2: Test custom constructor with default NumberFormat
    @Test
    public void testCustomConstructorWithDefaultNumberFormat() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        ProperFractionFormat pff = new ProperFractionFormat(nf);
        Fraction f = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        String result = pff.format(f, sb, fp).toString();
        assertEquals("1 / 2", result);
    }

    // Test case 3: Test custom constructor with custom NumberFormat
    @Test
    public void testCustomConstructorWithCustomNumberFormat() {
        // Set up custom NumberFormat for testing
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        wholeFormat.setMinimumIntegerDigits(1);
        numeratorFormat.setMinimumFractionDigits(1);
        denominatorFormat.setMinimumFractionDigits(1);

        ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction f = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        String result = pff.format(f, sb, fp).toString();
        assertEquals("-3.0 / 4.0", result);
    }

    // Test case 4: Test format method with negative whole number
    @Test
    public void testFormatWithNegativeWholeNumber() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction f = new Fraction(-2, 1);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        String result = pff.format(f, sb, fp).toString();
        assertEquals("-2 / 1", result);
    }

    // Test case 5: Test format method with fraction having zero denominator
    @Test
    public void testFormatWithFractionZeroDenominator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction f = new Fraction(1, 0);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        String result = pff.format(f, sb, fp).toString();
        assertNull(result);
    }

    // Test case 6: Test parse method with valid input string
    @Test
    public void testParseWithValidInputString() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction f = pff.parse(source, pos);
        assertEquals(new Fraction(3, 2), f);
    }

    // Test case 7: Test parse method with improper fraction
    @Test
    public void testParseWithImproperFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "5 2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction f = pff.parse(source, pos);
        assertEquals(new Fraction(15, 3), f);
    }

    // Test case 8: Test parse method with negative whole number
    @Test
    public void testParseWithNegativeWholeNumber() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "-3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction f = pff.parse(source, pos);
        assertEquals(new Fraction(-7, 2), f);
    }

    // Test case 9: Test parse method with fraction having zero denominator
    @Test
    public void testParseWithFractionZeroDenominator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "0 1/0";
        ParsePosition pos = new ParsePosition(0);
        Fraction f = pff.parse(source, pos);
        assertNull(f);
    }

    // Test case 10: Test parse method with fraction having negative numerator
    @Test
    public void testParseWithFractionNegativeNumerator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "-1 -1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction f = pff.parse(source, pos);
        assertEquals(new Fraction(-2, 1), f);
    }
}