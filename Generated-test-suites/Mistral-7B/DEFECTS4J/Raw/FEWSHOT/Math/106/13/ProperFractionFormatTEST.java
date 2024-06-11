package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test formatting
        Fraction input = new Fraction(1, 2);
        String output = format.format(input, new StringBuffer(), null);
        assertEquals("1 / 2", output.toString());

        input = new Fraction(-1, 2);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("-1 / 2", output.toString());

        input = new Fraction(3, 4);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("3 / 4", output.toString());

        input = new Fraction(-3, 4);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("-3 / 4", output.toString());

        input = new Fraction(0, 1);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("0 / 1", output.toString());

        input = new Fraction(2, 1);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("2", output.toString());

        // Test parsing
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse("1 / 2", pos);
        assertNotNull(parsed);
        assertEquals(1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());

        parsed = format.parse("-1 / 2", pos);
        assertNotNull(parsed);
        assertEquals(-1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());

        parsed = format.parse("3 / 4", pos);
        assertNotNull(parsed);
        assertEquals(3, parsed.getNumerator());
        assertEquals(4, parsed.getDenominator());

        parsed = format.parse("-3 / 4", pos);
        assertNotNull(parsed);
        assertEquals(-3, parsed.getNumerator());
        assertEquals(4, parsed.getDenominator());

        parsed = format.parse("0 / 1", pos);
        assertNotNull(parsed);
        assertEquals(0, parsed.getNumerator());
        assertEquals(1, parsed.getDenominator());

        parsed = format.parse("2", pos);
        assertNotNull(parsed);
        assertEquals(2, parsed.getNumerator());
        assertEquals(1, parsed.getDenominator());
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        wholeFormat.setGroupingUsed(false);
        numeratorFormat.setGroupingUsed(false);
        denominatorFormat.setGroupingUsed(false);
        numeratorFormat.setMinimumIntegerDigits(1);
        denominatorFormat.setMinimumIntegerDigits(1);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        // Test formatting
        Fraction input = new Fraction(1, 2);
        String output = format.format(input, new StringBuffer(), null);
        assertEquals("1/2", output.toString());

        input = new Fraction(-1, 2);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("-1/2", output.toString());

        input = new Fraction(3, 4);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("3/4", output.toString());

        input = new Fraction(-3, 4);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("-3/4", output.toString());

        input = new Fraction(0, 1);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("0/1", output.toString());

        input = new Fraction(2, 1);
        output = format.format(input, new StringBuffer(), null);
        assertEquals("2", output.toString());

        // Test parsing
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse("1/2", pos);
        assertNotNull(parsed);
        assertEquals(1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());

        parsed = format.parse("-1/2", pos);
        assertNotNull(parsed);
        assertEquals(-1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());

        parsed = format.parse("3/4", pos);
        assertNotNull(parsed);
        assertEquals(3, parsed.getNumerator());
        assertEquals(4, parsed.getDenominator());

        parsed = format.parse("-3/4", pos);
        assertNotNull(parsed);
        assertEquals(-3, parsed.getNumerator());
        assertEquals(4, parsed.getDenominator());

        parsed = format.parse("0/1", pos);
        assertNotNull(parsed);
        assertEquals(0, parsed.getNumerator());
        assertEquals(1, parsed.getDenominator());

        parsed = format.parse("2", pos);
        assertNotNull(parsed);
        assertEquals(2, parsed.getNumerator());
        assertEquals(1, parsed.getDenominator());

        // Test parsing with big numbers
        Fraction bigInput = new Fraction(new BigDecimal("12345678901234567890"), new BigDecimal("98765432109876543210"));
        parsed = format.parse(bigInput.toString(), pos);
        assertNotNull(parsed);
        assertEquals(bigInput.getNumerator(), parsed.getNumerator());
        assertEquals(bigInput.getDenominator(), parsed.getDenominator());

        parsed = format.parse("-" + bigInput.toString(), pos);
        assertNotNull(parsed);
        assertEquals(-bigInput.getNumerator(), parsed.getNumerator());
        assertEquals(bigInput.getDenominator(), parsed.getDenominator());
    }

    @Test
    public void testParseWithInvalidInput() {
        NumberFormat format = NumberFormat.getNumberInstance();
        ProperFractionFormat properFormat = new ProperFractionFormat(format, format, format);

        ParsePosition pos = new ParsePosition(0);

        // Test invalid input with whitespace
        String input = " 1 / 2 ";
        Fraction parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = " 1 / 2 \n";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "\n 1 / 2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 / 2 ";
        parsed = properFormat.parse(input, pos);
        assertNotNull(parsed);
        assertEquals(1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());
        assertEquals(input.length() - 1, pos.getIndex());

        // Test invalid input with improper fraction
        input = "1 1/2";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 1/2\n";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "\n1 1/2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 1/2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length() - 1, pos.getIndex());

        // Test invalid input with mixed fraction
        input = "1 -1/2";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 -1/2\n";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "\n1 -1/2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 -1/2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length() - 1, pos.getIndex());

        // Test invalid input with negative whole number
        input = "-1 / 2";
        parsed = properFormat.parse(input, pos);
        assertNotNull(parsed);
        assertEquals(-1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());
        assertEquals(input.length() - 1, pos.getIndex());

        input = "-1 / 2 \n";
        parsed = properFormat.parse(input, pos);
        assertNotNull(parsed);
        assertEquals(-1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());
        assertEquals(input.length() - 1, pos.getIndex());

        input = "\n-1 / 2 ";
        parsed = properFormat.parse(input, pos);
        assertNotNull(parsed);
        assertEquals(-1, parsed.getNumerator());
        assertEquals(2, parsed.getDenominator());
        assertEquals(input.length(), pos.getIndex());

        // Test invalid input with negative numerator
        input = "1 / -2";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 / -2 \n";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "\n1 / -2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 / -2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length() - 1, pos.getIndex());

        // Test invalid input with negative denominator
        input = "1 / -2";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 / -2 \n";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "\n1 / -2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());

        input = "1 / -2 ";
        parsed = properFormat.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length() - 1, pos.getIndex());
    }
}