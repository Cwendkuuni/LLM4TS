package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.NumberFormat;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();

        // test format method
        String input = "1 2/3";
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        ProperFractionFormat.Fraction f = pff.parse(new Fraction(1, 2), sb, pos);
        assertEquals("1 2/3", pff.format(f, sb, pos).toString());

        // test parse method
        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = pff.parse(input, pp);
        assertEquals("1 2/3", parsedFraction.toString());
    }

    @Test
    public void testCustomFormat() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        wholeFormat.setMinimumFractionDigits(2);
        numeratorFormat.setMinimumFractionDigits(2);
        denominatorFormat.setMinimumFractionDigits(2);

        ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        // test format method
        input = "1.23 2.45/6.78";
        sb = new StringBuffer();
        pos = new FieldPosition(0, 0);
        ProperFractionFormat.Fraction f = pff.parse(new Fraction(1, 2), sb, pos);
        assertEquals("1.23 2.45/6.78", pff.format(f, sb, pos).toString());

        // test parse method
        pp = new ParsePosition(0);
        Fraction parsedFraction = pff.parse(input, pp);
        assertEquals("1.23 2.45/6.78", parsedFraction.toString());
    }

    @Test
    public void testParseNegativeWholeNumber() {
        ProperFractionFormat pff = new ProperFractionFormat();

        String input = "-1 2/3";
        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = pff.parse(input, pp);
        assertEquals("-1 2/3", parsedFraction.toString());
        assertEquals(-7, parsedFraction.getWholeNumber().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNegativeFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();

        String input = "-1 -1/2";
        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = pff.parse(input, pp);
    }

    @Test
    public void testParseLargeNumbers() {
        ProperFractionFormat pff = new ProperFractionFormat();

        String input = "12345678901234567890 12345678901234567890/98765432109876543210";
        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = pff.parse(input, pp);
        assertTrue(parsedFraction.getDenominator().compareTo(new BigInteger("98765432109876543210")) > 0);
        assertTrue(parsedFraction.getNumerator().compareTo(new BigInteger("12345678901234567890")) > 0);
    }

    @Test
    public void testParseDecimalNumbers() {
        ProperFractionFormat pff = new ProperFractionFormat();

        String input = "1.23 3.45/5.67";
        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = pff.parse(input, pp);
        assertEquals("1.23 3.45/5.67", parsedFraction.toString());
        BigDecimal whole = new BigDecimal(parsedFraction.getWholeNumber().toString());
        BigDecimal numerator = new BigDecimal(parsedFraction.getNumerator().toString());
        BigDecimal denominator = new BigDecimal(parsedFraction.getDenominator().toString());
        assertEquals(new BigDecimal("1.23"), whole.add(numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    public void testFormatDecimalNumbers() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);

        ProperFractionFormat pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);

        String input = "1.23 3.45/5.67";
        StringBuffer output = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        Fraction f = new Fraction(1, 2);
        pff.format(f, output, pos);
        assertEquals("1.23 3.45/5.67", output.toString());
    }
}