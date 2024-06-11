package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        Fraction fraction = new Fraction(2, 3);
        format.format(fraction, toAppendTo, pos);
        assertEquals("2 / 3", toAppendTo.toString());

        fraction = new Fraction(5, 8);
        format.format(fraction, toAppendTo, pos);
        assertEquals("5 / 8", toAppendTo.toString());

        fraction = new Fraction(-1, 2);
        format.format(fraction, toAppendTo, pos);
        assertEquals("-1 / 2", toAppendTo.toString());

        fraction = new Fraction(4, 1);
        format.format(fraction, toAppendTo, pos);
        assertEquals("4", toAppendTo.toString());
    }

    @Test
    public void testFormatImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        Fraction fraction = new Fraction(5, 2);
        format.format(fraction, toAppendTo, pos);
        assertEquals("5 / 2", toAppendTo.toString());

        fraction = new Fraction(10, 3);
        format.format(fraction, toAppendTo, pos);
        assertEquals("3 1/3", toAppendTo.toString());

        fraction = new Fraction(-5, 2);
        format.format(fraction, toAppendTo, pos);
        assertEquals("-3 / 2", toAppendTo.toString());

        fraction = new Fraction(12, 1);
        format.format(fraction, toAppendTo, pos);
        assertEquals("12", toAppendTo.toString());
    }

    @Test
    public void testFormatWithCustomNumberFormat() {
        ProperFractionFormat format = new ProperFractionFormat(createCustomNumberFormat());
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        Fraction fraction = new Fraction(2, 3);
        format.format(fraction, toAppendTo, pos);
        assertEquals("TWO / THREE", toAppendTo.toString());

        fraction = new Fraction(5, 8);
        format.format(fraction, toAppendTo, pos);
        assertEquals("FIVE / EIGHT", toAppendTo.toString());

        fraction = new Fraction(-1, 2);
        format.format(fraction, toAppendTo, pos);
        assertEquals("-ONE / TWO", toAppendTo.toString());

        fraction = new Fraction(4, 1);
        format.format(fraction, toAppendTo, pos);
        assertEquals("FOUR", toAppendTo.toString());
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        String input = "2 / 3";
        Fraction fraction = format.parse(input, pos);
        assertEquals(new Fraction(2, 3), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "5 / 8";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(5, 8), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "-1 / 2";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(-1, 2), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "4";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(4, 1), fraction);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        String input = "3 1/3";
        Fraction fraction = format.parse(input, pos);
        assertEquals(new Fraction(3, 1, 3), fraction);
        assertEquals(input.length() - 1, pos.getIndex());

        input = "5/2";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(5, 2), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "-3/2";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(-3, 2), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "12";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(12, 1), fraction);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseWithCustomNumberFormat() {
        ProperFractionFormat format = new ProperFractionFormat(createCustomNumberFormat());
        ParsePosition pos = new ParsePosition(0);

        String input = "TWO / THREE";
        Fraction fraction = format.parse(input, pos);
        assertEquals(new Fraction(2, 3), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "FIVE / EIGHT";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(5, 8), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "-ONE / TWO";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(-1, 2), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "FOUR";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(4, 1), fraction);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        String input = "--1 / 2";
        Fraction fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());

        input = "-3 -1/2";
        fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());

        input = "2 / / 3";
        fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());

        input = "2 / 3 4";
        fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseCustomNumberFormat() {
        ProperFractionFormat format = new ProperFractionFormat(createCustomNumberFormat());
        ParsePosition pos = new ParsePosition(0);

        String input = "TWO / THREE";
        Fraction fraction = format.parse(input, pos);
        assertEquals(new Fraction(2, 3), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "FIVE / EIGHT";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(5, 8), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "-ONE / TWO";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(-1, 2), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "FOUR";
        fraction = format.parse(input, pos);
        assertEquals(new Fraction(4, 1), fraction);
        assertEquals(input.length(), pos.getIndex());

        input = "TWO / THREE with spaces";
        Fraction fraction2 = format.parse(input, pos);
        assertEquals(new Fraction(2, 3), fraction2);
        assertEquals(input.length(), pos.getIndex());

        input = "FIVE / EIGHT with spaces";
        Fraction fraction3 = format.parse(input, pos);
        assertEquals(new Fraction(5, 8), fraction3);
        assertEquals(input.length(), pos.getIndex());
    }

    private NumberFormat createCustomNumberFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        wholeFormat.setMinimumIntegerDigits(1);
        wholeFormat.setGroupingUsed(false);
        wholeFormat.setPositivePrefix("TWO");
        wholeFormat.setNegativePrefix("-");

        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        numeratorFormat.setMinimumIntegerDigits(1);
        numeratorFormat.setGroupingUsed(false);
        numeratorFormat.setPositivePrefix("FIVE");
        numeratorFormat.setNegativePrefix("-");

        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        denominatorFormat.setMinimumIntegerDigits(1);
        denominatorFormat.setGroupingUsed(true);
        denominatorFormat.setPositivePrefix("EIGHT");
        denominatorFormat.setNegativePrefix("-");

        return new DecimalFormat("#", new DecimalFormatSymbols(Locale.ENGLISH)) {
            @Override
            protected synchronized Number parseNumber(String source, ParsePosition pos) {
                if (pos.getIndex() == 0 && source.regionMatches(true, 0, "ONE", 0, 3)) {
                    pos.setIndex(pos.getIndex() + 3);
                    pos.setErrorIndex(pos.getIndex());
                    return new Integer(1);
                }
                return super.parseNumber(source, pos);
            }
        };
    }
}