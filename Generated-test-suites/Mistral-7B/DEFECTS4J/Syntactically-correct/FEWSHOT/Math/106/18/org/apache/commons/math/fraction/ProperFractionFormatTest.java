package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

    private static final NumberFormat DEFAULT_WHOLE_FORMAT = NumberFormat.getNumberInstance();
    private static final NumberFormat DEFAULT_NUMERATOR_FORMAT = NumberFormat.getNumberInstance();
    private static final NumberFormat DEFAULT_DENOMINATOR_FORMAT = NumberFormat.getNumberInstance();

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/2";
        Fraction expected = new Fraction(3, 2);

        validateFormat(format, input, expected);
    }

    @Test
    public void testProperFractionFormatCustom() {
        ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMERATOR_FORMAT, DEFAULT_DENOMINATOR_FORMAT);
        String input = "3 1/2";
        Fraction expected = new Fraction(3, 2);

        validateFormat(format, input, expected);
    }

    @Test
    public void testProperFractionFormatCustomWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getPercentInstance(), DEFAULT_NUMERATOR_FORMAT, DEFAULT_DENOMINATOR_FORMAT);
        String input = "30% 1/2";
        Fraction expected = new Fraction(3, 2);

        validateFormat(format, input, expected);
    }

    @Test
    public void testProperFractionFormatCustomNumeratorFormat() {
        ProperFractionFormat format = new ProperFractionFormat(DEFAULT_WHOLE_FORMAT, NumberFormat.getPercentInstance(), DEFAULT_DENOMINATOR_FORMAT);
        String input = "3 % 1/2";
        Fraction expected = new Fraction(3, 2);

        validateFormat(format, input, expected);
    }

    @Test
    public void testProperFractionFormatCustomDenominatorFormat() {
        ProperFractionFormat format = new ProperFractionFormat(DEFAULT_WHOLE_FORMAT, DEFAULT_NUMERATOR_FORMAT, NumberFormat.getPercentInstance());
        String input = "3 1/60%";
        Fraction expected = new Fraction(3, 60);

        validateFormat(format, input, expected);
    }

    @Test
    public void testProperFractionFormatNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 1/2";
        Fraction expected = new Fraction(-3, 2);

        validateFormat(format, input, expected);
    }

    @Test
    public void testProperFractionFormatInvalidInputWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 -1/2";

        Fraction result = format.parse(input, new ParsePosition(0));
        assertNull(result);
    }

    @Test
    public void testProperFractionFormatInvalidInputNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 -1/2";

        Fraction result = format.parse(input, new ParsePosition(0));
        assertNull(result);
    }

    @Test
    public void testProperFractionFormatInvalidInputDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1 -2";

        Fraction result = format.parse(input, new ParsePosition(0));
        assertNull(result);
    }

    @Test
    public void testProperFractionFormatInvalidInputMixed() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 -1/2 -3 1/2";

        Fraction result = format.parse(input, new ParsePosition(0));
        assertNull(result);
    }

    @Test
    public void testProperFractionFormatZeroNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "0 1/2";

        Fraction result = format.parse(input, new ParsePosition(0));
        assertEquals(new Fraction(0, 1), result);
    }

    @Test
    public void testProperFractionFormatOneNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 1/2";

        Fraction result = format.parse(input, new ParsePosition(0));
        assertEquals(new Fraction(1, 1), result);
    }

    @Test
    public void testProperFractionFormatLargeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "12345678901234567890 1/2";

        Fraction result = format.parse(input, new ParsePosition(0));
        BigDecimal expectedNumerator = new BigDecimal("12345678901234567890");
        BigDecimal expectedDenominator = new BigDecimal("2");
        Fraction expected = new Fraction(expectedNumerator.intExactValue(), expectedDenominator.intValue());

        assertEquals(expected, result);
    }

    @Test
    public void testProperFractionFormatLargeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/98765432109876543210";

        Fraction result = format.parse(input, new ParsePosition(0));
        BigDecimal expectedNumerator = new BigDecimal("3");
        BigDecimal expectedDenominator = new BigDecimal("98765432109876543210");
        Fraction expected = new Fraction(expectedNumerator.intValue(), expectedDenominator.intValue());

        assertEquals(expected, result);
    }

    @Test
    public void testProperFractionFormatLargeInputs() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input1 = "12345678901234567890 1/2345678901234567890";
        String input2 = "12345678901234567890 1/-2345678901234567890";

        Fraction result1 = format.parse(input1, new ParsePosition(0));
        BigDecimal expectedNumerator1 = new BigDecimal("12345678901234567890");
        BigDecimal expectedDenominator1 = new BigDecimal("2345678901234567890");
        Fraction expected1 = new Fraction(expectedNumerator1.intExactValue(), expectedDenominator1.intValue());

        assertEquals(expected1, result1);

        Fraction result2 = format.parse(input2, new ParsePosition(0));
        BigDecimal expectedNumerator2 = new BigDecimal("12345678901234567890");
        BigDecimal expectedDenominator2 = new BigDecimal("-2345678901234567890");
        Fraction expected2 = new Fraction(expectedNumerator2.intExactValue(), expectedDenominator2.intValue());

        assertEquals(expected2, result2);
    }

    private void validateFormat(ProperFractionFormat format, String input, Fraction expected) {
        Fraction result = format.parse(input, new ParsePosition(0));
        assertEquals(expected, result);

        String output = new StringBuffer().append("Result: ").append(format.format(expected, new StringBuffer(), null)).toString();
        assertEquals(input, output);
    }
}