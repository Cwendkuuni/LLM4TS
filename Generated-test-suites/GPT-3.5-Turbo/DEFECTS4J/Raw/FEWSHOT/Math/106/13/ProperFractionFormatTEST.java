package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("1 1/2", sb.toString());
        
        fraction = new Fraction(-5, 2);
        sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("-2 1/2", sb.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 1/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        
        assertEquals(3, fraction.getWhole());
        assertEquals(1, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        
        source = "-5 3/4";
        pos = new ParsePosition(0);
        fraction = format.parse(source, pos);
        
        assertEquals(-5, fraction.getWhole());
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        
        source = "7/4";
        pos = new ParsePosition(0);
        fraction = format.parse(source, pos);
        
        assertEquals(0, fraction.getWhole());
        assertEquals(7, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }
}