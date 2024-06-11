package org.apache.commons.math.fraction.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

/**
 * Test for {@link ProperFractionFormat} class.
 */
public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        NumberFormat numeratorFormat = format.getNumeratorFormat();
        NumberFormat denominatorFormat = format.getDenominatorFormat();
        assertEquals(NumberFormat.getDefault(), wholeFormat);
        assertEquals(NumberFormat.getDefault(), numeratorFormat);
        assertEquals(NumberFormat.getDefault(), denominatorFormat);
    }

    @Test
    public void testCustomWholeFormatConstructor() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertNull(format.getNumeratorFormat());
        assertNull(format.getDenominatorFormat());
    }

    @Test
    public void testCustomFormatConstructor() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0.00");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormatFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        assertEquals("1 / 2", toAppendTo.toString());
    }

    @Test
    public void testParseImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "3";
        Fraction ret = format.parse(source, pos);
        assertEquals(new Fraction(3, 1), ret);
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "1 2/3";
        Fraction ret = format.parse(source, pos);
        assertEquals(new Fraction(1, 3), ret);
        assertEquals(2, pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "-3 -1/2";
        format.parse(source, pos);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = new DecimalFormat("#,##0.000");
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

}