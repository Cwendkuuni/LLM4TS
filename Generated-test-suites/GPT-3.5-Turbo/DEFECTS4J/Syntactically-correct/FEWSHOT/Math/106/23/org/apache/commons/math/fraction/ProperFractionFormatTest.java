package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer buffer = format.format(fraction, new StringBuffer(), null);
        assertEquals("3 / 4", buffer.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 / 4", pos);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        assertEquals(6, pos.getIndex());
    }

    @Test
    public void testCustomWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);
        Fraction fraction = new Fraction(7, 4);
        StringBuffer buffer = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 3 / 4", buffer.toString());
    }
}