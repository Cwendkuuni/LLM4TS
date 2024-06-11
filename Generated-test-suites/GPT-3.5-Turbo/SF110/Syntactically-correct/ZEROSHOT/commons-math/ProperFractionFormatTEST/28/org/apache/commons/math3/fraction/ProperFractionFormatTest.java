package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        NumberFormat format = new DecimalFormat();
        properFractionFormat = new ProperFractionFormat(format);
    }

    @Test
    public void testFormat() {
        assertEquals("1 / 2", properFractionFormat.format(new Fraction(1, 2), new StringBuffer(), null).toString());
        assertEquals("1 / 3", properFractionFormat.format(new Fraction(1, 3), new StringBuffer(), null).toString());
        assertEquals("1 / 4", properFractionFormat.format(new Fraction(1, 4), new StringBuffer(), null).toString());
        assertEquals("2 / 3", properFractionFormat.format(new Fraction(2, 3), new StringBuffer(), null).toString());
        assertEquals("3 / 4", properFractionFormat.format(new Fraction(3, 4), new StringBuffer(), null).toString());
    }

    @Test
    public void testGetWholeFormat() {
        assertNotNull(properFractionFormat.getWholeFormat());
    }

    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("1 / 2", pos);
        assertEquals(new Fraction(1, 2), fraction);
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse("1 / 3", pos);
        assertEquals(new Fraction(1, 3), fraction);
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse("1 / 4", pos);
        assertEquals(new Fraction(1, 4), fraction);
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse("2 / 3", pos);
        assertEquals(new Fraction(2, 3), fraction);
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse("3 / 4", pos);
        assertEquals(new Fraction(3, 4), fraction);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat format = new DecimalFormat();
        properFractionFormat.setWholeFormat(format);
        assertEquals(format, properFractionFormat.getWholeFormat());
    }
}