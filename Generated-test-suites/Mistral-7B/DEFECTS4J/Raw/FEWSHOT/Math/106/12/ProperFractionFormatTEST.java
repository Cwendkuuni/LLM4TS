package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        properFractionFormat.format(fraction, toAppendTo, pos);

        assertEquals("-1 / 2", toAppendTo.toString());

        wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMinimumIntegerDigits(2);
        numeratorFormat = NumberFormat.getNumberInstance();
        numeratorFormat.setMinimumIntegerDigits(2);
        denominatorFormat = NumberFormat.getNumberInstance();
        denominatorFormat.setMinimumIntegerDigits(2);

        properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        fraction = new Fraction(3, 4);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        properFractionFormat.format(fraction, toAppendTo, pos);

        assertEquals("3 / 4", toAppendTo.toString());

        wholeFormat = NumberFormat.getPercentInstance();
        numeratorFormat = NumberFormat.getNumberInstance();
        denominatorFormat = NumberFormat.getNumberInstance();

        properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        fraction = new Fraction(5, 8);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        properFractionFormat.format(fraction, toAppendTo, pos);

        assertEquals("-0.625", toAppendTo.toString());

        wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMinimumIntegerDigits(3);
        numeratorFormat = NumberFormat.getNumberInstance();
        numeratorFormat.setMinimumIntegerDigits(3);
        denominatorFormat = NumberFormat.getNumberInstance();
        denominatorFormat.setMinimumIntegerDigits(3);

        properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        fraction = new Fraction(13, 24);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        properFractionFormat.format(fraction, toAppendTo, pos);

        assertEquals("-0.54583333333333333333333333333333", toAppendTo.toString());
    }

    @Test
    public void testFormatImproperFraction() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(5, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        properFractionFormat.format(fraction, toAppendTo, pos);

        assertEquals("2.25", toAppendTo.toString());
    }

    @Test
    public void testParseProperFraction() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertEquals(new Fraction(1, 2), fraction);

        source = "3 / 4";
        pos = new ParsePosition(0);

        fraction = properFractionFormat.parse(source, pos);

        assertEquals(new Fraction(3, 4), fraction);

        source = "-0.625";
        pos = new ParsePosition(0);

        fraction = properFractionFormat.parse(source, pos);

        assertEquals(new Fraction(-1, 2), fraction);

        wholeFormat = NumberFormat.getPercentInstance();
        numeratorFormat = NumberFormat.getNumberInstance();
        denominatorFormat = NumberFormat.getNumberInstance();

        properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        source = "-0.54583333333333333333333333333333";
        pos = new ParsePosition(0);

        fraction = properFractionFormat.parse(source, pos);

        assertEquals(new Fraction(-1, 2), fraction);
    }

    @Test
    public void testParseImproperFraction() {
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(null, numeratorFormat, denominatorFormat);

        String source = "2.25";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertEquals(new Fraction(5, 2), fraction);

        source = "-2.25";
        pos = new ParsePosition(0);

        fraction = properFractionFormat.parse(source, pos);

        assertEquals(new Fraction(-5, 2), fraction);
    }

    @Test
    public void testParseInvalidWholeNumber() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testParseInvalidNumerator() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-3 / -1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testParseInvalidDenominator() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-3 / 0";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testParseInvalidFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-3 / foo";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, pos);

        assertNull(fraction);
        assertEquals(-1, pos.getIndex());
    }
}