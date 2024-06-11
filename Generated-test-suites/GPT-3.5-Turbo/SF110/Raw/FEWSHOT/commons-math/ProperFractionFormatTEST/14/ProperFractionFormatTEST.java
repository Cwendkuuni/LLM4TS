package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat expectedWholeFormat = FractionFormat.getDefaultNumberFormat();
        assertEquals(expectedWholeFormat, format.getWholeFormat());
        assertEquals(expectedWholeFormat, format.getNumeratorFormat());
        assertEquals(expectedWholeFormat, format.getDenominatorFormat());
    }

    @Test
    public void testConstructor2() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testConstructor3() {
        NumberFormat expectedWholeFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(expectedWholeFormat);
        assertEquals(expectedWholeFormat, format.getWholeFormat());
        assertEquals(expectedWholeFormat, format.getNumeratorFormat());
        assertEquals(expectedWholeFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer expectedOutput = new StringBuffer("2 / 3");
        assertEquals(expectedOutput, format.format(fraction, toAppendTo, pos));
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedOutput = new Fraction(2, 3);
        assertEquals(expectedOutput, format.parse(source, pos));
        assertEquals(source.length(), pos.getIndex());
        assertNull(format.parse("invalid", pos));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}