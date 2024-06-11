package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        assertEquals("1 / 2", format.format(new Fraction(1, 2), new StringBuffer(), null).toString());
        assertEquals("3 / 4", format.format(new Fraction(3, 4), new StringBuffer(), null).toString());
        assertEquals("5 1/2", format.format(new Fraction(11, 2), new StringBuffer(), null).toString());
        assertEquals("-7 3/4", format.format(new Fraction(-31, 4), new StringBuffer(), null).toString());
        assertEquals("-5", format.format(new Fraction(-10, 2), new StringBuffer(), null).toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        assertEquals(new Fraction(3, 4), format.parse("3 / 4", null));
        assertEquals(new Fraction(7, 2), format.parse("7 1/2", null));
        assertEquals(new Fraction(-17, 3), format.parse("-17 / 3", null));
        assertEquals(new Fraction(-31, 4), format.parse("-7 3/4", null));
        assertNull(format.parse("1 / 2 / 3", null));
        assertNull(format.parse("a / 2", null));
    }
}