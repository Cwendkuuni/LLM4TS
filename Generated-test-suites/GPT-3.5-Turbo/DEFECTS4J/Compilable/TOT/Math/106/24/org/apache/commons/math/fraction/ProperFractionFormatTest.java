package org.apache.commons.math.fraction;
import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat_noWholeNumber() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);
        assertEquals("1 / 2", result.toString());
    }

    @Test
    public void testFormat_withWholeNumber() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);
        assertEquals("1 1/2", result.toString());
    }

    @Test
    public void testParse_validProperFraction() {
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse("1 1/2", pos);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
        assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void testParse_validImproperFraction() {
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse("3/2", pos);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
        assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void testParse_invalidMinusBeforeWholeNumber() {
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse("-1 1/2", pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void testParse_invalidMinusBeforeFraction() {
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse("1 -1/2", pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());
    }

    @Test
    public void testParse_invalidDoubleMinus() {
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse("-1 -1/2", pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());
    }
}