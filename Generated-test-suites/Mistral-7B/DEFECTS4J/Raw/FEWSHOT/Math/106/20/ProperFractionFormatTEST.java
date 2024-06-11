package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;

import org.apache.commons.commons.math.fraction.Fraction;
import org.apache.commons.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.commons.math.util.MathUtils;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test default constructor
        Fraction fraction = format.parse("3 1/2", new ParsePosition(0));
        assertEquals(new Fraction(3, 2), fraction);

        StringBuffer stringBuffer = new StringBuffer(100);
        format.format(new Fraction(3, 2), stringBuffer, new FieldPosition(0, 0));
        assertEquals("3 1/2", stringBuffer.toString());

        // Test constructor with custom NumberFormat
        NumberFormat numberFormat = new DecimalFormat("#,##0.00");
        ProperFractionFormat customFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);

        fraction = customFormat.parse("3.5 1/3", new ParsePosition(0));
        assertEquals(new Fraction(11, 3), fraction);

        stringBuffer.setLength(0);
        customFormat.format(new Fraction(11, 3), stringBuffer, new FieldPosition(0, 0));
        assertEquals("3.5 1/3", stringBuffer.toString());

        // Test parse with negative whole number
        ParsePosition pos = new ParsePosition(0);
        Fraction negativeFraction = customFormat.parse("-3.5 1/2", pos);
        assertEquals(new Fraction(-7, 2), negativeFraction);

        // Test parse with improper fraction
        fraction = customFormat.parse("5", pos);
        assertEquals(new Fraction(5, 1), fraction);

        // Test parse with invalid fraction
        String invalidFraction = "--3 1/2";
        pos.setIndex(0);
        fraction = customFormat.parse(invalidFraction, pos);
        assertNull(fraction);
        assertEquals(-1, pos.getIndex());
        assertEquals(-1, pos.getErrorIndex());

        // Test format with null Fraction
        stringBuffer.setLength(0);
        customFormat.format(null, stringBuffer, new FieldPosition(0, 0));
        assertEquals("", stringBuffer.toString());
    }

    @Test
    public void testParseWithBigDecimalAndBigInteger() {
        NumberFormat numberFormat = new DecimalFormat("#,##0.00");
        ProperFractionFormat customFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);

        // Test parse with BigDecimal and BigInteger for whole, numerator and denominator
        BigDecimal whole = new BigDecimal("-321.50");
        BigInteger numerator = new BigInteger("123456789012345678901234567890");
        BigInteger denominator = new BigInteger("987654321098765432109876543210");
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = customFormat.parse(whole.toString() + " " + numerator.toString() + " / " + denominator.toString(), pos);
        assertEquals(new Fraction(whole.longValueExact() * denominator.longValueExact() + numerator.longValueExact(), denominator), fraction);

        // Test parse with only BigDecimal for whole, numerator and denominator
        BigDecimal whole2 = new BigDecimal("-123");
        BigDecimal numerator2 = new BigDecimal("345");
        BigDecimal denominator2 = new BigDecimal("678");
        pos.setIndex(0);
        fraction = customFormat.parse(whole2.toString() + " " + numerator2.toString() + " / " + denominator2.toString(), pos);
        assertEquals(new Fraction(whole2.longValueExact() * denominator2.longValueExact() + numerator2.longValueExact(), denominator2), fraction);
    }
}