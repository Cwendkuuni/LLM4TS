package org.apache.commons.math.fraction.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FormatUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.junit.Test;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProperFractionFormatTest {

    private ProperFractionFormat pff;

    @org.junit.Before
    public void setUp() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
    }

    @Test
    public void testProperFractionFormat_default() {
        Fraction fraction = new Fraction(1, 2);
        String result = pff.format(fraction, new StringBuffer(), null);
        String expected = "1/2";
        assertEquals(expected, result);
    }

    @Test
    public void testProperFractionFormat_custom() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
        pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(1, 2);
        String result = pff.format(fraction, new StringBuffer(), null);
        String expected = "100%/50%";
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_integer() {
        int integer = 123;
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        pff = new ProperFractionFormat(wholeFormat, null, null);
        String result = pff.format(new Fraction(integer), new StringBuffer(), null);
        String expected = Integer.toString(integer);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_negativeInteger() {
        int integer = -123;
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        pff = new ProperFractionFormat(wholeFormat, null, null);
        String result = pff.format(new Fraction(integer), new StringBuffer(), null);
        String expected = "-" + Integer.toString(Math.abs(integer));
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_properFraction() {
        Fraction fraction = new Fraction(1, 3);
        String result = pff.format(fraction, new StringBuffer(), null);
        String expected = "1/3";
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_improperFraction() {
        Fraction fraction = new Fraction(3, 2);
        String result = pff.format(fraction, new StringBuffer(), null);
        String expected = "1 1/2";
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidWholeNumber() {
        String input = "apples 1/2";
        ParsePosition pos = new ParsePosition(0);
        pff.parse(input, pos);
        fail("Expected ParseException for input: " + input);
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidNumerator() {
        String input = "1 apples/2";
        ParsePosition pos = new ParsePosition(0);
        pff.parse(input, pos);
        fail("Expected ParseException for input: " + input);
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidDenominator() {
        String input = "1/apples 2";
        ParsePosition pos = new ParsePosition(0);
        pff.parse(input, pos);
        fail("Expected ParseException for input: " + input);
    }

    @Test
    public void testParse_properFraction() {
        String input = "1/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(input, pos);
        Fraction expected = new Fraction(1, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_improperFraction() {
        String input = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(input, pos);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_negativeProperFraction() {
        String input = "-1/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(input, pos);
        Fraction expected = new Fraction(-1, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_negativeImproperFraction() {
        String input = "-1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(input, pos);
        Fraction expected = new Fraction(-3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_largeFraction() {
        String input = "123456789012345678901234567890/987654321098765432109876543210";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(input, pos);
        BigInteger expectedNumerator = new BigInteger("123456789012345678901234567890");
        BigInteger expectedDenominator = new BigInteger("987654321098765432109876543210");
        Fraction expected = new Fraction(expectedNumerator, expectedDenominator);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_largeNegativeFraction() {
        String input = "-123456789012345678901234567890/987654321098765432109876543210";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(input, pos);
        BigInteger expectedNumerator = new BigInteger("-123456789012345678901234567890");
        BigInteger expectedDenominator = new BigInteger("987654321098765432109876543210");
        Fraction expected = new Fraction(expectedNumerator, expectedDenominator);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_similarFractions_sameValue() {
        String input1 = "1/2";
        ParsePosition pos1 = new ParsePosition(0);
        Fraction fraction1 = pff.parse(input1, pos1);
        String input2 = "2/4";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction fraction2 = pff.parse(input2, pos2);
        JaroWinklerDistance jwd = new JaroWinklerDistance();
        double similarity = jwd.distance(FormatUtils.toString(fraction1), FormatUtils.toString(fraction2));
        assertTrue("Expected similarity > 0.9, actual: " + similarity, similarity > 0.9);
    }

    @Test
    public void testParse_similarFractions_differentForm() {
        String input1 = "1/3";
        ParsePosition pos1 = new ParsePosition(0);
        Fraction fraction1 = pff.parse(input1, pos1);
        String input2 = "3/6";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction fraction2 = pff.parse(input2, pos2);
        JaroWinklerDistance jwd = new JaroWinklerDistance();
        double similarity = jwd.distance(FormatUtils.toString(fraction1), FormatUtils.toString(fraction2));
        assertTrue("Expected similarity > 0.9, actual: " + similarity, similarity > 0.9);
    }

    @Test
    public void testParse_mixedFractions() {
        String input = "1 1/2 + 1/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(input, pos);
        Fraction expected = new Fraction(new BigInteger("5"), new BigInteger("2"));
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParse_mixedFraction_invalidDenominator() {
        String input = "1 1/apples";
        ParsePosition pos = new ParsePosition(0);
        pff.parse(input, pos);
        fail("Expected ParseException for input: " + input);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        pff.setWholeFormat(wholeFormat);
        NumberFormat newWholeFormat = pff.getWholeFormat();
        assertEquals(wholeFormat, newWholeFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat_null() {
        NumberFormat wholeFormat = null;
        pff.setWholeFormat(wholeFormat);
        fail("Expected IllegalArgumentException for null format");
    }
}