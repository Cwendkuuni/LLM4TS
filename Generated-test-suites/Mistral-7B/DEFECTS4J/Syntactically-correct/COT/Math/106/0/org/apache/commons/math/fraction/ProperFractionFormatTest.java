package org.apache.commons.math.fraction.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;

/**
 * Test class for ProperFractionFormat
 */
public class ProperFractionFormatTest {

    /**
     * Test constructor ProperFractionFormat()
     */
    @Test
    public void testProperFractionFormatConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("ProperFractionFormat with default format",
                pff.toString(),
                "NumberFormat[whole=DecimalFormat{0}, numerator=DecimalFormat{0}, denominator=DecimalFormat{0}]");

        ProperFractionFormat pffDefault = new ProperFractionFormat();
        ProperFractionFormat pffCustom = new ProperFractionFormat(getDefaultNumberFormat());
        assertEquals("ProperFractionFormat with default format should be equal",
                pffDefault.getWholeFormat(), pffCustom.getWholeFormat());
        assertEquals("ProperFractionFormat with default format should be equal",
                pffDefault.getNumeratorFormat(), pffCustom.getNumeratorFormat());
        assertEquals("ProperFractionFormat with default format should be equal",
                pffDefault.getDenominatorFormat(), pffCustom.getDenominatorFormat());
    }

    /**
     * Test constructor ProperFractionFormat(NumberFormat format)
     */
    @Test
    public void testProperFractionFormatFormat() {
        ProperFractionFormat pff = new ProperFractionFormat(getDefaultNumberFormat());
        assertEquals("ProperFractionFormat with custom format",
                pff.toString(),
                "NumberFormat[whole=" + getDefaultNumberFormat().toString() + ", numerator="
                        + getDefaultNumberFormat().toString() + ", denominator="
                        + getDefaultNumberFormat().toString() + "]");

        ProperFractionFormat pffCustom = new ProperFractionFormat(getDefaultNumberFormat(),
                getDefaultNumberFormat(), getDefaultNumberFormat());
        ProperFractionFormat pffCustomFormat = new ProperFractionFormat(new DecimalFormat("0"));
        assertEquals("ProperFractionFormat with custom whole format should be equal",
                pffCustom.getWholeFormat(), pffCustomFormat.getWholeFormat());
        assertEquals("ProperFractionFormat with custom numerator format should be equal",
                pffCustom.getNumeratorFormat(), pffCustomFormat.getNumeratorFormat());
        assertEquals("ProperFractionFormat with custom denominator format should be equal",
                pffCustom.getDenominatorFormat(), pffCustomFormat.getDenominatorFormat());
    }

    /**
     * Test method format
     */
    @Test
    public void testFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction f1 = new Fraction(1, 1);
        Fraction f2 = new Fraction(2, 3);
        Fraction f3 = new Fraction(-3, 2);
        StringBuffer sb = new StringBuffer();

        // Test with whole = 0
        pff.format(f1, sb, new FieldPosition(0, 0));
        assertEquals("Format test with whole = 0", "1", sb.toString());

        // Test with whole not equal to 0
        pff.format(f2, sb, new FieldPosition(0, 0));
        assertEquals("Format test with whole not equal to 0", "2 1/3", sb.toString());

        // Test with negative whole
        pff.format(f3, sb, new FieldPosition(0, 0));
        assertEquals("Format test with negative whole", "-3 1/2", sb.toString());

        // Test with whole and numerator = 0
        Fraction f4 = new Fraction(0, 1);
        pff.format(f4, sb, new FieldPosition(0, 0));
        assertEquals("Format test with whole and numerator = 0", "0", sb.toString());
    }

    /**
     * Test method parse
     */
    @Test
    public void testParse() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String input = "2 1/3";
        ParsePosition pos = new ParsePosition(0);

        // Test with valid input
        Fraction result = pff.parse(input, pos);
        assertEquals("Parse test with valid input", new Fraction(2, 3), result);
        assertEquals("Parse test with valid input, expected index", 5, pos.getIndex());

        // Test with invalid input: missing '/'
        input = "2 1";
        result = pff.parse(input, pos);
        assertNull("Parse test with invalid input, missing '/'", result);
        assertEquals("Parse test with invalid input, expected index", 2, pos.getIndex());

        // Test with invalid input: '/' not in the end
        input = "2 / 3 1";
        result = pff.parse(input, pos);
        assertNull("Parse test with invalid input, '/' not in the end", result);
        assertEquals("Parse test with invalid input, expected index", 4, pos.getIndex());

        // Test with invalid input: negative whole
        input = "-2 1/2";
        result = pff.parse(input, pos);
        assertEquals("Parse test with invalid input, negative whole", new Fraction(-4, 2), result);
        assertEquals("Parse test with invalid input, expected index", 5, pos.getIndex());

        // Test with invalid input: whole = 0
        input = "0 1/2";
        result = pff.parse(input, pos);
        assertNull("Parse test with invalid input, whole = 0", result);
        assertEquals("Parse test with invalid input, expected index", 1, pos.getIndex());

        // Test with invalid input: numerator = 0
        input = "2 0/2";
        result = pff.parse(input, pos);
        assertNull("Parse test with invalid input, numerator = 0", result);
        assertEquals("Parse test with invalid input, expected index", 1, pos.getIndex());

        // Test with invalid input: numerator < 0
        input = "2 -1/2";
        result = pff.parse(input, pos);
        assertNull("Parse test with invalid input, numerator < 0", result);
        assertEquals("Parse test with invalid input, expected index", 2, pos.getIndex());

        // Test with invalid input: denominator = 0
        input = "2 1/0";
        result = pff.parse(input, pos);
        assertNull("Parse test with invalid input, denominator = 0", result);
        assertEquals("Parse test with invalid input, expected index", 2, pos.getIndex());
    }

    /**
     * Test method setWholeFormat
     */
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        NumberFormat format = getDecimalFormatInstance();
        pff.setWholeFormat(format);
        assertNotSame("Set whole format", getDefaultNumberFormat(), pff.getWholeFormat());
        assertEquals("Set whole format, expected format", format.toString(), pff.getWholeFormat().toString());
    }

    private static NumberFormat getDecimalFormatInstance() {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumIntegerDigits(1);
        return decimalFormat;
    }

    private static DecimalFormat getDefaultNumberFormat() {
        DecimalFormat decimalFormat = new DecimalFormat();
        return decimalFormat;
    }

    private static void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
        int index = pos.getIndex();
        while (index < source.length() && Character.isWhitespace(source.charAt(index))) {
            index++;
        }
        pos.setIndex(index);
    }

    private static char parseNextCharacter(String source, ParsePosition pos) {
        int index = pos.getIndex();
        if (index < source.length()) {
            return source.charAt(index);
        }
        return 0;
    }
}