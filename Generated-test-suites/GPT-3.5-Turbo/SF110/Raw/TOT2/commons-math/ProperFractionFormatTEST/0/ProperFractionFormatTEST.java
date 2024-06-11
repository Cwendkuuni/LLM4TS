package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer resultString = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(2, 3);
        StringBuffer expected = new StringBuffer("2 / 3");
        assertEquals(expected.toString(), format.format(fraction, resultString, pos).toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newWholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(newWholeFormat);
        assertEquals(newWholeFormat, format.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(2, 3);
        Fraction result = format.parse("2 / 3", pos);
        assertEquals(expected, result);
    }

    @Test
    public void testNullWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        try {
            format.setWholeFormat(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }
}