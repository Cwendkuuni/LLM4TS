
package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction1 = new Fraction(3, 4);
        assertEquals("0 3 / 4", format.format(fraction1, sb, pos).toString());

        sb = new StringBuffer();
        pos = new FieldPosition(0);
        Fraction fraction2 = new Fraction(-5, 2);
        assertEquals("-2 1 / 2", format.format(fraction2, sb, pos).toString());

        sb = new StringBuffer();
        pos = new FieldPosition(0);
        Fraction fraction3 = new Fraction(5, 1);
        assertEquals("5", format.format(fraction3, sb, pos).toString());

        sb = new StringBuffer();
        pos = new FieldPosition(0);
        Fraction fraction4 = new Fraction(0, 1);
        assertEquals("0", format.format(fraction4, sb, pos).toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertTrue(wholeFormat instanceof NumberFormat);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction1 = format.parse("3 / 4", pos);
        assertEquals(3, fraction1.getNumerator());
        assertEquals(4, fraction1.getDenominator());

        pos = new ParsePosition(0);
        Fraction fraction2 = format.parse("-2 1 / 2", pos);
        assertEquals(-5, fraction2.getNumerator());
        assertEquals(2, fraction2.getDenominator());

        pos = new ParsePosition(0);
        Fraction fraction3 = format.parse("5", pos);
        assertEquals(5, fraction3.getNumerator());
        assertEquals(1, fraction3.getDenominator());

        pos = new ParsePosition(0);
        Fraction fraction4 = format.parse("0", pos);
        assertEquals(0, fraction4.getNumerator());
        assertEquals(1, fraction4.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}
