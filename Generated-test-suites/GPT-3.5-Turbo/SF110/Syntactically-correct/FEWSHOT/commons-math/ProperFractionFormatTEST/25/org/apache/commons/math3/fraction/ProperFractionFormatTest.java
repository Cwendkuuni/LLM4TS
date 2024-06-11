package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;

    @Test
    public void testConstructor1() {
        properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat.getWholeFormat());
        assertNotNull(properFractionFormat.getNumeratorFormat());
        assertNotNull(properFractionFormat.getDenominatorFormat());
    }

    @Test
    public void testConstructor2() {
        NumberFormat format = new DecimalFormat();
        properFractionFormat = new ProperFractionFormat(format);
        assertEquals(format, properFractionFormat.getWholeFormat());
        assertEquals(format, properFractionFormat.getNumeratorFormat());
        assertEquals(format, properFractionFormat.getDenominatorFormat());
    }

    @Test
    public void testConstructor3() {
        NumberFormat wholeFormat = new DecimalFormat();
        NumberFormat numeratorFormat = new DecimalFormat();
        NumberFormat denominatorFormat = new DecimalFormat();
        properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
        assertEquals(numeratorFormat, properFractionFormat.getNumeratorFormat());
        assertEquals(denominatorFormat, properFractionFormat.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        properFractionFormat = new ProperFractionFormat();
        assertEquals("3 / 4", properFractionFormat.format(fraction, toAppendTo, pos).toString());
    }

    @Test
    public void testGetWholeFormat() {
        properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        properFractionFormat = new ProperFractionFormat();
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNotNull(fraction);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat format = new DecimalFormat();
        properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(format);
        assertEquals(format, properFractionFormat.getWholeFormat());
    }
}