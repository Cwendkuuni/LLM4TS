package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormatWithWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("2 1 / 2", sb.toString());
    }
    
    @Test
    public void testFormatWithoutWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("1 / 2", sb.toString());
    }
    
    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("-1 1 / 2", sb.toString());
    }
    
    @Test
    public void testFormatWithNegativeWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, -2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("-2 1 / 2", sb.toString());
    }
    
    @Test
    public void testFormatWithNegativeWholeAndNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, -2);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("2 1 / 2", sb.toString());
    }
    
    @Test
    public void testParseValidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertEquals(new Fraction(5, 2), fraction);
    }
    
    @Test
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 1 / -2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertNull(fraction);
    }
    
    @Test
    public void testParseInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2/1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertNull(fraction);
    }
}