
package org.apache.commons.math3.fraction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ProperFractionFormatTest {

    @Test
    public void testFormat_withPositiveFraction_shouldReturnFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();

        StringBuffer expected = new StringBuffer();
        expected.append("2 / 3");

        FieldPosition pos = new FieldPosition(NumberFormat.INTEGER_FIELD);
        StringBuffer result = format.format(new Fraction(2, 3), new StringBuffer(), pos);

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testFormat_withNegativeFraction_shouldReturnFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();

        StringBuffer expected = new StringBuffer();
        expected.append("-1 / 5");

        FieldPosition pos = new FieldPosition(NumberFormat.INTEGER_FIELD);
        StringBuffer result = format.format(new Fraction(-1, 5), new StringBuffer(), pos);

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testFormat_withZeroFraction_shouldReturnFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();

        StringBuffer expected = new StringBuffer();
        expected.append("0");

        FieldPosition pos = new FieldPosition(NumberFormat.INTEGER_FIELD);
        StringBuffer result = format.format(new Fraction(0), new StringBuffer(), pos);

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testFormat_withFractionBiggerThanOne_shouldReturnFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();

        StringBuffer expected = new StringBuffer();
        expected.append("1 123 / 456");

        FieldPosition pos = new FieldPosition(NumberFormat.INTEGER_FIELD);
        StringBuffer result = format.format(new Fraction(567, 456), new StringBuffer(), pos);

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testParse_withValidFractionString_shouldReturnParsedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction expected = new Fraction(-15, 7);

        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("-15 / 7", pos);

        assertEquals(expected, result);
        assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParse_withInvalidFractionString_shouldReturnNull() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("invalid fraction", pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void testParse_withFractionWithInvalidDenominator_shouldReturnNull() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("2 / 0", pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }

    @Test
    public void testGetWholeFormat_withValidFormat_shouldReturnWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"));

        NumberFormat expected = new DecimalFormat("0");

        NumberFormat result = format.getWholeFormat();

        assertEquals(expected, result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_withNullFormat_shouldThrowException() {
        ProperFractionFormat format = new ProperFractionFormat();

        format.setWholeFormat(null);
    }
}

