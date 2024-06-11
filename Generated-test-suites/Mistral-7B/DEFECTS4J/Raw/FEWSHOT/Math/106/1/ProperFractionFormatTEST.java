package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat);

        // test default format
        Fraction fraction = new Fraction(1, 2);
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 / 2", formatted.toString());

        // test custom whole format
        wholeFormat.setGroupingUsed(false);
        formatted = customFormat.format(fraction, new StringBuffer(), null);
        assertEquals("1/2", formatted.toString());

        // test parse
        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = customFormat.parse("1/2", pos);
        assertEquals(fraction, parsedFraction);

        // test format with negative whole number
        wholeFormat.setPositivePrefix("(-) ");
        format = new ProperFractionFormat(wholeFormat);
        formatted = format.format(new Fraction(-1, 2), new StringBuffer(), null);
        assertEquals("-1 / 2", formatted.toString());

        // test parse with negative whole number
        formatted = "-1/2";
        parsedFraction = customFormat.parse(formatted, pos);
        assertEquals(new Fraction(-1, 2), parsedFraction);

        // test parse with negative whole number and negative numerator
        formatted = "-1 / -2";
        parsedFraction = customFormat.parse(formatted, pos);
        assertEquals(new Fraction(1, 2), parsedFraction);

        // test parse with multiple whitespaces
        formatted = "1 / 2 ";
        parsedFraction = customFormat.parse(formatted, pos);
        assertEquals(fraction, parsedFraction);

        // test parse with multiple slashes
        formatted = "1 / 2 / 3";
        parsedFraction = customFormat.parse(formatted, pos);
        assertNull(parsedFraction);

        // test format with big numbers
        BigDecimal whole = new BigDecimal("12345678901234567890");
        BigInteger numerator = new BigInteger("13579246890135792468901357");
        BigInteger denominator = new BigInteger("2345678901234567890123456789");
        format = new ProperFractionFormat(NumberFormat.getNumberInstance());
        formatted = format.format(new Fraction(numerator, denominator), new StringBuffer(), null);
        String expected = "13579246890135792468901357 / 2345678901234567890123456789";
        assertEquals(expected, formatted.toString());

        // test parse with big numbers
        pos = new ParsePosition(0);
        formatted = "13579246890135792468901357 / 2345678901234567890123456789";
        parsedFraction = customFormat.parse(formatted, pos);
        Fraction expectedFraction = new Fraction(numerator, denominator);
        assertEquals(expectedFraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(expectedFraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(expectedFraction.toString(), parsedFraction.toString());
    }
}