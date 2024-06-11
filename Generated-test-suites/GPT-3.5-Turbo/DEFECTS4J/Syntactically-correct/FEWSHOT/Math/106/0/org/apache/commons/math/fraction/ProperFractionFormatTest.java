package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

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
        Fraction fraction = new Fraction(3, 2);
        StringBuffer toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        assertEquals("3 / 2", toAppendTo.toString());
        
        fraction = new Fraction(-1, 2);
        toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        assertEquals("-1 / 2", toAppendTo.toString());
        
        fraction = new Fraction(0, 2);
        toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        assertEquals("0 / 2", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        String source = "3 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        source = "-1 / 2";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNotNull(fraction);
        assertEquals(-1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        source = "0 / 2";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNotNull(fraction);
        assertEquals(0, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        source = "3 / 0";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertNotNull(pos.getErrorIndex());

        source = "a / 2";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertNotNull(pos.getErrorIndex());

        source = "";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertNotNull(pos.getErrorIndex());
    }
}