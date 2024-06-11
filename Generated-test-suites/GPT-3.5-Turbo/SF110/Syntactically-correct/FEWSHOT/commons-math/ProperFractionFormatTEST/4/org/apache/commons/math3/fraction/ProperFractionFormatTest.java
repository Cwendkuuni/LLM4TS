package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.LocalizedFormats;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.BigFraction;
import org.apache.commons.math3.util.BigFractionField;
import org.apache.commons.math3.util.NumberFormatException;
import org.apache.commons.math3.util.Transformer;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        StringBuffer expected = new StringBuffer("3/4");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, buffer, fieldPosition).toString());
    }

    @Test
    public void testFormat_WholeNumber() {
        Fraction fraction = new Fraction(2, 1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        StringBuffer expected = new StringBuffer("2");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, buffer, fieldPosition).toString());
    }

    @Test
    public void testFormat_WholeNumberNegativeNumerator() {
        Fraction fraction = new Fraction(2, -1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        StringBuffer expected = new StringBuffer("-2");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, buffer, fieldPosition).toString());
    }

    @Test
    public void testFormat_WholeNumberNegativeDenominator() {
        Fraction fraction = new Fraction(2, -1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        StringBuffer expected = new StringBuffer("-2");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, buffer, fieldPosition).toString());
    }

    @Test
    public void testFormat_WholeNumberZeroDenominator() {
        Fraction fraction = new Fraction(2, 0);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        StringBuffer expected = new StringBuffer("NaN");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, buffer, fieldPosition).toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFormat_NullFraction() {
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(null, buffer, fieldPosition);
    }

    @Test
    public void testParse() {
        String source = "3/4";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_WholeNumber() {
        String source = "2";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(2, 1);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_WholeNumberNegativeNumerator() {
        String source = "-2";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(-2, 1);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_WholeNumberNegativeDenominator() {
        String source = "2/-1";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(2, -1);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_WholeNumberZeroDenominator() {
        String source = "2/0";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(2, 0);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_InvalidSource() {
        String source = "1.5";
        ParsePosition parsePosition = new ParsePosition(0);

        assertNull(properFractionFormat.parse(source, parsePosition));
        assertEquals(0, parsePosition.getIndex());
    }

    @Test
    public void testParse_InvalidFraction() {
        String source = "3/0";
        ParsePosition parsePosition = new ParsePosition(0);

        assertNull(properFractionFormat.parse(source, parsePosition));
        assertEquals(0, parsePosition.getIndex());
    }

    @Test(expected = NullPointerException.class)
    public void testParse_NullSource() {
        ParsePosition parsePosition = new ParsePosition(0);
        properFractionFormat.parse(null, parsePosition);
    }

    @Test
    public void testParse_EmptySource() {
        String source = "";
        ParsePosition parsePosition = new ParsePosition(0);

        assertNull(properFractionFormat.parse(source, parsePosition));
        assertEquals(0, parsePosition.getIndex());
    }

    @Test
    public void testParse_PositiveInfinity() {
        String source = "+Infinity";
        ParsePosition parsePosition = new ParsePosition(0);

        assertNull(properFractionFormat.parse(source, parsePosition));
        assertEquals(0, parsePosition.getIndex());
    }

    @Test
    public void testParse_NegativeInfinity() {
        String source = "-Infinity";
        ParsePosition parsePosition = new ParsePosition(0);

        assertNull(properFractionFormat.parse(source, parsePosition));
        assertEquals(0, parsePosition.getIndex());
    }

    @Test
    public void testParse_NaN() {
        String source = "NaN";
        ParsePosition parsePosition = new ParsePosition(0);

        assertNull(properFractionFormat.parse(source, parsePosition));
        assertEquals(0, parsePosition.getIndex());
    }

    @Test
    public void testParse_Infinity() {
        String source = "Infinity";
        ParsePosition parsePosition = new ParsePosition(0);

        assertNull(properFractionFormat.parse(source, parsePosition));
        assertEquals(0, parsePosition.getIndex());
    }

    @Test
    public void testParse_IntegerMaxValue() {
        String source = Integer.toString(Integer.MAX_VALUE);
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(Integer.MAX_VALUE, 1);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_IntegerMinValue() {
        String source = Integer.toString(Integer.MIN_VALUE);
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(Integer.MIN_VALUE, 1);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_LongMaxValue() {
        String source = Long.toString(Long.MAX_VALUE);
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(Long.MAX_VALUE, 1);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }

    @Test
    public void testParse_LongMinValue() {
        String source = Long.toString(Long.MIN_VALUE);
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction expected = new Fraction(Long.MIN_VALUE, 1);
        assertEquals(expected, properFractionFormat.parse(source, parsePosition));
    }
}